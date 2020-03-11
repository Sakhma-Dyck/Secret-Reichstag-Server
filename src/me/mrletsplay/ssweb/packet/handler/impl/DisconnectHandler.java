package me.mrletsplay.ssweb.packet.handler.impl;

import me.mrletsplay.ssweb.SSWeb;
import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketDisconnect;

public class DisconnectHandler extends SingleTypePacketHandler<PacketDisconnect> {

	public DisconnectHandler() {
		super(PacketDisconnect.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketDisconnect data) {
		SSWeb.removePlayer(player);
		player.getWebSocket().close();
		return null;
	}

}
