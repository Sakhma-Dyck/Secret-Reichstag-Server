package me.mrletsplay.srweb.game.bot;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.java_websocket.WebSocket;

import me.mrletsplay.srweb.SRWebMain;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.bot.strategy.AbstractStrategy;
import me.mrletsplay.srweb.game.bot.strategy.RandomStrategy;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.game.state.board.action.ActionBlockPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionExamineTopCards;
import me.mrletsplay.srweb.game.state.board.action.ActionExamineTopCardsOther;
import me.mrletsplay.srweb.game.state.board.action.ActionInspectPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionInspectPlayerResult;
import me.mrletsplay.srweb.game.state.board.action.ActionKillPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionPickPresident;
import me.mrletsplay.srweb.game.state.board.action.GameActionData;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.impl.PacketClientChatMessage;
import me.mrletsplay.srweb.packet.impl.PacketClientDiscardCard;
import me.mrletsplay.srweb.packet.impl.PacketClientDrawCards;
import me.mrletsplay.srweb.packet.impl.PacketClientPerformAction;
import me.mrletsplay.srweb.packet.impl.PacketClientSelectChancellor;
import me.mrletsplay.srweb.packet.impl.PacketClientVeto;
import me.mrletsplay.srweb.packet.impl.PacketClientVote;
import me.mrletsplay.srweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerAction;
import me.mrletsplay.srweb.packet.impl.PacketServerUpdateGameState;
import me.mrletsplay.srweb.packet.impl.PacketServerVeto;

public class BotPlayer extends Player {
	
	private AbstractStrategy strategy;
	
	public BotPlayer() {
		super(null, "Bot-" + new Random().nextInt(10000));
		this.strategy = new RandomStrategy(this);
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void setWebSocket(WebSocket webSocket) {
		throw new UnsupportedOperationException();
	}
	
	public void chat(String message) {
		SRWebMain.handlePacket(this, new Packet(new PacketClientChatMessage(message)));
	}
	
	@Override
	public void send(Packet packet) {
		PacketData d = packet.getData();
		if(d instanceof PacketServerUpdateGameState) {
			GameState st = ((PacketServerUpdateGameState) d).getNewState();
			if(st.getMoveState() == GameMoveState.SELECT_CHANCELLOR
					&& st.getPresident().getID().equals(getID())) {
				List<Player> pls = getRoom().getPlayers().stream()
						.filter(p -> !p.getID().equals(getID())
								&& !(st.getPreviousChancellor() != null && p.getID().equals(st.getPreviousChancellor().getID()))
								&& !(st.getPreviousPresident() != null && p.getID().equals(st.getPreviousPresident().getID()))
								&& !(st.getBlockedPlayer() != null && p.getID().equals(st.getBlockedPlayer().getID()))
								&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
						.collect(Collectors.toList());
				SRWebMain.handlePacket(this, new Packet(new PacketClientSelectChancellor(strategy.selectChancellor(st, pls))));
			}else if(st.getMoveState() == GameMoveState.VOTE) {
				SRWebMain.handlePacket(this, new Packet(new PacketClientVote(strategy.vote(st))));
			}else if(st.getMoveState() == GameMoveState.DRAW_CARDS
					&& st.getPresident().getID().equals(getID())) {
				SRWebMain.handlePacket(this, new Packet(new PacketClientDrawCards()));
			}
		}else if(d instanceof PacketServerPickCards) {
			SRWebMain.handlePacket(this, new Packet(new PacketClientDiscardCard(strategy.pickCard(getRoom().getGameState(), getHand()))));
		}else if(d instanceof PacketServerPlayerAction) {
			PacketServerPlayerAction ac = (PacketServerPlayerAction) d;
			switch(ac.getAction()) {
				case BLOCK_PLAYER:
				{
					GameState st = getRoom().getGameState();
					List<Player> pls = getRoom().getPlayers().stream()
							.filter(p -> !p.getID().equals(getID())
									&& !p.getID().equals(st.getChancellor().getID())
									&& !p.getID().equals(st.getPresident().getID())
									&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
							.collect(Collectors.toList());
					GameActionData ad = new ActionBlockPlayer(strategy.actionBlockPlayer(st, pls));
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction(ad)));
					break;
				}
				case EXAMINE_TOP_CARDS:
					ActionExamineTopCards a = (ActionExamineTopCards) ac.getData();
					strategy.actionExamineTopCards(getRoom().getGameState(), a.getCards());
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction()));
					break;
				case EXAMINE_TOP_CARDS_OTHER:
				{
					GameState st = getRoom().getGameState();
					List<Player> pls = getRoom().getPlayers().stream()
							.filter(p -> !p.getID().equals(getID())
									&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
							.collect(Collectors.toList());
					GameActionData ad = new ActionExamineTopCardsOther(strategy.actionExamineTopCardsOther(st, pls));
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction(ad)));
					break;
				}
				case INSPECT_PLAYER:
				{
					GameState st = getRoom().getGameState();
					List<Player> pls = getRoom().getPlayers().stream()
							.filter(p -> !p.getID().equals(getID())
									&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
							.collect(Collectors.toList());
					GameActionData ad = new ActionInspectPlayer(strategy.actionInspectPlayer(st, pls));
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction(ad)));
					break;
				}
				case KILL_PLAYER:
				{
					GameState st = getRoom().getGameState();
					List<Player> pls = getRoom().getPlayers().stream()
							.filter(p -> !p.getID().equals(getID())
									&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
							.collect(Collectors.toList());
					GameActionData ad = new ActionKillPlayer(strategy.actionKillPlayer(st, pls));
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction(ad)));
					break;
				}
				case PICK_PRESIDENT:
				{
					GameState st = getRoom().getGameState();
					List<Player> pls = getRoom().getPlayers().stream()
							.filter(p -> !p.getID().equals(getID())
									&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
							.collect(Collectors.toList());
					GameActionData ad = new ActionPickPresident(strategy.actionPickPresident(st, pls));
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction(ad)));
					break;
				}
				case WIN:
				default:
					break;
			}
		}else if(d instanceof ActionInspectPlayerResult) {
			strategy.actionInspectPlayerResult(getRoom().getGameState(), ((ActionInspectPlayerResult) d).getParty());
		}else if(d instanceof PacketServerVeto) {
			SRWebMain.handlePacket(this, new Packet(new PacketClientVeto(strategy.onVeto(getRoom().getGameState()))));
		}
	}

}
