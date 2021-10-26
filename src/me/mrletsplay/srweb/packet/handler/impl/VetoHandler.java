 package me.mrletsplay.srweb.packet.handler.impl;

import java.util.Collections;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientVeto;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;
import me.mrletsplay.srweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.srweb.packet.impl.PacketServerVeto;

public class VetoHandler extends SingleTypePacketHandler<PacketClientVeto> {
	
	public VetoHandler() {
		super(PacketClientVeto.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientVeto data) {
		Room r = player.getRoom();
		if(!r.isGameRunning()) return PacketServerNoData.INSTANCE;
		
		GameState s = r.getGameState();
		if(s.getMoveState().equals(GameMoveState.DISCARD_CHANCELLOR) && s.getChancellor().equals(player) && s.isVetoPowerUnlocked() && !s.isVetoBlocked() && !s.isVetoRequested()) {
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
