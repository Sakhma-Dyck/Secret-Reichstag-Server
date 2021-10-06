package me.mrletsplay.srweb.packet.handler.impl;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.game.state.VoteState;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientSelectChancellor;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;

public class SelectChancellorHandler extends SingleTypePacketHandler<PacketClientSelectChancellor> {

	public SelectChancellorHandler() {
		super(PacketClientSelectChancellor.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientSelectChancellor data) {
		Room r = player.getRoom();
		GameState s = r.getGameState();
		
		if(s.getMoveState().equals(GameMoveState.SELECT_CHANCELLOR) && s.getPresident().equals(player)) {
			Player ch = r.getPlayer(data.getPlayerID());
			
			if(ch.equals(s.getBlockedPlayer())
					|| ch.equals(s.getPreviousPresident())
					|| ch.equals(s.getPreviousChancellor())
					|| ch.equals(s.getPresident())
					|| s.isPlayerDead(ch)) return PacketServerNoData.INSTANCE;
			
			r.broadcastEventLogEntry(player.getName() + " proposes " + ch.getName() + " as chancellor");
			
			s.setChancellor(ch);
			s.setMoveState(GameMoveState.VOTE);
			s.setVoteState(new VoteState(s));
			r.broadcastStateUpdate();
		}
		
		return PacketServerNoData.INSTANCE;
	}
	
}
