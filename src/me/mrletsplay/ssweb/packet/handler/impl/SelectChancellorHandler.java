package me.mrletsplay.ssweb.packet.handler.impl;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.game.state.GameMoveState;
import me.mrletsplay.ssweb.game.state.GameState;
import me.mrletsplay.ssweb.game.state.VoteState;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientSelectChancellor;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;

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
			
			s.setChancellor(ch);
			s.setMoveState(GameMoveState.VOTE);
			s.setVoteState(new VoteState(s));
			r.broadcastStateUpdate();
			
			r.broadcastEventLogEntry(player.getName() + " proposes " + ch.getName() + " as chancellor");
		}
		
		return PacketServerNoData.INSTANCE;
	}
	
}
