package me.mrletsplay.ssweb.packet.handler.impl;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientStartGame;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;

public class StartGameHandler extends SingleTypePacketHandler<PacketClientStartGame> {

	public StartGameHandler() {
		super(PacketClientStartGame.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientStartGame data) {
		Room r = player.getRoom();

		if(!r.isGameRunning() && r.getPlayers().size() >= r.getSettings().getPlayerCount()) {
			r.startGame();
			r.broadcastEventLogEntry("Game started!");
			r.broadcastEventLogEntry("The first president is " + r.getGameState().getPresident().getName());
		}
		
		return PacketServerNoData.INSTANCE;
	}
	
}
