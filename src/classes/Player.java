package classes;

import java.util.ArrayList;

public class Player {
	
	private String name;
	private ArrayList<Card> hand;
	private int points;
	private int handsEstimation;
	private int currentNumHands;
	private boolean dealer;
	
	public Player(String name){
		this.name = name;
		this.hand = new ArrayList<Card>(10);
		this.points = 0;
		this.handsEstimation = 0;
		this.currentNumHands = 0;
		this.setDealer(false);
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public int getHandsEstimation() {
		return handsEstimation;
	}

	public void setHandsEstimation(int handsEstimation) {
		this.handsEstimation = handsEstimation;
	}

	public int getCurrentNumHands() {
		return currentNumHands;
	}

	public void setCurrentNumHands(int currentNumHands) {
		this.currentNumHands = currentNumHands;
	}

	public boolean isDealer() {
		return dealer;
	}

	public void setDealer(boolean dealer) {
		this.dealer = dealer;
	}

}
