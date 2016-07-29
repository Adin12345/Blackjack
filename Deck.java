//Adin I
import java.util.*;

public class Deck {
	private ArrayList <Card> deck;
	
	//constructor; makes a new ArrayList of cards
	public Deck() {
		deck = new ArrayList<Card>();
		reset();
	}
	
	//resets the deck to its default format, with all cards in order
	public void reset() {
		deck.clear();
		
		for (int i = 2; i <= 14; i++) {
			for (int j = 1; j <= 4; j++) {
				Card tempCard = new Card (i, j);
				deck.add(tempCard);
			}
		}
	}
	
	//returns deck in String format; each card is printed on its own line
	public String toString() {
		String toPrint = "";
		for (int i = 0; i < deck.size(); i++) {
			toPrint = toPrint + deck.get(i) + "\n";
		}
		return toPrint;
	}
	
	//returns the card at a given position in the deck
	public Card getPosition(int position) {
		Card card = null;
		for (int i = 0; i < deck.size(); i++) {
			if (i == position) {
				card = deck.get(i);
			}
		}
		return card;
	}
	
	//removes the "top" card from the deck and returns it
	public Card dealCard() {
		Card toDeal = deck.get(0);
		deck.remove(0);
		return toDeal;
	}
	
	//resets and then mixes the deck into a random order
	public void shuffle() {
		ArrayList<Card> temp = new ArrayList<Card>();
		
		for (int i = 0; i < deck.size(); i++) {
				temp.add(deck.get(i));
		}
		deck.clear();
		
		while (temp.size() > 0) {
			int toTake = (int)(Math.random() * temp.size());
			deck.add(temp.get(toTake));
			temp.remove(toTake);
		}
		
		
	}
}
