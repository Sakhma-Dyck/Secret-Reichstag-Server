package me.mrletsplay.srweb.packet.handler.impl;

import me.mrletsplay.mrcore.command.parser.CommandParsingException;
import me.mrletsplay.srweb.command.SRCommandProvider;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientChatMessage;
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
		
		if(data.getMessage().startsWith("/")) {
			String commandLine = data.getMessage().substring(1);
			
			try {
				SRCommandProvider.INSTANCE.invoke(player, commandLine);
			}catch(CommandParsingException e) {
				e.send(player);
			}
			
			return PacketServerNoData.INSTANCE;
		}
		
		r.broadcastEventLogEntry(player.getName() + ": " + data.getMessage(), true);
		
		return PacketServerNoData.INSTANCE;
	}

}
