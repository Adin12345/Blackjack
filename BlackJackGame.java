//Adin I
import java.util.*;
public class BlackJackGame {

	static int endedMoveCount;

	//Main method from which most code is activated
	public static void main(String[] args) {
		boolean newRound = true;
		boolean skip = false;
		int initialPlayers;
		Deck deck = new Deck();
		deck.shuffle();		

		ArrayList<Player> everyone= new ArrayList<Player>();

		//introduces game and takes input for number of players
		Scanner user = new Scanner(System.in);
		System.out.println("Welcome to blackjack! Up to 4 players can play." +
		"\nEach player gets 10 chips on the house!");
		System.out.print("How many players (not counting dealer; max 4)? ");
		String numPlayers = user.nextLine();
		String[] numbers = {"1", "2", "3", "4"};
		while (checkString(numPlayers, numbers) == false) {
			System.out.print("Please enter a valid number: ");
			numPlayers = user.nextLine();
		}
		System.out.println();

		//asks each player's name, and checks if valid
		for (int i = 1; i <= Integer.parseInt(numPlayers); i++) {
			if (Integer.parseInt(numPlayers) == 1) {
				System.out.print("What is your name? ");
			} else {
				System.out.print("What's player " + i + "'s name? ");
			}
			String name = user.nextLine();
			while (checkName(name) == false) {
				System.out.print("Please enter a valid name: ");
				name = user.nextLine();
			}
			System.out.println();
			if (i == 1) {
				Player one = new Player(deck, name, false);
				everyone.add(one);
			} else if (i == 2) {
				Player two = new Player(deck, name, false);
				everyone.add(two);
			} else if (i == 3) {
				Player three = new Player(deck, name, false);
				everyone.add(three);
			} else {
				Player four = new Player(deck, name, false);
				everyone.add(four);
			}
		}

		//adds dealer
		Player dealer = new Player(deck, "Dealer", true);
		everyone.add(dealer);

		//this while loop repeats until the game is over. One time through
		//is one round.
		while (newRound == true) {

			String input = "";
			for (int i = 0; i < everyone.size(); i++) {
				everyone.get(i).surrender(false);
				everyone.get(i).insure(false);
				everyone.get(i).setInsurance(0);
			}
			System.out.println("The deck is shuffled, and hands are dealt.");
			skip = false;
			dealer.hide();
			deck.reset();
			deck.shuffle();
			dealPlayers(everyone);
			endedMoveCount = 0;
			initialPlayers =  everyone.size() - 1;
			
			//gives the choice to rig the hand of a player
			//rig(everyone, 1);

			//tells each player their chips and asks how much they want to bet.
			//checks if valid number
			for (int i = 0; i < everyone.size() - 1; i++) {
				try {
					Thread.sleep(1000);             
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				System.out.println();
				System.out.println(everyone.get(i).getName() + ", you have " 
						+ everyone.get(i).getChips() + " chips.\n");
				System.out.print("How much would you like to bet? ");
				String toBet = user.nextLine();
				while (checkBet(toBet, everyone.get(i)) == false) {
					System.out.print("Please enter a valid bet: ");
					toBet = user.nextLine();
				}
				everyone.get(i).bet(Integer.parseInt(toBet));
			}

			printPlayers(everyone);

			//if dealer has an ace showing, asks all players if they 
			//want to place insurance
			if (dealer.getCard(2).getNumber() == 11) {
				System.out.println("The dealer has an ace showing.");
				System.out.println();
				if (everyone.size() == 2 && everyone.get(0).getChips() > 
				everyone.get(0).getBet()) {
					System.out.print("Would you like to place " +
					"insurance? (y/n): ");
					String insurance = user.nextLine(); 
					String[] ny = {"n", "y"};
					while (checkString(insurance, ny) == false) {
						System.out.print("Please enter y/n: ");
						insurance = user.nextLine();
					}
					System.out.println();
					if (insurance.equals("y")) {
						everyone.get(0).insure(true);
						System.out.print("How much would you like to bet? " +
						"(1/2 original bet max): ");
						String insuranceBet = user.nextLine();
						while (checkInsuranceBet(insuranceBet, 
								everyone.get(0)) == false) {
							System.out.print("Please enter a valid bet: ");
							insuranceBet = user.nextLine();
						}
						int insuranceChips = Integer.parseInt(insuranceBet);
						if (dealer.check21().equals("blackjack")) {
							dealer.reveal();
							System.out.println(dealer);
							System.out.println("The dealer had a blackjack!");
							System.out.println();
							System.out.println("You win " + insuranceChips * 
									2 + " chips.");
							everyone.get(0).winBet(insuranceChips * 2);
							skip = true;
							endedMoveCount++;
						} else {
							System.out.println("The dealer doesn't have a " +
							"blackjack.");
							System.out.println();
							everyone.get(0).loseBet(insuranceChips);
						}
					}
				} else {
					for (int i = 0; i < everyone.size() - 1; i++) {
						if (everyone.get(i).getChips() > 
						everyone.get(i).getBet()) {
							System.out.print(everyone.get(i).getName() + ", " +
							"would you like to place insurance? (y/n): ");
							String insurance = user.nextLine(); 
							String[] ny = {"n", "y"};
							while (checkString(insurance, ny) == false) {
								System.out.print("Please enter y/n: ");
								insurance = user.nextLine();
							}
							System.out.println();
							if (insurance.equals("y")) {
								everyone.get(i).insure(true);
								System.out.print("How much would you " +
										"like to bet?" +
								" (1/2 original bet max): ");
								String insuranceBet = user.nextLine();
								while (checkInsuranceBet(insuranceBet, 
										everyone.get(0)) == false) {
									System.out.print("Please enter a " +
											"valid bet: ");
									insuranceBet = user.nextLine();
								}
								everyone.get(i).setInsurance
								(Integer.parseInt(insuranceBet));
							} else {
								skip = false;
							}
						}
					}
					if (dealer.check21().equals("blackjack")) {
						dealer.reveal();
						System.out.println(dealer);								
						System.out.println("The dealer has a " +
						"blackjack!");
						for (int i = 0; i < everyone.size() - 1; i++) {
							if (everyone.get(i).checkInsurance() == true) {
								System.out.println(everyone.get(i).getName() + 
										" wins " + everyone.get(i).
										getInsurance() 
										* 2 + " chips.");
								everyone.get(i).winBet(everyone.get(i).
										getInsurance() * 2);
								endedMoveCount++;
							} else {
								everyone.get(i).loseBet();
								endedMoveCount++;
							}
						}

						skip = true;
					} else {
						System.out.println("The dealer doesn't have " +
						"a blackjack.");
						for (int i = 0; i < everyone.size() - 1; i ++) {
							everyone.get(i).loseBet(everyone.get(i).
									getInsurance());
						}
					}
				}
				printPlayers(everyone);
			}

			//checks if dealer has a blackjack. If he does, skips to end
			//and subtracts chips from relevent players
			if (dealer.check21().equals("blackjack") && skip == false) {
				System.out.println("The dealer has a blackjack!");
				skip = true;
				for (int i = 0; i <= everyone.size() - 1; i++) {
					if (!everyone.get(i).check21().equals("blackjack") && 
							everyone.get(i).checkInsurance() == false) {
						everyone.get(i).loseBet();
					}
				}
			}

			//for each player, asks what they want to do from valid options 
			//and executes that code. 
			for (int i = 0; i < everyone.size() - 1; i++) {
				try {
					Thread.sleep(1000);            
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				input = "";
				boolean end = false;
				if (everyone.get(i).check21().equals("blackjack") && 
						skip == false && end == false) {
					System.out.println(everyone.get(i).getName() 
							+ " has a blackjack!");
					everyone.get(i).blackjack();
					end = true;
					endedMoveCount++;
				} else if (everyone.get(i).getHandSize() > 1 && skip == false 
						&& end == false) {
					if (everyone.get(i).getCardValue(1) == 
						everyone.get(i).getCardValue(2) && 
						everyone.get(i).getBet() * 2 <= 
							everyone.get(i).getChips()) {
						if (skip == false && everyone.get(i).getBet() * 2 
								<= everyone.get(i).getChips() 
								&& everyone.get(i).getHandSize() == 2) {
							System.out.print(everyone.get(i).getName() 
									+ ", would you like to hit (h), stay (s), "
									+ "double down (dd), split (sp), or " +
							"surrender (su)? ");
							input = user.nextLine();
							String[] hsddspsu = {"h", "s", "dd", "sp", "su"};
							while (checkString(input, hsddspsu) == false) {
								System.out.print("Please enter hit (h), " +
										"stay (s), double down (dd), " +
								"split (sp), or surrender (su).");
								input = user.nextLine();
							}
							if (input.equals("sp")) {
								Player temp = new Player(deck, "temp", false);
								everyone.add(i+1, temp);
								everyone.get(i+1).bet(everyone.get(i).getBet());
								Card tempCard = everyone.get(i).getCard(2);
								everyone.get(i).removeCard(2);
								everyone.get(i+1).addCard(tempCard);
								i--;
							} else if (input.equals("su")) {
								surrender(everyone.get(i));
								end = true;
								endedMoveCount++;
							} else if (input.equals("h")) {
								hit(user, everyone.get(i));
								end = true;
							} else if (input.equals("s")) {
								end = true;
							} else if (input.equals("dd")) {
								everyone.get(i).doubleDown();
								end = true;
							}
						} else {
							System.out.print(everyone.get(i).getName() +
									", would you like to hit (h), stay (s), " +
							"split (sp), or surrender (su)? ");
							input = user.nextLine();
							String[] hsspsu = {"h", "s", "sp", "su"};
							while (checkString(input, hsspsu) == false) {
								System.out.print("Please enter hit (h), " +
										"stay (s), split (sp), or surrender " +
								"(su).");
								input = user.nextLine();
							}
							if (input.equals("sp")) {
								Player temp = new Player(deck, "temp", false);
								everyone.add(i+1, temp);
								everyone.get(i+1).bet(everyone.get(i).getBet());
								Card tempCard = everyone.get(i).getCard(2);
								everyone.get(i).removeCard(2);
								everyone.get(i+1).addCard(tempCard);
								i--;
							} else if (input.equals("su")) {
								surrender(everyone.get(i));
								end = true;
								endedMoveCount++;
							} else if (input.equals("h")) {
								hit(user, everyone.get(i));
								end = true;
							} else if (input.equals("s")) {
								end = true;
							}
						}
					}
				}
				if (skip == false && end == false && everyone.get(i).getBet() 
						* 2 <= everyone.get(i).getChips() && 
						everyone.get(i).getHandSize() == 2) {
					end = true;
					if (everyone.get(i).getName().equals("temp")) {
						System.out.print(everyone.get(i-1).getName() + 
								", would you like to hit (h), stay (s), " +
						"double down (dd), or surrender (su)? ");
					} else {
						System.out.print(everyone.get(i).getName() + 
								", would you like to hit (h), stay (s), " +
						"double down (dd), or surrender (su)? ");
					}
					input = user.nextLine();
					String[] hsddsu = {"h", "s", "dd", "su"};
					while (checkString(input, hsddsu) == false) {
						System.out.print("Please enter hit (h), stay " +
						"(s), double down (dd), or surrender (su).");
						input = user.nextLine();
					}
					if (input.equals("dd")) {
						everyone.get(i).doubleDown();
					} else if (input.equals("su")) {
						surrender(everyone.get(i));
						end = true;
						endedMoveCount++;
					} else if (input.equals("h")) {
						hit(user, everyone.get(i));
						end = true;
					} else if (input.equals("s")) {
						end = true;
					}
				}
				if (skip == false && end == false && 
						everyone.get(i).getHandSize() == 2) {
					end = true;
					if (everyone.get(i).getName().equals("temp")) {
						System.out.print(everyone.get(i-1).getName() + 
								", would you like to hit (h), stay (s), " +
						"or surrender (su)? ");
					} else {
						System.out.print(everyone.get(i).getName() + 
								", would you like to hit (h), stay (s), " +
						"or surrender (su)? ");
					}
					input = user.nextLine();
					String[] hssu = {"h", "s", "su"};
					while (checkString(input, hssu) == false) {
						System.out.print("Please enter hit (h), stay (s), " +
						"or surrender (su).");
						input = user.nextLine();
					}
					if (input.equals("su")) {
						surrender(everyone.get(i));
						end = true;
						endedMoveCount++;
					} else {
						if (input.equals("h") && !everyone.get(i).check21()
								.equals("over") && !everyone.get(i).check21()
								.equals("blackjack")) {
							hit(user, everyone.get(i));
						}
					}
				}

				if (skip == false && end == false) {
					end = true;
					if (!input.equals("h") && !input.equals("s")) {
						if (everyone.get(i).getName().equals("temp")) {
							System.out.print(everyone.get(i-1).getName() + 
									", would you like to hit (h) or " +
							"stay (s)? ");
						} else {
							System.out.print(everyone.get(i).getName() + 
									", would you like to hit (h) or " +
							"stay (s)? ");
						}
						input = user.nextLine();
					}
					String[] hs = {"h", "s"};
					while (checkString(input, hs) == false) {
						System.out.print("Please enter hit (h) or stay (s).");
						input = user.nextLine();
					}
					if (input.equals("h") && 
							!everyone.get(i).check21().equals("over") && 
							!everyone.get(i).check21().equals("blackjack")) {
						hit(user, everyone.get(i));
					}
				}
				if (!everyone.get(i).check21().equals("over") && 
						!dealer.check21().equals("blackjack"))
					printPlayers(everyone);
			}

			//hits dealer until they either have 17 or over or bust
			while (dealer.getHandValue() <= 16 && 
					!dealer.check21().equals("over") && 
					!dealer.check21().equals("blackjack") && 
					everyone.size() - 1 - endedMoveCount != 0) {
				dealer.hit();
				try {
					Thread.sleep(2000);                 
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				System.out.println("The dealer hits.");
				printPlayers(everyone);
				if (dealer.check21().equals("over")) {
					dealer.switchAce();
					if (dealer.check21().equals("over")) {
						System.out.println("The dealer busted.");
					}
				} else if (dealer.check21().equals("blackjack")) {
					dealer.blackjack();
				}
			}

			//reveals all cards if necessary
			if (everyone.size() - 1 - endedMoveCount != 0) {
				try {
					Thread.sleep(1000);                
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				System.out.println("All cards are revealed.");
				dealer.reveal();
				printPlayers(everyone);
			}

			//compares dealer to each player's hand and adds or removes 
			//chips as necessary
			for (int i = 0; i < everyone.size() - 1; i ++) {
				if (!everyone.get(i).check21().equals("over") && 
						!everyone.get(i).check21().equals("blackjack") && 
						!dealer.check21().equals("blackjack") && 
						everyone.get(i).getSurrender() == false) {
					try {
						Thread.sleep(1000);                 
					} catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					if (dealer.check21().equals("over")) {
						everyone.get(i).winBet();
						if (everyone.get(i).getName().equals("temp")) {
							everyone.get(i-1).winBet();
						}
					} else if (everyone.get(i).getSurrender() == false){
						int result = everyone.get(i).compare(dealer);
						if (result == 1) {
							if (everyone.get(i).getName().equals("temp")) {
								System.out.println(everyone.get(i-1).getName()
										+ " wins his second hand! (+" +
										everyone.get(i).getBet() + ")");
								everyone.get(i-1).winBet();
								everyone.remove(i);
							} else {
								System.out.println(everyone.get(i).getName() + 
										" wins! (+" + everyone.get(i).getBet()
										+ ")");
								everyone.get(i).winBet();
							}
						} else if (result == 2) {
							if (everyone.get(i).getName().equals("temp")) {
								System.out.println("Dealer beats " + 
										everyone.get(i-1).getName() + "'s " +
										"second hand. (-" + 
										everyone.get(i).getBet() + ")");
								everyone.get(i-1).loseBet();
								everyone.remove(i);
							} else {
								System.out.println("Dealer beats " + 
										everyone.get(i).getName() + ". " +
										"(-" + everyone.get(i).
										getBet() + ")");
								everyone.get(i).loseBet();
							}
						} else {
							if (everyone.get(i).getName().equals("temp")) {
								System.out.println(everyone.get(i-1).getName() 
										+ "'s second hand ties the dealer.");
								everyone.remove(i);
							} else {
								System.out.println(everyone.get(i).getName() 
										+ " ties the dealer.");
							}
						}
					}
					System.out.println();
				} 
			}

			//removes players that are out of chips
			for (int i = 0; i < everyone.size() - 1; i++ ) {
				if (everyone.get(i).getChips() <= 0) {
					try {
						Thread.sleep(1000);                 
					} catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					if (everyone.size() > 2) {
						System.out.println(everyone.get(i).getName() + 
						" is out of chips!");
					} else {
						System.out.println("You are out of chips!");
					}
					System.out.println();
					everyone.remove(i);
					i--;
				}
			}

			if (everyone.size() == 1) {
				newRound = false;
			}

			//if players remain, ask if each one would like to leave.
			//if yes, removes them. If no, continues on as normal.
			//If at least one player remains, continues to next round
			if (newRound == true) {
				if (initialPlayers > 1) {
					try {
						Thread.sleep(1000);                
					} catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					System.out.println("Would anyone like to leave?");
					for (int i = 0; i < everyone.size() - 1; i++) {
						System.out.print(everyone.get(i).getName() + "(" + 
								everyone.get(i).getChips() + ")? (y/n): ");
						String leave = user.nextLine(); 
						String[] ny = {"n", "y"};
						while (checkString(leave, ny) == false) {
							System.out.print("Please enter y/n: ");
							leave = user.nextLine();
						}
						if (leave.equals("y")) {
							System.out.println();
							System.out.println(everyone.get(i).getName() + " " +
									"leaves with " + 
									everyone.get(i).getChips() + " chips.");
							everyone.remove(i);
							System.out.println();
							i--;
						} else {
							System.out.println();
							System.out.println(everyone.get(i).getName() + " " +
							"will continue playing.");
							System.out.println();
						}
					}
				} else {
					try {
						Thread.sleep(1000);                
					} catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					System.out.print(everyone.get(0).getName() + 
							", would you like to leave with " + 
							everyone.get(0).getChips() + " chips? (y/n): ");
					String leave = user.nextLine(); 
					String[] ny = {"n", "y"};
					while (checkString(leave, ny) == false) {
						System.out.print("Please enter y/n: ");
						leave = user.nextLine();
					}
					if (leave.equals("y")) {
						System.out.println();
						System.out.println("You leave the table with " + 
								everyone.get(0).getChips() + " chips.");
						everyone.remove(0);
						System.out.println();
					} else {
						System.out.println();
						System.out.println("You choose to stay at the table.");
						System.out.println();
					}
				}
			}

			//if players left, prints which players remain
			if (everyone.size() - 1 != initialPlayers) {
				try {
					Thread.sleep(1000);                
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				if (everyone.size() == 4) {
					System.out.println(everyone.get(0).getName() + ", " + everyone.get(1).getName() + ", and " + everyone.get(2).getName() + " remain.");
				} else if (everyone.size() == 3) {
					System.out.println(everyone.get(0).getName() + " and " + everyone.get(1).getName() + " remain.");
				} else if (everyone.size() == 2 && initialPlayers != 1) {
					System.out.println(everyone.get(0).getName() + " is the only one that remains.");
				} else if (everyone.size() == 1) {
					newRound = false;
				}
			}
		}

		//at end of game, after all players leave or run out of chips
		System.out.println("Thanks for playing!");

	}

	//prints all player's hands
	public static void printPlayers(ArrayList<Player> everyone) {
		String handCards = "";

		System.out.println();
		for (int i = 0; i < everyone.size(); i++) {
			if (everyone.get(i).getName().equals("temp")) {
				for (int j = 0; j < everyone.get(i).getHandSize(); j++) {
					if (everyone.get(i).checkHidden() == true && j == 0) {
						handCards = handCards + "[hidden]";
					} else {
						handCards = handCards + everyone.get(i).getCard(j+1);
					}
					if (j != everyone.get(i).getHandSize() - 1) {
						handCards = handCards + " || ";
					}
				}
				System.out.println(everyone.get(i-1).getName() + " hand 2: " + handCards);
			} else {
				System.out.println(everyone.get(i));
			}
		}
		System.out.println();
	}

	//checks if the number entered to choose the bet amount
	//is valid
	public static boolean checkBet(String input, Player player) {
		if (input.length() < 1) {
			return false;
		}

		for (int i = 1; i <= player.getChips(); i++ ) {
			if (input.equals(i + "")) {
				return true;
			}
		}
		return false;
	}
	public static boolean checkInsuranceBet(String input, Player player) {
		if (input.length() < 1) {
			return false;
		}

		for (int i = 1; i <= player.getBet() / 2; i++ ) {
			if (input.equals(i + "")) {
				return true;
			}
		}
		return false;
	}

	//checks if the string entered to choose each player's
	//name is valid
	public static boolean checkName(String input) {
		if (input.length() < 1) {
			return false;
		}

		if (input.charAt(0) == ' ')
			return false;

		return true;
	}

	//Clears each player's hand and deals them 2 cards
	public static void dealPlayers(ArrayList<Player> everyone) {
		for (int i = 0; i < everyone.size(); i++) {
			everyone.get(i).clearHand();
			everyone.get(i).getDealt();
		}
	}

	//only used for testing; gives certain player a certain combination
	//of cards
	public static void rig(ArrayList<Player> everyone, int player) {
		everyone.get(player).setCard(1, 10, 1);
		everyone.get(player).setCard(2, 14, 1);
	}

	//checks if a String given by the user is equal to any one of several
	//options, given in an array. Used in several places.
	public static boolean checkString(String input, String[] validOptions) {
		if (input.length() < 1) {
			return false;
		}

		for (int i = 0; i < validOptions.length; i++) {
			if (input.equals(validOptions[i])) {
				return true;
			}
		}

		return false;
	}

	//called if the user chooses to hit. Gives them their new card,
	//checks for a bust, and then prompts them to either hit or stay again.
	public static void hit(Scanner user, Player player) {
		String input = "h";
		String handCards = "";

		while (input.equals("h") && !player.check21().equals("over") && !player.check21().equals("blackjack")) {
			player.hit();
			System.out.println();
			System.out.println(player);
			System.out.println();
			if (player.check21().equals("over")) {
				player.switchAce();
				if (player.check21().equals("over")) {
					player.loseBet();
					endedMoveCount++;
					if (player.getName().equals("temp")) {
						System.out.println();
						System.out.println("Hand 2 busted. (-" + player.getBet() + " chips)");
						System.out.println();
					} else {
						System.out.println();
						System.out.println(player.getName() + " busted. (-" + player.getBet() + " chips)");
						System.out.println();
					}
				} else {
					if (player.getName().equals("temp")) {
						for (int j = 0; j < player.getHandSize(); j++) {
							if (player.checkHidden() == true && j == 0) {
								handCards = handCards + "[hidden]";
							} else {
								handCards = handCards + player.getCard(j+1);
							}
							if (j != player.getHandSize() - 1) {
								handCards = handCards + " || ";
							}
						}
						System.out.println("Hand 2: " + handCards);
					} else {
						System.out.print(player.getName() + ", would you like to hit (h) or stay (s)? ");
					}
					input = user.nextLine();
				}
			} else {
				System.out.print(player.getName() + ", would you like to hit (h) or stay (s)? ");
				input = user.nextLine();
			}
		}
	}

	//called if the user chooses to surrender. Makes them lose
	//half of their bet, and changes the player object to have
	//the boolean surrender set to true.
	public static void surrender(Player player) {
		player.loseBet();
		player.winBet(player.getBet() / 2);
		player.surrender(true);
	}
}
