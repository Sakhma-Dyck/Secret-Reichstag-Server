package me.mrletsplay.srweb.packet.handler.impl;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientStartGame;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;

public class StartGameHandler extends SingleTypePacketHandler<PacketClientStartGame> {

	public StartGameHandler() {
		super(PacketClientStartGame.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientStartGame data) {
		Room r = player.getRoom();

		if(!r.isGameRunning() && r.getPlayers().size() >= r.getSettings().getMinPlayerCount()) {
			r.startGame();
			r.broadcastEventLogEntry("Game started!");
			r.broadcastEventLogEntry("The first president is " + r.getGameState().getPresident().getName());
		}
		
		return PacketServerNoData.INSTANCE;
	}
	
}
