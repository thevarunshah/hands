package classes;

public enum Value {
	
	TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
	
	public String toString(){
		switch(this){
			case TWO: return "Two";
			case THREE: return "Three";
			case FOUR: return "Four";
			case FIVE: return "Five";
			case SIX: return "Six";
			case SEVEN: return "Seven";
			case EIGHT: return "Eight";
			case NINE: return "Nine";
			case TEN: return "Ten";
			case JACK: return "Jack";
			case QUEEN: return "Queen";
			case KING: return "King";
			case ACE: return "Ace";
			default: return "";
		}
	}
	
	private int getIntValue(){
		switch(this){
			case TWO: return 2;
			case THREE: return 3;
			case FOUR: return 4;
			case FIVE: return 5;
			case SIX: return 6;
			case SEVEN: return 7;
			case EIGHT: return 8;
			case NINE: return 9;
			case TEN: return 10;
			case JACK: return 11;
			case QUEEN: return 12;
			case KING: return 13;
			case ACE: return 14;
			default: return -1;
		}
	}
	
	public boolean isLessThan(Value that){
		int thisValue = this.getIntValue();
		int thatValue = that.getIntValue();
		return thisValue < thatValue;
	}

}
