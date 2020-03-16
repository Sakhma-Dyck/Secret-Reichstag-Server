package me.mrletsplay.srweb.packet.handler;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;

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
