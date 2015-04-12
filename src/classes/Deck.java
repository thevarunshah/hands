package classes;

import java.util.Random;


public class Deck {
	
	private Card[] cards;

	public Deck(){
		this.cards = new Card[52];
		createDeck();
	}
	
	private void createDeck(){
		
		int index = 0;
		for(int i = 0; i < 4; i++){
			Suit currentSuit = null;
			switch(i){
				case 0: currentSuit = Suit.DIAMOND; break;
				case 1: currentSuit = Suit.SPADE; break;
				case 2: currentSuit = Suit.HEART; break;
				case 3: currentSuit = Suit.CLUB; break;
			}
			for(int j = 0; j < 13; j++){
				Value currentValue = null;
				switch(j){
					case 0: currentValue = Value.TWO; break;
					case 1: currentValue = Value.THREE; break;
					case 2: currentValue = Value.FOUR; break;
					case 3: currentValue = Value.FIVE; break;
					case 4: currentValue = Value.SIX; break;
					case 5: currentValue = Value.SEVEN; break;
					case 6: currentValue = Value.EIGHT; break;
					case 7: currentValue = Value.NINE; break;
					case 8: currentValue = Value.TEN; break;
					case 9: currentValue = Value.JACK; break;
					case 10: currentValue = Value.QUEEN; break;
					case 11: currentValue = Value.KING; break;
					case 12: currentValue = Value.ACE; break;
				}
				Card currentCard = new Card(currentSuit, currentValue);
				this.cards[index] = currentCard;
				index++;
			}
		}
	}
	
	public void shuffleDeck(){
		
		Random rand = new Random();
		
		for(int i = 0; i < 52; i++){
			Card currentCard = this.cards[i];
			int random = rand.nextInt(52);
			this.cards[i] = this.cards[random];
			this.cards[random] = currentCard;
		}
	}
	
	public Card[] getCards() {
		return cards;
	}

}
