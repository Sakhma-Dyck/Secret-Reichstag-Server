package me.mrletsplay.srweb.command.impl;

import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.srweb.command.SRCommand;
import me.mrletsplay.srweb.command.SRCommandProvider;
import me.mrletsplay.srweb.game.Player;

public class CommandHelp extends SRCommand {

	public CommandHelp() {
		super("help");
		setDescription("Lists all available commands");
		setUsage("/help");
		setAllowPregame(true);
		setAllowIngame(true);
		setHostOnly(false);
	}

	@Override
	public void invoke(Player sender, CommandInvokedEvent event) {
		sender.sendMessage("Available commands:");
		for(SRCommand cmd : SRCommandProvider.INSTANCE.getCommands()) {
			sender.sendMessage(cmd.getUsage() + " | " + cmd.getDescription());
		}
	}

}
