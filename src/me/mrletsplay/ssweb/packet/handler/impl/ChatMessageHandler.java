package me.mrletsplay.ssweb.packet.handler.impl;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.game.state.GameState;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientChatMessage;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;

public class ChatMessageHandler extends SingleTypePacketHandler<PacketClientChatMessage> {
	
	public ChatMessageHandler() {
		super(PacketClientChatMessage.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientChatMessage data) {
		Room r = player.getRoom();
		GameState s = r.getGameState();
		
		if(!data.isValid() || s.isPlayerDead(player)) return PacketServerNoData.INSTANCE;
		
		r.broadcastEventLogEntry(player.getName() + ": " + data.getMessage(), true);
		
		return PacketServerNoData.INSTANCE;
	}

}
