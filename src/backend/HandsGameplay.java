package backend;

import java.util.ArrayList;

import classes.Card;
import classes.Player;
import classes.Suit;

public class HandsGameplay {

	private static Card[] deckCards;

	public static void main(String[] args) {

		System.out.println("Welcome to Hands!\n");
		deckCards = HandsBackend.getDeck().getCards();

		HandsBackend.createPlayers();

		int roundNum = 1;
		//start game
		while(true){
			
			System.out.println();
			System.out.println("Round " + roundNum);
			HandsBackend.getDeck().shuffleDeck();

			//fresh start
			HandsBackend.newRound();

			//distribute cards and get trump card index
			int trumpIndex = HandsBackend.distributeCards(roundNum, deckCards);

			Suit trumpSuit = deckCards[trumpIndex].getSuit();
			System.out.println("The trump suit for this round is: " + trumpSuit + "\n");

			//set dealer and get the index of the dealer
			int dealerIndex = HandsBackend.setDealer(roundNum);
			int startIndex = HandsBackend.getStartIndex(roundNum, dealerIndex);

			//get hand estimations
			HandsBackend.setHandEstimations(roundNum, dealerIndex, startIndex);
			
			int turnStart = startIndex;
			int turnEnd = dealerIndex;
			
			//play this round
			for(int i = 0; i < 11-roundNum; i++){
				
				ArrayList<Card> pile = new ArrayList<Card>();
				Suit pileSuit = null;
				//everyone plays turn
				for(int j = turnStart; j != turnEnd; j++){

					Player p = HandsBackend.getPlayers().get(j);
					System.out.println("Player: " + p.getName());
					ArrayList<Card> playerHand = p.getHand();

					System.out.println("Your hand: ");
					for(int k = 0; k < playerHand.size(); k++){
						System.out.print(k+1 + ") " + playerHand.get(k) + "  ");
					}
					System.out.println();

					System.out.println("Which card would you like to play? (enter number)");
					if(j == turnStart){
						int cardIndex = IO.readInt();
						while(cardIndex > p.getHand().size()){
							System.out.println("Please reenter a valid value");
							cardIndex = IO.readInt();
						}
						System.out.println();
						pileSuit = p.getHand().get(cardIndex-1).getSuit();
						pile.add(p.getHand().get(cardIndex-1));
						p.getHand().remove(cardIndex-1);
					}
					else{
						System.out.println("Current pile:");
						boolean first = true;
						for(Card c : pile){
							if(first){
								System.out.print(c);
								first = false;
							}
							else{
								System.out.print(" | " + c);
							}
						}
						System.out.println();
						int cardIndex = IO.readInt();
						while(cardIndex > p.getHand().size()){
							System.out.println("Please reenter a valid value");
							cardIndex = IO.readInt();
						}
						//legal card play check
						boolean hasPileSuit = false;
						for(Card c : p.getHand()){
							if(c.getSuit() == pileSuit){
								hasPileSuit = true;
								break;
							}
						}
						while(p.getHand().get(cardIndex-1).getSuit() != pileSuit && hasPileSuit){
							System.out.println("You must play the suit that is the current pile's suit.");
							cardIndex = IO.readInt();
							while(cardIndex > p.getHand().size()){
								System.out.println("Please reenter a valid value");
								cardIndex = IO.readInt();
							}
						}
						System.out.println();
						pile.add(p.getHand().get(cardIndex-1));
						p.getHand().remove(cardIndex-1);
					}

					if(j+1 == HandsBackend.getPlayers().size()){
						j = -1;
					}
				}
				//last player
				Player last = HandsBackend.getPlayers().get(turnEnd);
				System.out.println("Player: " + last.getName());
				ArrayList<Card> playerHand = last.getHand();

				System.out.println("Your hand: ");
				for(int j = 0; j < playerHand.size(); j++){
					System.out.print(j+1 + ") " + playerHand.get(j) + "  ");
				}
				System.out.println();

				System.out.println("Which card would you like to play? (enter number)");
				System.out.println("Current pile:");
				boolean first = true;
				for(Card c : pile){
					if(first){
						System.out.print(c);
						first = false;
					}
					else{
						System.out.print(" | " + c);
					}
				}
				System.out.println();
				int cardIndex = IO.readInt();
				while(cardIndex > last.getHand().size()){
					System.out.println("Please reenter a valid value");
					cardIndex = IO.readInt();
				}
				//legal card play check
				boolean hasPileSuit = false;
				for(Card c : last.getHand()){
					if(c.getSuit() == pileSuit){
						hasPileSuit = true;
						break;
					}
				}
				while(last.getHand().get(cardIndex-1).getSuit() != pileSuit && hasPileSuit){
					System.out.println("You must play the suit that is the current pile's suit.");
					cardIndex = IO.readInt();
					while(cardIndex > last.getHand().size()){
						System.out.println("Please reenter a valid value");
						cardIndex = IO.readInt();
					}
				}
				System.out.println();
				pile.add(last.getHand().get(cardIndex-1));
				last.getHand().remove(cardIndex-1);

				//calculate turn winner
				Card currentHighest = pile.get(0);
				for(Card c : pile){
					if(c.getSuit() == trumpSuit){
						if(currentHighest.getSuit() == trumpSuit){
							if(currentHighest.getValue().isLessThan(c.getValue())){
								currentHighest = c;
							}
						}
						else{
							currentHighest = c;
						}
					}
					else{
						if(c.getSuit() == currentHighest.getSuit()){
							if(currentHighest.getValue().isLessThan(c.getValue())){
								currentHighest = c;
							}
						}
					}
				}
				int turnWinnerIndex = pile.indexOf(currentHighest);
				int playerIndex = turnStart;
				for(int count = 0; count < turnWinnerIndex; count++){
					if(playerIndex+1 == HandsBackend.getPlayers().size()){
						playerIndex = 0;
						continue;
					}
					playerIndex++;
				}
				
				System.out.println(HandsBackend.getPlayers().get(playerIndex).getName() + ", that's your hand!");
				System.out.println();
				HandsBackend.getPlayers().get(playerIndex).setCurrentNumHands(HandsBackend.getPlayers().get(playerIndex).getCurrentNumHands()+1);
				
				//modify who starts next turn
				turnStart = playerIndex;
				turnEnd = playerIndex-1;
				if(playerIndex-1 < 0){
					turnEnd = HandsBackend.getPlayers().size()-1;
				}
			}

			for(Player p : HandsBackend.getPlayers()){
				System.out.println(p.getName() + ": " + p.getCurrentNumHands() + " hands");
			}

			//calculate round winners
			for(Player p : HandsBackend.getPlayers()){
				if(p.getCurrentNumHands() == p.getHandsEstimation()){
					p.setPoints(p.getPoints()+10+p.getCurrentNumHands());
				}
			}

			for(Player p : HandsBackend.getPlayers()){
				System.out.println(p.getName() + ": " + p.getPoints() + " points");
			}
			
			roundNum++;

			//win condition
			boolean someoneWon = false;
			for(Player p : HandsBackend.getPlayers()){
				if(p.getPoints() >= 75){
					System.out.println(p.getName() + ", you won!!");
					someoneWon = true;
				}
			}
			
			if(someoneWon || roundNum == 11){
				break;
			}
		}
	}

}
