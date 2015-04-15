package backend;

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
				
				//play turn and return the person's index who won this turn
				int playerIndex = HandsBackend.playTurn(turnStart, turnEnd, trumpSuit); 
				
				//modify who starts next turn
				turnStart = playerIndex;
				turnEnd = playerIndex-1;
				if(playerIndex-1 < 0){
					turnEnd = HandsBackend.getPlayers().size()-1;
				}
			}
			
			System.out.println("Number of hands this round:");
			for(Player p : HandsBackend.getPlayers()){
				System.out.println(p.getName() + ": " + p.getCurrentNumHands() + " hands");
			}

			HandsBackend.calculateRoundWinners();
			System.out.println();
			
			System.out.println("Points per player:");
			for(Player p : HandsBackend.getPlayers()){
				System.out.println(p.getName() + ": " + p.getPoints() + " points");
			}
			System.out.println("----------");
			
			roundNum++;

			//win condition
			boolean someoneWon = HandsBackend.winCondition();
			
			if(someoneWon || roundNum == 11){
				break;
			}
		}
		
		System.out.println("----------");
		System.out.println("Thank you for playing!");
	}
}
