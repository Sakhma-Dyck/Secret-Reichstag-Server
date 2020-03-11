package me.mrletsplay.ssweb.packet.handler;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;

public abstract class SingleTypePacketHandler<T extends PacketData> extends PacketHandler {

	private Class<T> handlingType;
	
	public SingleTypePacketHandler(Class<T> handlingType) {
		super(handlingType);
		this.handlingType = handlingType;
	}
	
	@Override
	public PacketData handle(Player player, Packet packet, PacketData data) {
		return handleSingle(player, packet, handlingType.cast(packet.getData()));
	}
	
	public abstract PacketData handleSingle(Player player, Packet packet, T data);

}
