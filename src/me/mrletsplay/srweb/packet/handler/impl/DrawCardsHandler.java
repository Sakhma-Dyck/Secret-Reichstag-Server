package me.mrletsplay.srweb.packet.handler.impl;

import java.util.List;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientDrawCards;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;
import me.mrletsplay.srweb.packet.impl.PacketServerPickCards;

public class DrawCardsHandler extends SingleTypePacketHandler<PacketClientDrawCards> {

	public DrawCardsHandler() {
		super(PacketClientDrawCards.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientDrawCards data) {
		Room r = player.getRoom();
		if(!r.isGameRunning()) return PacketServerNoData.INSTANCE;
		
		if(r.getGameState().getMoveState().equals(GameMoveState.DRAW_CARDS) && r.getGameState().getPresident().equals(player)) {
			List<GamePolicyCard> drawnCards = r.getGameState().drawCards();
			player.setHand(drawnCards);
			r.getGameState().setMoveState(GameMoveState.DISCARD_PRESIDENT);
			r.broadcastStateUpdate();
			player.send(new Packet(new PacketServerPickCards(drawnCards, false)));
		}
		return PacketServerNoData.INSTANCE;
	}

}
