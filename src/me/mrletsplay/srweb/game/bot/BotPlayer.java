package me.mrletsplay.srweb.game.bot;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.java_websocket.WebSocket;

import me.mrletsplay.srweb.SRWebMain;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.game.state.board.action.ActionBlockPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionExamineTopCardsOther;
import me.mrletsplay.srweb.game.state.board.action.ActionInspectPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionKillPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionPickPresident;
import me.mrletsplay.srweb.game.state.board.action.GameActionData;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
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
	
	private final static Random RANDOM = new Random();
	
	public BotPlayer() {
		super(null, "Bot-" + new Random().nextInt(10000));
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void setWebSocket(WebSocket webSocket) {
		throw new UnsupportedOperationException();
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
				SRWebMain.handlePacket(this, new Packet(new PacketClientSelectChancellor(pls.get(RANDOM.nextInt(pls.size())).getID())));
			}else if(st.getMoveState() == GameMoveState.VOTE) {
				boolean voteYes = !(getRoom().getGameState().getPresident() instanceof BotPlayer)
						|| !(getRoom().getGameState().getChancellor() instanceof BotPlayer);
				SRWebMain.handlePacket(this, new Packet(new PacketClientVote(voteYes)));
			}else if(st.getMoveState() == GameMoveState.DRAW_CARDS
					&& st.getPresident().getID().equals(getID())) {
				SRWebMain.handlePacket(this, new Packet(new PacketClientDrawCards()));
			}
		}else if(d instanceof PacketServerPickCards) {
			SRWebMain.handlePacket(this, new Packet(new PacketClientDiscardCard(RANDOM.nextInt(getHand().size()))));
		}else if(d instanceof PacketServerPlayerAction) {
			PacketServerPlayerAction ac = (PacketServerPlayerAction) d;
			switch(ac.getAction()) {
				case BLOCK_PLAYER:
				{
					GameState st = getRoom().getGameState();
					List<Player> pls = getRoom().getPlayers().stream()
							.filter(p -> !p.getID().equals(getID())
									&& !(st.getPreviousChancellor() != null && p.getID().equals(st.getPreviousChancellor().getID()))
									&& !(st.getPreviousPresident() != null && p.getID().equals(st.getPreviousPresident().getID()))
									&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
							.collect(Collectors.toList());
					GameActionData ad = new ActionBlockPlayer(pls.get(RANDOM.nextInt(pls.size())).getID());
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction(ad)));
					break;
				}
				case EXAMINE_TOP_CARDS:
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction()));
					break;
				case EXAMINE_TOP_CARDS_OTHER:
				{
					GameState st = getRoom().getGameState();
					List<Player> pls = getRoom().getPlayers().stream()
							.filter(p -> !p.getID().equals(getID())
									&& !st.getDeadPlayers().stream().anyMatch(dp -> p.getID().equals(dp.getID())))
							.collect(Collectors.toList());
					GameActionData ad = new ActionExamineTopCardsOther(pls.get(RANDOM.nextInt(pls.size())).getID());
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
					GameActionData ad = new ActionInspectPlayer(pls.get(RANDOM.nextInt(pls.size())).getID());
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
					GameActionData ad = new ActionKillPlayer(pls.get(RANDOM.nextInt(pls.size())).getID());
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
					GameActionData ad = new ActionPickPresident(pls.get(RANDOM.nextInt(pls.size())).getID());
					SRWebMain.handlePacket(this, new Packet(new PacketClientPerformAction(ad)));
					break;
				}
				case WIN:
				default:
					break;
			}
		}else if(d instanceof PacketServerVeto) {
			SRWebMain.handlePacket(this, new Packet(new PacketClientVeto(RANDOM.nextBoolean())));
		}
	}

}
