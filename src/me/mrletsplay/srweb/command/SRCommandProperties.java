package me.mrletsplay.srweb.command;

import me.mrletsplay.mrcore.command.properties.CommandProperties;

public class SRCommandProperties implements CommandProperties {
	
	private boolean allowPregame = false;
	private boolean allowIngame = false;
	private boolean hostOnly = true;
	
	public boolean isAllowPregame() {
		return allowPregame;
	}
	
	public void setAllowPregame(boolean allowPregame) {
		this.allowPregame = allowPregame;
	}
	
	public boolean isAllowIngame() {
		return allowIngame;
	}
	
	public void setAllowIngame(boolean allowIngame) {
		this.allowIngame = allowIngame;
	}
	
	public void setHostOnly(boolean hostOnly) {
		this.hostOnly = hostOnly;
	}
	
	public boolean isHostOnly() {
		return hostOnly;
	}

}
