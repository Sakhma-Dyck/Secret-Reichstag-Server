package me.mrletsplay.ssweb.packet.handler.impl;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.game.state.GameMoveState;
import me.mrletsplay.ssweb.game.state.GameParty;
import me.mrletsplay.ssweb.game.state.GameState;
import me.mrletsplay.ssweb.game.state.VoteState;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientVote;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;
import me.mrletsplay.ssweb.packet.impl.PacketServerVoteResults;

public class VoteHandler extends SingleTypePacketHandler<PacketClientVote> {

	public VoteHandler() {
		super(PacketClientVote.class);
	}

	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientVote data) {
		Room r = player.getRoom();
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
					
					
					if(s.getCommunistBoard().getNumCards() >= 3) {
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
