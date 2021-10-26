package me.mrletsplay.srweb.packet.handler.impl;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.game.state.VoteState;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientVote;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;
import me.mrletsplay.srweb.packet.impl.PacketServerVoteResults;

public class VoteHandler extends SingleTypePacketHandler<PacketClientVote> {

	public VoteHandler() {
		super(PacketClientVote.class);
	}

	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientVote data) {
		Room r = player.getRoom();
		if(!r.isGameRunning()) return PacketServerNoData.INSTANCE;
		
		GameState s = r.getGameState();
		if(s.getMoveState().equals(GameMoveState.VOTE)) {
			if(s.isPlayerDead(player)) return PacketServerNoData.INSTANCE;
			
			VoteState v = s.getVoteState();
			v.putVote(player, data.isYes());
			if(v.isComplete()) {
				PacketServerVoteResults vr = new PacketServerVoteResults(v);
				r.getPlayers().forEach(p -> p.send(new Packet(vr)));
				
				if(v.isVoteYes()) {
					r.broadcastEventLogEntry(String.format("Election results: %s yes, %s no (%s)",
							v.getNumYes(),
							v.getNumNo(),
							v.isVoteYes() ? "passed" : "failed"));
					
					if(s.getFascistBoard().getNumCards() >= 3) {
						if(s.getChancellor().equals(s.getHitler())) {
							r.setWinner(GameParty.FASCIST);
							r.stopGame();
							return PacketServerNoData.INSTANCE;
						}else {
							s.addNotHitlerConfirmed(s.getChancellor());
						}
					}
					
					
					if(s.getCommunistBoard() != null && s.getCommunistBoard().getNumCards() >= 3) {
						if(s.getChancellor().equals(s.getStalin())) {
							r.setWinner(GameParty.COMMUNIST);
							r.stopGame();
							return PacketServerNoData.INSTANCE;
						}else {
							s.addNotStalinConfirmed(s.getChancellor());
						}
					}
					
					s.resetFailedElections();
					s.setMoveState(GameMoveState.DRAW_CARDS);
					r.broadcastStateUpdate();
				}else {
					r.broadcastEventLogEntry(String.format("Election results: %s yes, %s no (%s)",
							v.getNumYes(),
							v.getNumNo(),
							v.isVoteYes() ? "passed" : "failed"));
					
					s.setMoveState(GameMoveState.SELECT_CHANCELLOR);
					s.advancePresident();
					s.setChancellor(null);
					s.addFailedElection();
					
					if(s.getFailedElections() == 3) {
						r.broadcastEventLogEntry("The top card of the card pile will be put on the board because of too many failed elections");
						s.randomCard();
						s.resetFailedElections();
					}
					
					r.broadcastStateUpdate();
				}
			}
		}
		
		return PacketServerNoData.INSTANCE;
	}
	
}
