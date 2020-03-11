package me.mrletsplay.ssweb.packet.handler.impl;

import java.util.List;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.game.state.GameMoveState;
import me.mrletsplay.ssweb.game.state.GamePolicyCard;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientDrawCards;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;
import me.mrletsplay.ssweb.packet.impl.PacketServerPickCards;

public class DrawCardsHandler extends SingleTypePacketHandler<PacketClientDrawCards> {

	public DrawCardsHandler() {
		super(PacketClientDrawCards.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientDrawCards data) {
		Room r = player.getRoom();
		if(r.getGameState().getMoveState().equals(GameMoveState.DRAW_CARDS) && r.getGameState().getPresident().equals(player)) {
			List<GamePolicyCard> drawnCards = r.getGameState().drawCards();
			player.setHand(drawnCards);
			r.getGameState().setMoveState(GameMoveState.DISCARD_PRESIDENT);
			r.broadcastStateUpdate();
			player.send(new Packet(new PacketServerPickCards(drawnCards, false)));
			System.out.println("DRAW: " + drawnCards);
		}
		return PacketServerNoData.INSTANCE;
	}

}
