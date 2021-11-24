package me.mrletsplay.srweb.command.impl;

import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.srweb.command.SRCommand;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.bot.BotPlayer;

public class CommandAddBot extends SRCommand {
	
	public CommandAddBot() {
		super("addbot");
		setDescription("Add a bot to the game");
		setUsage("/addbot");
		setAllowPregame(true);
	}

	@Override
	public void invoke(Player sender, CommandInvokedEvent event) {
		if(event.getArguments().length != 0) {
			sendCommandInfo(sender);
			return;
		}
		
		BotPlayer bPl = new BotPlayer();
		if(sender.getRoom().isFull()) {
			sender.sendMessage("Can't add bot: Room is full");
			return;
		}
		
		sender.getRoom().addPlayer(bPl);
	}

}
