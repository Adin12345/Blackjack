//Adin I
import java.util.*;

public class Player {
	private ArrayList <Card> hand;
	private Deck deck;
	private String name;
	private int chips;
	private int insuranceBet;
	private int bet;
	private boolean dealer;
	private boolean hidden;
	private boolean surrender = false;
	private boolean insured = false;
	

	//constructor; creates player using a given deck, a given name, and 
	//or boolean for whether or not it is the dealer
	public Player(Deck deck, String name, boolean dealer) {
		this.deck = deck;
		this.name = name;
		if (dealer == false) {
			this.chips = 10;
		}
		this.dealer = dealer;
		this.hidden = dealer;
		hand = new ArrayList<Card>();
	}

	//ACCESSORS
	
	//returns insuranceBet
	public int getInsurance() {
		return insuranceBet;
	}
	
	//checks if player put insurance
	public boolean checkInsurance() {
		return insured;
	}
	
	//checks if a card should be hidden
	public boolean checkHidden() {
		return hidden;
	}

	//returns the amount bet by the player
	public int getBet() {
		return bet;
	}

	//returns the size of the player's hand
	public int getHandSize() {
		return hand.size();
	}

	//returns the player's name
	public String getName() {
		return name;
	}

	//returns the total value of the player's hand
	public int getHandValue() {
		int total = 0;

		if (this.check21().equals("over")) {
			return 0;
		}
		for (int i = 1; i <= this.hand.size(); i ++) {
			total += getCardValue(i);
		}
		return total;
	}

	//returns the card at a given position in the player's hand
	//position 1 = index 0
	public Card getCard(int position) {
		return (hand.get(position - 1));
	}

	//returns the amount of chips the player has
	public int getChips() {
		return chips;
	}

	//returns the value (1-11) of a card at a position in the player's hand
	//position 1 = index 0
	public int getCardValue(int position) {
		return getCard(position).getNumber();
	}
	
	//returns if the player has surrendered
	public boolean getSurrender() {
		return surrender;
	}


	//OTHER
	
	//sets insuranceBet
	public void setInsurance(int amount) {
		insuranceBet = amount;
	}
	
	//insures/uninsures player
	public void insure(boolean trueFalse) {
		insured = trueFalse;
	}

	//adds a given card to the player's hand
	public void addCard(Card toAdd) {
		hand.add(toAdd);
	}
	
	//changes the value of surrender to the given boolean
	public void surrender(boolean trueFalse) {
			surrender = trueFalse;
	}
	
	//removes the card at the given location
	//location 1 = index 0
	public void removeCard(int location) {
		hand.remove(location - 1);
	}

	//doubles the player's bet, prints relevent double down info, and gives 
	//them the one card they recieve
	public void doubleDown() {
		bet *= 2;
		System.out.println("Your bet doubles to " + getBet() + ".");
		System.out.print(this + " || [hidden]\n");
		hit();
	}

	//deals the player two cards
	public void getDealt() {
		hit();
		hit();
	}

	//removes all cards from the player's hand
	public void clearHand() {
		for (int i = 0; i < hand.size(); i++) {
			hand.remove(i);
			i--;
		}
	}

	//changes the bet of the player to the given number
	public void bet(int toBet) {
		bet = toBet;
	}

	//sets the card in a given location of a player's hand to a given
	//value and suit
	public void setCard(int location, int value, int suit) {
		Card newCard = new Card(value, suit);
		hand.set(location - 1, newCard);
	}

	//sets the card in a given location of a player's hand to a given card
	public void setCard(int location, Card newCard) {
		hand.set(location - 1, newCard);
	}

	//takes the amount bet out of the player's chips
	public void loseBet() {
		chips -= bet;
	}

	//takes a given amount of chips from the player's chips
	public void loseBet(int amount) {
		chips -= amount;
	}
	
	//adds the amount bet to the player's chips
	public void winBet() {
		chips += bet;
	}
	
	//adds a given amount of chips to the player's chips
	public void winBet(int amount) {
		chips += amount;
	}

	//deals a single card to the player
	public void hit() {
		hand.add(deck.dealCard());
	}

	//changes value of hidden to false
	public void reveal() {
		hidden = false;
	}

	//changes value of hidden to true
	public void hide() {
		hidden = true;
	}

	//adds 1.5x the player's bet to their chips
	public void blackjack() {
		winBet();
		chips += (bet / 2);
	}

	//checks whether the player is over, under, or equal to 21, 
	//or has a blackjack. 
	//Returns "over", "blackjack", "21", or "under".
	public String check21() {
		int total = 0;

		for (int i = 0; i < hand.size(); i++) {
			total += hand.get(i).getNumber();
		}

		if (total > 21) {
			return "over";
		} else if (total == 21 && hand.size() == 2) {
			return "blackjack";
		} else if (total == 21){
			return "21";
		} else {
			return "under";
		}
	}

	//changes the value of every ace in the player's hand from 11 to 1
	public void switchAce() {
		for (int i = 0; i < hand.size(); i++) {
			if (getCardValue(i+1) == 11) {
				hand.get(i).switchAce();
			}
		}
	}

	//prints the player's hand with the format: 
	//Name: Ace of Hearts || 8 of clubs
	public String toString() {
		String handCards = "";

		for (int i = 0; i < hand.size(); i++) {
			if (hidden == true && i == 0) {
				handCards = handCards + "[hidden]";
			} else {
				handCards = handCards + hand.get(i);
			}
			if (i != hand.size() - 1) {
				handCards = handCards + " || ";
			}
		}

		if (dealer == true) {
			return (name + ": " + handCards);
		} else {
			return (name + "(" + chips + "): " + handCards);
		}
	}

	//compares the player's hand to that of the dealer.
	//returns 3 if they're equal, 2 if the dealer is higher,
	//or 1 if the player is higher
	public int compare(Player dealer) {
		if (this.getHandValue() == dealer.getHandValue()) {
			return 3;
		} else if (this.getHandValue() > dealer.getHandValue()) {
			return 1;
		} else {
			return 2;
		}
	}
}
