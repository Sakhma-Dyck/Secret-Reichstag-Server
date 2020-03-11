package me.mrletsplay.ssweb.packet.handler;

import java.util.Arrays;
import java.util.List;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;

public abstract class PacketHandler {
	
	private List<Class<? extends PacketData>> handlingTypes;
	
	@SafeVarargs
	public PacketHandler(Class<? extends PacketData>... handlingTypes) {
		this.handlingTypes = Arrays.asList(handlingTypes);
	}
	
	public abstract PacketData handle(Player player, Packet packet, PacketData data);
	
	public boolean shouldHandle(Packet packet) {
		return handlingTypes.stream().anyMatch(c -> c.isInstance(packet.getData()));
	}

}
