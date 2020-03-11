 package me.mrletsplay.ssweb.packet.handler.impl;

import java.util.Collections;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.game.state.GameMoveState;
import me.mrletsplay.ssweb.game.state.GameState;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientVeto;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;
import me.mrletsplay.ssweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.ssweb.packet.impl.PacketServerVeto;

public class VetoHandler extends SingleTypePacketHandler<PacketClientVeto> {
	
	public VetoHandler() {
		super(PacketClientVeto.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientVeto data) {
		Room r = player.getRoom();
		GameState s = r.getGameState();
		
		if(s.getMoveState().equals(GameMoveState.DISCARD_CHANCELLOR) && s.getChancellor().equals(player) && s.isVetoPowerUnlocked() && !s.isVetoBlocked()) {
			s.setVetoRequested(true);
			r.broadcastEventLogEntry(player.getName() + " has requested a veto");
			
			r.broadcastStateUpdate();
			
			s.getPresident().send(new Packet(new PacketServerVeto()));
		}else if(s.getMoveState().equals(GameMoveState.DISCARD_CHANCELLOR) && s.getPresident().equals(player) && s.isVetoRequested()) {
			s.setVetoRequested(false);
			
			if(data.isAcceptVeto()) {
				r.broadcastEventLogEntry(player.getName() + " has accepted the veto");
				s.getDiscardPile().addAll(s.getChancellor().getHand());
				s.getChancellor().setHand(Collections.emptyList());
				s.addFailedElection();
				s.advanceRound();
			}else {
				r.broadcastEventLogEntry(player.getName() + " has declined the veto");
				s.setVetoBlocked(true);
				s.getChancellor().send(new Packet(new PacketServerPickCards(s.getChancellor().getHand(), true)));
			}
			
			r.broadcastStateUpdate();
		}
		return PacketServerNoData.INSTANCE;
	}

}
