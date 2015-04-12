package backend;

import java.util.ArrayList;

import classes.Card;
import classes.Deck;
import classes.Player;
import classes.Suit;

public class HandsGameplay {
	
	private static ArrayList<Player> players = new ArrayList<Player>(5);
	private static Deck deck = new Deck();

	public static void main(String[] args) {
		
		Card[] deckCards = deck.getCards();
		
		Player alice = new Player("Alice");
		Player bob = new Player("Bob");
		Player evil = new Player("Evil");
		
		players.add(alice);
		players.add(bob);
		players.add(evil);
		
		int roundNum = 1;
		
		while(true){
			
			deck.shuffleDeck();
			
			System.out.println("Round " + roundNum);
			
			//fresh start
			for(Player p : players){
				p.getHand().clear();
				p.setHandsEstimation(0);
				p.setCurrentNumHands(0);
			}
			
			//distribute cards
			int deckIndex = 0;
			for(int i = 0; i < 11-roundNum; i++){
				for(Player p : players){
					p.getHand().add(deckCards[deckIndex]);
					deckIndex++;
				}
			}
			
			Suit trumpSuit = deckCards[deckIndex].getSuit();
			System.out.println("The trump suit for this round is: " + trumpSuit + "\n");
			
			//set dealer
			int dealerIndex = 0;
			for(int i = 0; i < players.size(); i++){
				Player p = players.get(i);
				if(roundNum == 1){
					players.get(players.size()-1).setDealer(true);
					dealerIndex = players.size()-1;
					break;
				}
				else{
					if(p.isDealer()){
						p.setDealer(false);
						if(i+1 == players.size()){
							players.get(0).setDealer(true);
							dealerIndex = 0;
						}
						else{
							players.get(i+1).setDealer(true);
							dealerIndex = i+1;
						}
						break;
					}
				}
			}
			
			//start index
			int startIndex = 0;
			if(roundNum != 1){
				if(dealerIndex+1 != players.size()){
					startIndex = dealerIndex+1;
				}
			}
			
			//get hand estimations
			for(int i = startIndex; i != dealerIndex; i++){
				
				Player p = players.get(i);
				System.out.println("Player: " + p.getName());
				ArrayList<Card> pHand = p.getHand();
				
				System.out.println("Your hand: ");
				for(int j = 0; j < pHand.size(); j++){
					System.out.print(pHand.get(j) + ";\t");
				}
				System.out.println("\n");
				
				System.out.println("How many hands do you think you will be able to make?");
				p.setHandsEstimation(IO.readInt());
				
				if(i+1 == players.size()){
					i = -1;
				}
			}
			//dealer declares hands
			Player dealer = players.get(dealerIndex);
			System.out.println("Player: " + dealer.getName());
			ArrayList<Card> pHand = dealer.getHand();
			
			System.out.println("Your hand: ");
			for(int j = 0; j < pHand.size(); j++){
				System.out.print(pHand.get(j) + ";\t");
			}
			System.out.println("\n");
			
			int totalHands = 0;
			for(Player tmp : players){
				totalHands += tmp.getHandsEstimation();
			}
			System.out.println("You are the dealer, the total number of hands delared so far is: " + totalHands);
			System.out.println("How many hands do you think you will be able to make?");
			int dealerHands = IO.readInt();
			while(dealerHands+totalHands == 11-roundNum){
				System.out.println("The total number of hands cannot equal the cards in this round, please re-enter");
				dealerHands = IO.readInt();
			}
			dealer.setHandsEstimation(dealerHands);
			
			//play this round
			for(int i = 0; i < 11-roundNum; i++){
				ArrayList<Card> pile = new ArrayList<Card>();
				Suit pileSuit = null;
				//everyone plays turn
				for(int j = startIndex; j != dealerIndex; j++){
					
					Player p = players.get(j);
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
					
					if(j+1 == players.size()){
						j = -1;
					}
				}
				//dealer plays turn
				Player p = players.get(dealerIndex);
				System.out.println("Player: " + p.getName());
				ArrayList<Card> playerHand = p.getHand();
				
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
					if(playerIndex+1 == players.size()){
						playerIndex = 0;
						continue;
					}
					playerIndex++;
				}
				System.out.println(players.get(playerIndex).getName() + ", you won this turn!");
				players.get(playerIndex).setCurrentNumHands(players.get(playerIndex).getCurrentNumHands()+1);
			}
			
			for(Player p : players){
				System.out.println(p.getName() + ": " + p.getCurrentNumHands() + " hands");
			}
			
			//calculate round winners
			for(Player p : players){
				if(p.getCurrentNumHands() == p.getHandsEstimation()){
					p.setPoints(p.getPoints()+10+p.getCurrentNumHands());
				}
			}
			
			for(Player p : players){
				System.out.println(p.getName() + ": " + p.getPoints() + " points");
			}
			
			//win condition
			boolean exit = false;
			for(Player p : players){
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
