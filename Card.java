//Adin I
public class Card {
	private String value;
	private String suit;
	private int number;
	private int aceNumber = 11;
	
	//constructor; takes in number (2-14) for number and number(1-4) for suit
	public Card(int number, int suit) {
		this.number = number;
		
		if (number == 11) {
			this.value = "Jack";
		} else if (number == 12) {
			this.value = "Queen";
		} else if (number == 13) {
			this.value = "King";
		} else if (number == 14) {
			this.value = "Ace";
		} else {
			this.value = number + "";
		}
		
		if (suit == 1) {
			this.suit = "Clubs";
		} else if (suit == 2) {
			this.suit = "Diamonds";
		} else if (suit == 3) {
			this.suit = "Hearts";
		} else {
			this.suit = "Spades";
		}
	}
	
	//returns the the cards value when playing blackjack (for example, a queen 
	//returns 10, not 12)
	public int getNumber() {
		if (value.equals("Jack") || value.equals("Queen") ||
				value.equals("King")) {
			return 10;
		} else if (value.equals("Ace")) {
			return aceNumber;
		} else {
			return number;
		}
	}
	
	//returns the card with the format: 9 of Spades
	public String toString() {
		return (value + " of " + suit);
	}
	
	//returns the card's suit
	public String getSuit() {
		return suit;
	}
	
	//returns the card's number (for exampe, a queen returns 12, not 10)
	public String getValue() {
		return value;
	}
	
	//if the card is an ace, switches its value from either
	//1 or 11 to the opposite one
	public void switchAce() {
		if (getNumber() == 11) {
			aceNumber = 1;
		} else if (getNumber() == 1) {
			aceNumber = 11;
		}
	}
}
