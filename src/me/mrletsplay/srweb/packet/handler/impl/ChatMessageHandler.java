package me.mrletsplay.srweb.packet.handler.impl;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.bot.BotPlayer;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientChatMessage;
import me.mrletsplay.srweb.packet.impl.PacketServerEventLogEntry;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;

public class ChatMessageHandler extends SingleTypePacketHandler<PacketClientChatMessage> {
	
	public ChatMessageHandler() {
		super(PacketClientChatMessage.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientChatMessage data) {
		Room r = player.getRoom();
		GameState s = r.getGameState();
		
		if(!data.isValid() || s.isPlayerDead(player)) return PacketServerNoData.INSTANCE;
		
		if(data.getMessage().equalsIgnoreCase("/addbot")) {
			BotPlayer bPl = new BotPlayer();
			if(player.getRoom().isGameRunning()) {
				player.send(new Packet(new PacketServerEventLogEntry("Can't add bot: Game is running", false)));
				return PacketServerNoData.INSTANCE;
			}
			
			if(player.getRoom().isFull()) {
				player.send(new Packet(new PacketServerEventLogEntry("Can't add bot: Room is full", false)));
				return PacketServerNoData.INSTANCE;
			}
			
			player.getRoom().addPlayer(bPl);
			return PacketServerNoData.INSTANCE;
		}
		
		r.broadcastEventLogEntry(player.getName() + ": " + data.getMessage(), true);
		
		return PacketServerNoData.INSTANCE;
	}

}
