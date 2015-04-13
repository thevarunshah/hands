package backend;

import java.util.ArrayList;

import classes.Card;
import classes.Player;
import classes.Suit;

public class HandsGameplay {

	private static Card[] deckCards;

	public static void main(String[] args) {

		System.out.println("Welcome to Hands!");
		deckCards = HandsBackend.getDeck().getCards();

		HandsBackend.createPlayers();

		int roundNum = 1;
		//start game
		while(true){

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

			//play this round
			for(int i = 0; i < 11-roundNum; i++){
				
				ArrayList<Card> pile = new ArrayList<Card>();
				Suit pileSuit = null;
				//everyone plays turn
				for(int j = startIndex; j != dealerIndex; j++){

					Player p = HandsBackend.getPlayers().get(j);
					System.out.println("Player: " + p.getName());
					ArrayList<Card> playerHand = p.getHand();

					System.out.println("Your hand: ");
					for(int k = 0; k < playerHand.size(); k++){
						System.out.print(k+1 + ") " + playerHand.get(k) + "  ");
					}
					System.out.println("\n");

					System.out.println("Which card would you like to play? (enter number)");
					if(j == startIndex){
						int cardIndex = IO.readInt();
						while(cardIndex > p.getHand().size()){
							System.out.println("Please reenter a valid value");
							cardIndex = IO.readInt();
						}
						pileSuit = p.getHand().get(cardIndex-1).getSuit();
						pile.add(p.getHand().get(cardIndex-1));
						p.getHand().remove(cardIndex-1);
					}
					else{
						System.out.println("Current pile:");
						for(Card c : pile){
							System.out.print(c + ";\t");
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
						pile.add(p.getHand().get(cardIndex-1));
						p.getHand().remove(cardIndex-1);
					}

					if(j+1 == HandsBackend.getPlayers().size()){
						j = -1;
					}
				}
				//dealer plays turn
				Player dealer = HandsBackend.getPlayers().get(dealerIndex);
				System.out.println("Player: " + dealer.getName());
				ArrayList<Card> playerHand = dealer.getHand();

				System.out.println("Your hand: ");
				for(int j = 0; j < playerHand.size(); j++){
					System.out.print(j+1 + ") " + playerHand.get(j) + "  ");
				}
				System.out.println("\n");

				System.out.println("Which card would you like to play? (enter number)");
				System.out.println("Current pile:");
				for(Card c : pile){
					System.out.print(c + ";\t");
				}
				System.out.println();
				int cardIndex = IO.readInt();
				while(cardIndex > dealer.getHand().size()){
					System.out.println("Please reenter a valid value");
					cardIndex = IO.readInt();
				}
				//legal card play check
				boolean hasPileSuit = false;
				for(Card c : dealer.getHand()){
					if(c.getSuit() == pileSuit){
						hasPileSuit = true;
						break;
					}
				}
				while(dealer.getHand().get(cardIndex-1).getSuit() != pileSuit && hasPileSuit){
					System.out.println("You must play the suit that is the current pile's suit.");
					cardIndex = IO.readInt();
					while(cardIndex > dealer.getHand().size()){
						System.out.println("Please reenter a valid value");
						cardIndex = IO.readInt();
					}
				}
				pile.add(dealer.getHand().get(cardIndex-1));
				dealer.getHand().remove(cardIndex-1);

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
				int playerIndex = startIndex;
				for(int count = 0; count < turnWinnerIndex; count++){
					if(playerIndex+1 == HandsBackend.getPlayers().size()){
						playerIndex = 0;
						continue;
					}
					playerIndex++;
				}
				System.out.println(HandsBackend.getPlayers().get(playerIndex).getName() + ", you won this turn!");
				HandsBackend.getPlayers().get(playerIndex).setCurrentNumHands(HandsBackend.getPlayers().get(playerIndex).getCurrentNumHands()+1);
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

			//win condition
			boolean exit = false;
			for(Player p : HandsBackend.getPlayers()){
				if(p.getPoints() >= 75){
					System.out.println(p.getName() + ", you won!!");
					exit = true;
				}
			}
			if(exit){
				break;
			}

			roundNum++;
			if(roundNum == 11){
				break;
			}
		}
	}

}
