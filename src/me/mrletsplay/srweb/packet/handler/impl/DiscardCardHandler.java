package me.mrletsplay.srweb.packet.handler.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.game.state.board.GameBoardAction;
import me.mrletsplay.srweb.game.state.board.action.ActionExamineTopCards;
import me.mrletsplay.srweb.game.state.board.action.GameActionData;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.PacketData;
import me.mrletsplay.srweb.packet.handler.SingleTypePacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientDiscardCard;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;
import me.mrletsplay.srweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerAction;

public class DiscardCardHandler extends SingleTypePacketHandler<PacketClientDiscardCard> {

	public DiscardCardHandler() {
		super(PacketClientDiscardCard.class);
	}
	
	@Override
	public PacketData handleSingle(Player player, Packet packet, PacketClientDiscardCard data) {
		Room r = player.getRoom();
		GameState s = r.getGameState();
		if(s.getPresident().equals(player) && s.getMoveState().equals(GameMoveState.DISCARD_PRESIDENT)) {
			if(data.getDiscardIndex() < 0 || data.getDiscardIndex() > 2) return PacketServerNoData.INSTANCE;
			GamePolicyCard c = player.getHand().remove(data.getDiscardIndex());
			s.getDiscardPile().add(0, c); // Put discarded card on top of the disc. pile
			s.setMoveState(GameMoveState.DISCARD_CHANCELLOR);
			
			List<GamePolicyCard> rCards = s.getPresident().getHand();
			s.getChancellor().setHand(rCards); // Pass remaining cards to chancellor
			s.getPresident().setHand(Collections.emptyList());
			
			r.broadcastStateUpdate();
			s.getChancellor().send(new Packet(new PacketServerPickCards(rCards, false)));
		}else if(s.getChancellor().equals(player) && s.getMoveState().equals(GameMoveState.DISCARD_CHANCELLOR)) {
			if(data.getDiscardIndex() < 0 || data.getDiscardIndex() > 1) return PacketServerNoData.INSTANCE;
			GamePolicyCard c = player.getHand().remove(data.getDiscardIndex());
			s.getDiscardPile().add(0, c); // Put discarded card on top of the disc. pile
			
			GameBoardAction ac = null;
			switch(player.getHand().get(0)) {
				case COMMUNIST:
					ac = s.getCommunistBoard().addCard();
					break;
				case FASCIST:
					ac = s.getFascistBoard().addCard();
					break;
				case LIBERAL:
					ac = s.getLiberalBoard().addCard();
					break;
			}
			
			if(s.getFascistBoard().getNumCards() == 5 || s.getCommunistBoard().getNumCards() == 5) {
				s.setVetoPowerUnlocked(true);
			}
			
			if(ac == null) {
				s.advanceRound();
			}else {
				if(ac.equals(GameBoardAction.WIN)) {
					r.setWinner(player.getHand().get(0).getParty());
					r.stopGame();
					return PacketServerNoData.INSTANCE;
				}
				
				GameActionData aData = null;
				
				if(ac.equals(GameBoardAction.EXAMINE_TOP_CARDS)) {
					aData = new ActionExamineTopCards(new ArrayList<>(s.getDrawPile().subList(0, 3)));
				}
				
				s.setMoveState(GameMoveState.ACTION);
				s.setActionPerformer(s.getPresident());
				s.getActionPerformer().setActionData(aData);
				s.setAction(ac);
				s.getPresident().send(new Packet(new PacketServerPlayerAction(ac, aData)));
				
				r.broadcastEventLogEntry(String.format(ac.getEventLogMessage(), s.getActionPerformer().getName()));
			}
			
			player.setHand(Collections.emptyList());
			
			r.broadcastStateUpdate();
		}
		return PacketServerNoData.INSTANCE;
	}

}
