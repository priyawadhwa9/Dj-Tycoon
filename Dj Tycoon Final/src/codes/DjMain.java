package codes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class DjMain {

	public static final int SIZE = 20;
	//array for music titles, populated from file
	public static String[] music = new String[SIZE];
	//keeps track of players energy
	public static int energy = 100;
	//keeps track of players money
	public static int money = 100;
	//number of records player owns, max 20
	public static int musicCounter = 4;
	//used for money pay out per gig based on equipment
	public static int equiptmentBonus = 0;
	//player location for transport method, start in club
	public static int locationX = 4;
	public static int locationY = 3;
	//keep track of what player owns
	public static boolean table1 = false;
	public static boolean table2 = false;
	public static boolean speaker1 = false;
	public static boolean speaker2 = false;
	//maps to be displayed when transporting, populated from file
	public static String[] map1 = new String[15];
	public static String[] map2 = new String[15];
	public static String[] map3 = new String[15];
	public static String[] map4 = new String[15];
	public static void main(String[] args) throws InterruptedException, IOException {
		populateMusic();
		populateMap();
		boolean playing = true;
		boolean win = false;
		System.out.println("Welcome to Dj Tycoon! "
				+ "\nChoose your difficulty:\n1.Easy\n2.Medium\n3.Hard");
		int level = Utility.errorTrapInt(3, 1);
		if(level == 2)
		{
			money = 75;
			energy = 75;
		}
		else if(level == 3)
		{
			money = 50;
			energy = 75;
		}
		
		while (playing == true) {
			//check if player has won the game
			if(win == false){
				if(table1 == true && table2 == true && speaker1 == true && speaker2 == true && musicCounter == 20)
				{
					System.out.println("\n\n\n\n\n\n\n\n"
							+ "YOU WIN!!! "
							+ "\nYou have become a professional Dj, congrats!"
							+ "\nYou have all of the equiptment and records in the game,"
							+ " but feel free to keep playing");
					System.out.println("\n\n\n\n\n\n\n\n\n");
				win = true;
			}
			//check if player has been offered a gig, 66% chance
			int gigOpportunity = Utility.rng(3, 1);
			if (gigOpportunity == 3 || gigOpportunity == 1) {
				//if player has the right equipment, let them play the gig
				if (gigCheck() == true) {
					gig();
				}
			}
			//players options for each turn
			System.out.println(
					"What would you like to do?\t\tEnergy:" + energy + " Money:" + money
					+ "\n1. Go to the Dj store"
					+ "\n2. Go to the food court" + "\n3. Wander and wait for a gig"
					+ "\n4.Rest (You may get robbed)" + "\n5.Quit");
			int choice = Utility.errorTrapInt(5, 1);
			//if player chooses store, transport to store and then run store method
			if (choice == 1) {
				// location of store: 1,1
				if (locationX != 1 || locationY != 1)
				{
					if(locationX == 4 && locationY == 3)
					{
						transport(1, 1);
						displayMap(4);
					}
					else if(locationX == 1 && locationY == 5)
					{
						transport(1, 1);
						displayMap(2);
					}
				}
				store();
				}
			//if player chooses food court, transport to food court and then run food method
			 else if (choice == 2) {
				
				// location of food court: 1,5
				if (locationX != 1 || locationY != 5)
				{
					
					if(locationX == 1 || locationY == 1){
						transport(1, 5);
						displayMap(2);
					}
					if(locationX == 4 && locationY == 3)
					{
						transport(1, 5);
						displayMap(3);
					}
				}
					food();
			} 
			//let player "wander", unless they do not have enough energy
			 else if (choice == 3) {
				//make player rest if they don't have enough energy
				if(energy < 5)
				{
					System.out.println("You must rest, you don't have enough energy");
					rest();
				}
				System.out.println("Wandering...");
				TimeUnit.SECONDS.sleep(2);
				energy = energy - 5;
				//33% chance player gets to play dice game while wandering 
				int gameOpportunity = Utility.rng(3, 1);
				if(gameOpportunity == 3)
					dice();
				} 
			 else if (choice == 4)
				 rest();
			 else
				//if player chooses to quit
				playing = false;
				//make player rest if they have run out of energy
			if(energy == 0)
			{
					System.out.println("You must rest, you don't have enough energy");
					rest();
				}
			}
		}
		System.out.println("Thank you for playing! Goodbye");
	}
	//method for gambling mini game
	public static void dice() throws InterruptedException
	{
		if(money > 0)
		{
			//menu
			System.out.println("You have encountered a street game! \nRules: "
					+ "\nYou must place a bet on a number and roll two dice. Payout is based on probabliity"
					+ "\n2,12 pay out: 4x bet"
					+ "\n3,11 pay out: 3x bet"
					+ "\n4,10 pay out: 2.5x bet"
					+ "\n5,9 pay out: 2x bet"
					+ "\n6,8 pay out: 1.5x bet"
					+ "\n7 pay out: 1x bet");
			// bet input
			System.out.println("What number would you like to bet on?");
			int betNumber = Utility.errorTrapInt(12, 2);
			System.out.println("How much would you like to bet?");
			int bet = Utility.errorTrapInt(money, 1);
			System.out.println("Good luck! \nRolling...");
			TimeUnit.SECONDS.sleep(2);
			//generate dice
			int sum = Utility.rng(6, 1) + Utility.rng(6, 1);
			System.out.println("You rolled " + sum);
			// track payout
			int payout = -bet;
			// add to payout if they won
			if(sum == betNumber)
			{
				System.out.println("You win!");
				if(betNumber == 2 || betNumber == 12)
					payout = payout + 5*bet;
				if(betNumber == 3 || betNumber == 11)
					payout = payout + 4*bet;
				if(betNumber == 4 || betNumber == 10)
					payout = (int) (payout + 3.5*bet);
				if(betNumber == 5 || betNumber == 9)
					payout = payout + 3*bet;
				if(betNumber == 6 || betNumber == 8)
					payout = (int) (payout + 2.5*bet);
				if(betNumber == 7)
					payout = payout + 2*bet;
			}
			else
				System.out.println("You lost, better luck next time!");
			money = money + payout;
		}
	}
	//method to check if player has the right equipment for a gig
	public static boolean gigCheck() throws InterruptedException 
	{
		//chooses gig equipment requirements
		int rnd = Utility.rng(7, 1);
		boolean gig = false;
		System.out.println("You have been offered a gig! " 
		+ "\nWhat would you like to do?\t\tEnergy:" + energy
		+ " Money:" + money + "\n1. Take it" + "\n2. Reject it");
		int choice = Utility.errorTrapInt(2, 1);
		if (choice == 1) {
			//checks if player has sufficient equipment for the random requirements
			System.out.println("Hold on, let me check your equiptment...");
			TimeUnit.SECONDS.sleep(1);
			if (rnd == 1) {
				gig = true;
			} else if (rnd == 2) {
				if (equiptmentBonus >= 4)
					gig = true;
				else
					System.out.println("You don't have the right equiptment...go to the store!");
			} else if (rnd == 3) {
				if (equiptmentBonus >= 20)
					gig = true;
				else
					System.out.println("You don't have the right equiptment...go to the store!");
			} else if (rnd == 4) {
				if (equiptmentBonus >= 24)
					gig = true;
				else
					System.out.println("You don't have the right equiptment...go to the store!");
			} else if (rnd == 5) {
				if (equiptmentBonus >= 30)
					gig = true;
				else
					System.out.println("You don't have the right equiptment...go to the store!");
			} else if (rnd == 6) {
				if (equiptmentBonus >= 64)
					gig = true;
				else
					System.out.println("You don't have the right equiptment...go to the store!");
			} else if (rnd == 7) {
				if (equiptmentBonus >= 70)
					gig = true;
				else
					System.out.println("You don't have the right equiptment...go to the store!");
			}
		} 
		//if user rejects gig
		else
			System.out.println("Alright, maybe next time!");
		return gig;
	}
	//method for food court
	public static void food() {
		boolean leave = false;
		System.out.println("Welcome to the food court!");
		do {
			// display menu
			System.out.println("\nMenu:\t\t\tEnergy:" + energy + " Money:" + money); 
			System.out.printf("%-30.30s  %-30.30s%n","1. Burrito $10", "2. Pasta $10");
			System.out.printf("%-30.30s  %-30.30s%n","3. Chicken nuggets $5", "4. Fries $5");
			System.out.printf("%-30.30s  %-30.30s%n","5. Ice cream $5" ,"6. Coke $1");
			System.out.printf("%-30.30s  %-30.30s%n","7.Coffee $2", "8. Exit");
			int choice = Utility.errorTrapInt(8, 1);
			if (choice == 1 || choice == 2) 
			{
				// $10 item buy
				if(money > 10)
				{
					money = money - 10;
					energy = energy + 20;
				}
				else
					System.out.println("You don't have enough money for this!");
			} 
			else if (choice == 3 || choice == 4 || choice == 5) 
			{
				// $5 item buy
				if(money > 5)
				{
					money = money - 5;
					energy = energy + 10;
				}
				else
					System.out.println("You don't have enough money for this!");
			} 
			else if (choice == 6) 
			{
				// $1 item buy
				if(money > 1)
				{
					money = money - 1;
					energy = energy + 2;
				}
				else
					System.out.println("You don't have enough money for this!");
			} 
			else if (choice == 7) 
			{
				// $2 item buy
				if(money > 2)
				{
					money = money - 2;
					energy = energy + 8;
				}
				else
					System.out.println("You don't have enough money for this!");
			} else
				leave = true;
		} while (leave == false);
		System.out.println("Thank you for coming");
	}
	//method for resting, restores energy
	public static void rest() throws InterruptedException 
	{
		// 33% chance the player gets robbed
		int robbedCheck = Utility.rng(3, 1);
		//energy reset
		energy = 100;
		System.out.println("Resting....");
		TimeUnit.SECONDS.sleep(3);
		//take away all money and equipment
		if (robbedCheck == 3) 
		{
			System.out.println("You fell asleep while resting and were robbed!!!"
					+ "All of your equiptment and money was stolen!");
			money = 0;
			table1 = false;
			table2 = false;
			speaker1 = false;
			speaker2 = false;
			equiptmentBonus = 0;
		}
	}
	// method for movement from building to building
	public static void transport(int x, int y) throws InterruptedException 
	{
		//empty map
		displayMap(1);
		//determine distance to travel
		int cost = 2 * ((Math.abs(locationX - x)) + (Math.abs(locationY - y)));
		System.out.println("The cost is " + cost);
		//make player rest, insufficient money and energy for travel
		if (cost > energy && cost > money) 
		{
			System.out.println("You don't have enough energy or money for travel, you must rest. "
					+ "\nI hope you dont get robbed!");
			rest();
		}
		// allow player to choose what to spend
		if (energy > cost && money > cost) 
		{
			System.out.println("Would you like to... " + "\n1. walk (costs energy) " + "\n2. take a cab (costs money)");
			int choice = Utility.errorTrapInt(2, 1);
			if (choice == 1) {
				energy = energy - cost;
				locationX = x;
				locationY = y;
			} else {
				money = money - cost;
				locationX = x;
				locationY = y;
			}
		} 
		//not enough money, but enough energy
		else if (energy > cost) 
		{
			System.out.println("You don't have enough money for a cab, you must walk");
			energy = energy - cost;
			locationX = x;
			locationY = y;
		} 
		//not enough energy, but enough money
		else 
		{
			System.out.println("You don't have enough energy to walk, you must take a cab.");
			money = money - cost;
			locationX = x;
			locationY = y;
		}
	}
	
	//method to play gigs
	public static void gig() throws InterruptedException 
	{
		//transport to club and show correct map
		if (locationX != 4 || locationY != 3)
		{
			
			if(locationX == 1 && locationY == 1)
			{
				transport(4,3);
				displayMap(4);
			}
			else if(locationX == 1 && locationY == 5)
			{
				transport(4,3);
				displayMap(3);
			}
		}
		//only can play gig if player has sufficient energy
		if (energy > 20) 
		{
			int earned = 0;
			System.out.println("This is your time to shine, go kill it! "
					+ "\nYou are going to have to pick your three main tracks of the night,"
					+ "\nHere is your music library");
			display();
			//recieve input for the three songs
			System.out.println("\nFirst track?");
			int track1 = Utility.errorTrapInt(musicCounter, 1);
			System.out.println("Second track?");
			int track2 = Utility.errorTrapInt(musicCounter, 1);
			System.out.println("Third track?");
			int track3 = Utility.errorTrapInt(musicCounter, 1);
			int sum = track1 + track2 + track3;
			//use track choices to determine the money earned, moderately random.
			//money earned can increase with more records owned
			if (sum <= 15) 
			{
				earned = earned + Utility.rng(30, 10);
			} 
			else if (sum <= 30) 
			{
				earned = earned + Utility.rng(50, 10);
			}
			else
			{
				earned = earned + Utility.rng(100, 30);
			}
			System.out.println("Good work, you earned $" + (earned + equiptmentBonus));
			money = money + earned + equiptmentBonus;
			energy = energy - 20;
		} 
		else
			System.out.println("You don't have enough energy to play the gig, go eat or rest.");
	}

	//method for the dj store
	public static void store() 
	{
		boolean exit = false;
		System.out.println("Welcome to the store!");
		while (money >= 20 && exit == false) 
		{
			//menu
			System.out.println("\nAvailable for purchase:\t\tAvailable to sell: "
				+ "\n1. Basic mixer: $100\t\t6. Table"
				+ "\n2. Mini speakers: $20\t\t7. Speakers" 
				+ "\n3. 2 deck turn table: $300" 
				+ "\n4. Medium speakers: $50"
				+ "\n5. 4 records: $20 " 
				+ "\n8. Exit");
			//display inventory
			System.out.println("You currently have: Second hand minimal mixer  Plastic cups ");
			if(table1 == true)
				System.out.print("  Basic mixer");
			if(table2 == true)
				System.out.print("  2 deck turn table");
			if(speaker1 == true)
				System.out.print("  Mini speakers");
			if(speaker2 == true)
				System.out.print("  Medium speakers");
			System.out.println("\nWhat would you like to do?\t\tEnergy:" + energy + " Money:" + money);
			int choice = Utility.errorTrapInt(8, 1);
			/* buying choices check to see if object is owned, check if there is enough money
			and then sell if all qualifications are met */
			if (choice == 1) 
			{
				if (table1 == true)
					System.out.println("You already own this!");
				else if (money >= 100) 
				{
					money = money - 100;
					equiptmentBonus = equiptmentBonus + 20;
					table1 = true;
					System.out.println("purchased");
				} 
				else
					System.out.println("You don't have enough money for this!");
			} 
			else if (choice == 2) 
			{
				if (speaker1 == true)
					System.out.println("You already own this!");
				else if (money >= 20) 
				{
					money = money - 20;
					equiptmentBonus = equiptmentBonus + 4;
					speaker1 = true;
					System.out.println("purchased");
				} 
				else
					System.out.println("You don't have enough money for this!");
			} 
			else if (choice == 3) 
			{
				if (table2 == true)
					System.out.println("You already own this!");
				else if (money >= 300) 
				{
					money = money - 300;
					equiptmentBonus = equiptmentBonus + 60;
					table2 = true;
					System.out.println("purchased");
				} 
				else
					System.out.println("You don't have enough money for this!");
			} 
			else if (choice == 4) 
			{
				if (speaker2 == true)
					System.out.println("You already own this!");
				else if (money >= 50) 
				{
					money = money - 50;
					equiptmentBonus = equiptmentBonus + 10;
					speaker2 = true;
					System.out.println("purchased");
				} else
					System.out.println("You don't have enough money for this!");
			} 
			else if (choice == 5) 
			{
				if (money >= 25) 
				{
					if(musicCounter <= 16)
					{
					money = money - 20;
					musicCounter = musicCounter + 4;
					System.out.println("purchased");
					}
					else
						System.out.println("You own too many records");
					
				} 
				else
					System.out.println("You don't have enough money for this!");
			}
			//selling table
			else if (choice == 6) 
			{
				//checks to see if user has anything of value to sell
				if (table1 == false && table2 == false)
					System.out.println("I can't give you any money for that.");
				//if they own both give them the option
				else if (table1 == true && table2 == true)
				{
					System.out.println("\n1. Sell Basic mixer \n2. Sell 2 deck turn table.");
					int choice1 = Utility.errorTrapInt(2, 1);
					if(choice1 == 1)
					{
						System.out.println("Okay, I'll give you $50");
						money = money + 50;
						table1 = false;
						equiptmentBonus = equiptmentBonus - 20;
					}
					else
					{
						System.out.println("Okay, I'll give you $150");
						money = money + 150;
						table2 = false;
						equiptmentBonus = equiptmentBonus - 60;
					}	
				}
				//only give table1 option
				else if (table1 == true) {
					System.out.println("Okay, I'll give you $50");
					money = money + 50;
					table1 = false;
					equiptmentBonus = equiptmentBonus - 20;
				}
				//only give table2 option
				else if (table2 == true) {
					System.out.println("Okay, I'll give you $150");
					money = money + 150;
					table2 = false;
					equiptmentBonus = equiptmentBonus - 60;
				}
				
			} 
			//sell speakers, runs the same as sell tables
			else if (choice == 7) 
			{
				if (speaker1 == false && speaker2 == false)
					System.out.println("I can't give you any money for that.");
				else if (speaker1 == true && speaker2 == true)
				{
					System.out.println("\n1. Sell Basic mixer \n2. Sell 2 deck turn table.");
					int choice1 = Utility.errorTrapInt(2, 1);
					if(choice1 == 1)
					{
						System.out.println("Okay, I'll give you $50");
						money = money + 50;
						speaker1 = false;
						equiptmentBonus = equiptmentBonus - 4;
					} 
					else
					{
						System.out.println("Okay, I'll give you $150");
						money = money + 150;
						speaker2 = false;
						equiptmentBonus = equiptmentBonus - 10;
					}	
				}
				else if (speaker1 == true) 
				{
					System.out.println("Okay, I'll give you $10");
					money = money + 10;
					speaker1 = false;
					equiptmentBonus = equiptmentBonus - 4;
				} 
				else if (speaker2 == true) 
				{
					System.out.println("Okay, I'll give you $25");
					money = money + 25;
					speaker2 = false;
					equiptmentBonus = equiptmentBonus - 10;
				}
			}
			else if (choice == 8)
				exit = true;
				
		}
		if (money < 20)
			System.out.println("You don't have enough money to spend! Go play some gigs.");
	}
	//method to populate music array for gigs
	public static void populateMusic() throws IOException 
	{
		File read = new File("music.txt");
		Scanner input = new Scanner(read);
		int x = 0;
		while (input.hasNextLine() && x < SIZE) 
		{
			music[x] = input.nextLine();
			x++;
		}
	}
	//method to display music inventory
	public static void display() 
	{
		for (int y = 0; y < musicCounter - 1; y = y+2)
		{
			
			if(y%2 == 0)
			{
				System.out.println();
			}
				System.out.printf("%-30.30s  %-30.30s%n", music[y], music[y+1]);
		}
	}
	//method to populate maps from file
	public static void populateMap() throws IOException
	{
		File read = new File("map.txt");
		Scanner input = new Scanner(read);
		int y = 0;
		while(input.hasNextLine() && y < 15)
		{
			String line = input.nextLine();
			map1[y] = line;
			y++;
		}
		y = 0;
		while(input.hasNextLine() && y < 15)
		{
			String line = input.nextLine();
			map2[y] = line;
			y++;
		}
		y = 0;
		while(input.hasNextLine() && y < 15)
		{
			String line = input.nextLine();
			map3[y] = line;
			y++;
		}
		y = 0;
		while(input.hasNextLine() && y < 15)
		{
			String line = input.nextLine();
			map4[y] = line;
			y++;
		}
	}
	//method to display map when traveling
	public static void displayMap(int map)
	{
		if (map == 1)
		{
			for(int x = 0; x < 15; x++)
				System.out.println(map1[x]);
		}
		else if (map == 2)
		{
			for(int x = 0; x < 15; x++)
				System.out.println(map2[x]);
		}
		else if (map == 3)
		{
			for(int x = 0; x < 15; x++)
				System.out.println(map3[x]);
		}
		else if (map == 4)
		{
			for(int x = 0; x < 15; x++)
				System.out.println(map4[x]);
		}
	}

}
