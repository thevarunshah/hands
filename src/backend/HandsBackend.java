package backend;

import java.util.ArrayList;

import classes.Card;
import classes.Deck;
import classes.Player;

public class HandsBackend {
	
	private static ArrayList<Player> players = new ArrayList<Player>(5);
	private static Deck deck = new Deck();
	
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
	
	private static void printPlayerHand(Player p){
		
		ArrayList<Card> pHand = p.getHand();
		
		System.out.println("Your hand: ");
		for(int j = 0; j < pHand.size(); j++){
			if(j+1 != pHand.size()){
				System.out.print(pHand.get(j) + " | ");
			}
			else{
				System.out.println(pHand.get(j));
			}
		}
	}
	
	public static void setHandEstimations(int roundNum, int dealerIndex, int startIndex){
		
		//ask everyone for hand estimates except dealer
		for(int i = startIndex; i != dealerIndex; i++){
			
			Player p = players.get(i);
			System.out.println("Player: " + p.getName());
			printPlayerHand(p);
			
			System.out.println("How many hands do you think you will be able to make?");
			p.setHandsEstimation(IO.readInt());
			System.out.println();
			
			if(i+1 == players.size()){
				i = -1;
			}
		}
		
		//dealer declares hands
		Player dealer = HandsBackend.getPlayers().get(dealerIndex);
		System.out.println("Player: " + dealer.getName());
		printPlayerHand(dealer);
		
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
	
	public static ArrayList<Player> getPlayers() {
		return players;
	}
	
	public static Deck getDeck() {
		return deck;
	}

}
