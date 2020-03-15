package me.mrletsplay.ssweb.packet.handler.impl;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.game.state.GameMoveState;
import me.mrletsplay.ssweb.game.state.GameParty;
import me.mrletsplay.ssweb.game.state.GameState;
import me.mrletsplay.ssweb.game.state.board.GameBoardAction;
import me.mrletsplay.ssweb.game.state.board.action.ActionBlockPlayer;
import me.mrletsplay.ssweb.game.state.board.action.ActionExamineTopCards;
import me.mrletsplay.ssweb.game.state.board.action.ActionExamineTopCardsOther;
import me.mrletsplay.ssweb.game.state.board.action.ActionInspectPlayer;
import me.mrletsplay.ssweb.game.state.board.action.ActionInspectPlayerResult;
import me.mrletsplay.ssweb.game.state.board.action.ActionKillPlayer;
import me.mrletsplay.ssweb.game.state.board.action.ActionPickPresident;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientPerformAction;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;
import me.mrletsplay.ssweb.packet.impl.PacketServerPlayerAction;

public class PerformActionHandler extends SingleTypePacketHandler<PacketClientPerformAction> {

	public PerformActionHandler() {
		super(PacketClientPerformAction.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientPerformAction data) {
		Room r = player.getRoom();
		GameState s = r.getGameState();
		
		if(s.getMoveState().equals(GameMoveState.ACTION) && s.getActionPerformer().equals(player)) {
			switch(s.getAction()) {
				case KILL_PLAYER:
				{
					ActionKillPlayer a = (ActionKillPlayer) data.getData();
					Player p = r.getPlayer(a.getPlayerID());
					if(p == null || s.isPlayerDead(p) || player.equals(p)) return PacketServerNoData.INSTANCE;
					s.addDeadPlayer(p);
					
					r.broadcastEventLogEntry(player.getName() + " kills " + p.getName());
					
					if((s.getHitler() == null || s.isPlayerDead(s.getHitler()))
							&& (s.getStalin() == null || s.isPlayerDead(s.getStalin()))) {
						r.setWinner(GameParty.LIBERAL);
						r.stopGame();
						return PacketServerNoData.INSTANCE;
					}
					
					if(!p.equals(s.getHitler())) {
						s.addNotHitlerConfirmed(p);
					}
					
					if(!p.equals(s.getStalin())) {
						s.addNotStalinConfirmed(p);
					}
					
					break;
				}
				case BLOCK_PLAYER:
				{
					ActionBlockPlayer a = (ActionBlockPlayer) data.getData();
					Player p = r.getPlayer(a.getPlayerID());
					if(p == null || s.isPlayerDead(p) || player.equals(p) || p.equals(s.getChancellor())
							|| (p.equals(s.getPresident()) && r.getPlayers().size() >= 8)) return PacketServerNoData.INSTANCE;

					s.setBlockedPlayer(p);
					s.advanceRound();
					s.setBlockedPlayer(p);
					r.broadcastStateUpdate();
					
					r.broadcastEventLogEntry(player.getName() + " blocks " + p.getName());
					
					return PacketServerNoData.INSTANCE;
				}
				case INSPECT_PLAYER:
				{
					ActionInspectPlayer a = (ActionInspectPlayer) data.getData();
					Player p = r.getPlayer(a.getPlayerID());
					if(p == null || s.isPlayerDead(p) || player.equals(p)) return PacketServerNoData.INSTANCE;
					
					s.advanceRound();
					r.broadcastStateUpdate();
					
					r.broadcastEventLogEntry(player.getName() + " inspects " + p.getName());
					
					return new ActionInspectPlayerResult(s.getRole(p).getParty());
				}
				case EXAMINE_TOP_CARDS:
					break;
				case EXAMINE_TOP_CARDS_OTHER:
				{
					ActionExamineTopCardsOther a = (ActionExamineTopCardsOther) data.getData();
					Player p = r.getPlayer(a.getPlayerID());
					if(p == null || s.isPlayerDead(p) || player.equals(p)) return PacketServerNoData.INSTANCE;
					
					s.setActionPerformer(p);
					s.setAction(GameBoardAction.EXAMINE_TOP_CARDS);
					p.send(new Packet(new PacketServerPlayerAction(GameBoardAction.EXAMINE_TOP_CARDS, new ActionExamineTopCards(s.getDrawPile().subList(0, 3)))));
					
					r.broadcastEventLogEntry(player.getName() + " picked " + p.getName() + " to inspect the top three cards");

					return PacketServerNoData.INSTANCE;
				}
				case PICK_PRESIDENT:
				{
					ActionPickPresident a = (ActionPickPresident) data.getData();
					Player p = r.getPlayer(a.getPlayerID());
					if(p == null || s.isPlayerDead(p) || player.equals(p)) return PacketServerNoData.INSTANCE;
					
					s.advanceRound(false);
					s.setPresident(p);
					r.broadcastStateUpdate();
					
					r.broadcastEventLogEntry(player.getName() + " picked " + p.getName() + " to be the next president");
					
					return PacketServerNoData.INSTANCE;
				}
				default:
					break;
					
			}

			s.advanceRound();
			r.broadcastStateUpdate();
		}
		
		return PacketServerNoData.INSTANCE;
	}
	
}
