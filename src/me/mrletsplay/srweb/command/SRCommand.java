package me.mrletsplay.srweb.command;

import me.mrletsplay.mrcore.command.AbstractCommand;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.srweb.game.Player;

public abstract class SRCommand extends AbstractCommand<SRCommandProperties> {

	public SRCommand(String name) {
		super(name, new SRCommandProperties());
	}
	
	public void setAllowPregame(boolean allowPregame) {
		getProperties().setAllowPregame(allowPregame);
	}
	
	public void setAllowIngame(boolean allowIngame) {
		getProperties().setAllowIngame(allowIngame);
	}
	
	public void setHostOnly(boolean hostOnly) {
		getProperties().setHostOnly(hostOnly);
	}
	
	public void action(CommandInvokedEvent event) {
		Player sender = (Player) event.getSender();
		
		if(sender.getRoom() == null) {
			sender.sendMessage("Can't use command: Not in a room");
			return;
		}
		
		if(getProperties().isHostOnly() && !sender.getID().equals(sender.getRoom().getPlayers().get(0).getID())) {
			sender.sendMessage("Can't use command: Host-only command");
			return;
		}
		
		if(!getProperties().isAllowIngame() && sender.getRoom().isGameRunning()) {
			sender.sendMessage("Can't use command: Not allowed ingame");
			return;
		}
		
		if(!getProperties().isAllowPregame() && !sender.getRoom().isGameRunning()) {
			sender.sendMessage("Can't use command: Not allowed pregame");
			return;
		}
		
		invoke(sender, event);
	}
	
	public abstract void invoke(Player sender, CommandInvokedEvent event);
	
}
