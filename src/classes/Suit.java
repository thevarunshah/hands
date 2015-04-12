package classes;

public enum Suit {
	
	DIAMOND, SPADE, HEART, CLUB;
	
	public String toString(){
		switch(this){
			case DIAMOND: return "Diamond";
			case SPADE: return "Spade";
			case HEART: return "Heart";
			case CLUB: return "Club";
			default: return "";
		}
	}

}
