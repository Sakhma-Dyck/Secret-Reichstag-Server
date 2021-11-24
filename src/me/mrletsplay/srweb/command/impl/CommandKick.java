package me.mrletsplay.srweb.command.impl;

import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.srweb.command.SRCommand;
import me.mrletsplay.srweb.game.Player;

public class CommandKick extends SRCommand {
	
	public CommandKick() {
		super("kick");
		setDescription("Kick a player from the game");
		setUsage("/kick <Player>");
		setAllowPregame(true);
	}

	@Override
	public void invoke(Player sender, CommandInvokedEvent event) {
		if(event.getArguments().length != 1) {
			sendCommandInfo(sender);
			return;
		}
		
		Player pl = sender.getRoom().getPlayerByName(event.getArguments()[0]);
		if(pl == null) {
			sender.sendMessage("Can't kick player: Player not found");
			return;
		}
		
		if(pl.getID().equals(sender.getID())) {
			sender.sendMessage("Can't kick player: You can't kick yourself");
			return;
		}
		
		sender.getRoom().removePlayer(pl);
		pl.disconnect("Kicked by the room host");
	}

}
