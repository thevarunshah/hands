package backend;

import java.util.ArrayList;

import classes.Card;
import classes.Deck;
import classes.Player;
import classes.Suit;

public class HandsBackend {
	
	private static ArrayList<Player> players = new ArrayList<Player>(5);
	private static Deck deck = new Deck();
	
	public static void printRules(){
		
		System.out.println("Welcome to Hands!\n");
		System.out.println("To look at how to play, please look at the attached Hands.txt file.\n");
	}
	
	public static void createPlayers(){
		
		System.out.println("How many players will there be? (2-5)");
		int numPlayers = IO.readInt();
		while(numPlayers < 2 || numPlayers > 5){
			System.out.println("You must have between 2 and 5 players - please reenter the number of players: ");
			numPlayers = IO.readInt();
		}
		
		//get player names
		for(int i = 0; i < numPlayers; i++){
			int playerNum = i+1;
			System.out.println("Enter the name of player " + playerNum + ": ");
			String name = IO.readString();
			Player player = new Player(name);
			players.add(player);
		}
	}
	
	public static void newRound(){
		
		for(Player p : players){
			p.getHand().clear();
			p.setHandsEstimation(0);
			p.setCurrentNumHands(0);
		}
	}
	
	//returns the index of the trump card
	public static int distributeCards(int roundNum, Card[] deckCards){
		
		int deckIndex = 0;
		for(int i = 0; i < 11-roundNum; i++){
			for(Player p : players){
				p.getHand().add(deckCards[deckIndex]);
				deckIndex++;
			}
		}
		
		return deckIndex;
	}
	
	//returns the index of the dealer in the players list
	public static int setDealer(int roundNum){
		
		int dealerIndex = 0;
		for(int i = 0; i < players.size(); i++){
			Player p = players.get(i);
			if(roundNum == 1){
				players.get(players.size()-1).setDealer(true);
				dealerIndex = players.size()-1;
				return dealerIndex;
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
					return dealerIndex;
				}
			}
		}
		
		return dealerIndex;
	}
	
	public static int getStartIndex(int roundNum, int dealerIndex){
		
		int startIndex = 0;
		if(roundNum != 1){
			if(dealerIndex+1 != players.size()){
				startIndex = dealerIndex+1;
			}
		}
		
		return startIndex;
	}
	
	private static void printPlayerHand(Player p, boolean num){
		
		ArrayList<Card> pHand = p.getHand();
		
		System.out.println("Your hand: ");
		if(!num){
			for(int i = 0; i < pHand.size(); i++){
				if(i+1 != pHand.size()){
					System.out.print(pHand.get(i) + " | ");
				}
				else{
					System.out.println(pHand.get(i));
				}
			}
		}
		else{
			for(int i = 0; i < pHand.size(); i++){
				System.out.print(i+1 + ") " + pHand.get(i) + "  ");
			}
			System.out.println();
		}
	}
	
	public static void setHandEstimations(int roundNum, int dealerIndex, int startIndex){
		
		//ask everyone for hand estimates except dealer
		for(int i = startIndex; i != dealerIndex; i++){
			
			Player p = players.get(i);
			System.out.println("Player: " + p.getName());
			printPlayerHand(p, false);
			
			System.out.println("How many hands do you think you will be able to make?");
			p.setHandsEstimation(IO.readInt());
			System.out.println();
			
			if(i+1 == players.size()){
				i = -1;
			}
		}
		
		//dealer declares hands
		Player dealer = players.get(dealerIndex);
		System.out.println("Player: " + dealer.getName());
		printPlayerHand(dealer, false);
		
		//num hands already declared
		int totalHands = 0;
		for(Player p : players){
			totalHands += p.getHandsEstimation();
		}
		
		System.out.println("You are the dealer, the total number of hands delared so far is: " + totalHands);
		System.out.println("How many hands do you think you will be able to make?");
		int dealerHands = IO.readInt();
		while(dealerHands+totalHands == 11-roundNum){
			System.out.println("The total number of hands cannot equal the cards in this round, please reenter");
			dealerHands = IO.readInt();
		}
		dealer.setHandsEstimation(dealerHands);
		System.out.println();
	}
	
	private static int getCardIndex(Player p){
		
		int cardIndex = IO.readInt();
		while(cardIndex > p.getHand().size()){
			System.out.println("Please reenter a valid value");
			cardIndex = IO.readInt();
		}
		
		return cardIndex;
	}
	
	private static void printPile(ArrayList<Card> pile){
		
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
	}
	
	private static void doLegalPlay(Suit pileSuit, Player p, int cardIndex){
		
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
	}
	
	private static int findWinner(ArrayList<Card> pile, Suit trumpSuit, int turnStart){
		
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
			if(playerIndex+1 == players.size()){
				playerIndex = 0;
				continue;
			}
			playerIndex++;
		}
		
		return playerIndex;
	}
	
	public static int playTurn(int turnStart, int turnEnd, Suit trumpSuit){
		
		ArrayList<Card> pile = new ArrayList<Card>();
		Suit pileSuit = null;
		//everyone plays turn
		for(int i = turnStart; i != turnEnd; i++){

			Player p = players.get(i);
			System.out.println("Player: " + p.getName());
			printPlayerHand(p, true);

			System.out.println("Which card would you like to play? (enter number)");
			if(i == turnStart){
				int cardIndex = getCardIndex(p);
				System.out.println();
				
				pileSuit = p.getHand().get(cardIndex-1).getSuit();
				pile.add(p.getHand().get(cardIndex-1));
				p.getHand().remove(cardIndex-1);
			}
			else{
				System.out.println("Current pile:");
				printPile(pile);
				
				int cardIndex = getCardIndex(p);
				
				//legal card play check
				doLegalPlay(pileSuit, p, cardIndex);
				
				pile.add(p.getHand().get(cardIndex-1));
				p.getHand().remove(cardIndex-1);
			}

			if(i+1 == players.size()){
				i = -1;
			}
		}
		//last player
		Player last = players.get(turnEnd);
		System.out.println("Player: " + last.getName());
		printPlayerHand(last, true);

		System.out.println("Which card would you like to play? (enter number)");
		System.out.println("Current pile:");
		printPile(pile);

		int cardIndex = getCardIndex(last);
		
		//legal card play check
		doLegalPlay(pileSuit, last, cardIndex);

		pile.add(last.getHand().get(cardIndex-1));
		last.getHand().remove(cardIndex-1);

		//calculate turn winner
		int playerIndex = findWinner(pile, trumpSuit, turnStart);
		
		System.out.println(players.get(playerIndex).getName() + ", that's your hand!");
		System.out.println();
		players.get(playerIndex).setCurrentNumHands(players.get(playerIndex).getCurrentNumHands()+1);
		
		return playerIndex;
	}
	
	public static void calculateRoundWinners(){
		
		for(Player p : players){
			if(p.getCurrentNumHands() == p.getHandsEstimation()){
				p.setPoints(p.getPoints()+10+p.getCurrentNumHands());
			}
		}
	}
	
	public static boolean winCondition(){
		
		boolean someoneWon = false;
		for(Player p : players){
			if(p.getPoints() >= 75){
				System.out.println(p.getName() + ", you won!!");
				someoneWon = true;
			}
		}
		
		return someoneWon;
	}
	
	public static ArrayList<Player> getPlayers() {
		return players;
	}
	
	public static Deck getDeck() {
		return deck;
	}

}
