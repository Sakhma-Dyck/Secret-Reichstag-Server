package me.mrletsplay.srweb.command;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.command.parser.CommandParser;
import me.mrletsplay.mrcore.command.provider.CommandProvider;
import me.mrletsplay.srweb.command.impl.CommandAddBot;
import me.mrletsplay.srweb.command.impl.CommandHelp;
import me.mrletsplay.srweb.command.impl.CommandKick;

public class SRCommandProvider implements CommandProvider {
	
	public static final SRCommandProvider INSTANCE = new SRCommandProvider();
	
	static {
		INSTANCE.addCommand(new CommandAddBot());
		INSTANCE.addCommand(new CommandKick());
		INSTANCE.addCommand(new CommandHelp());
	}
	
	private List<SRCommand> commands;
	private CommandParser parser;
	
	public SRCommandProvider() {
		this.commands = new ArrayList<>();
		this.parser = new CommandParser(this);
	}

	@Override
	public List<SRCommand> getCommands() {
		return commands;
	}
	
	public void addCommand(SRCommand command) {
		commands.add(command);
	}

	@Override
	public CommandParser getCommandParser() {
		return parser;
	}

}
