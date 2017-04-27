package codes;

import java.util.Scanner;

public class Utility {
	//Integer Error Trap
	public static int errorTrapInt(int max, int min)
	{
		int var = 0;
		Scanner input = new Scanner(System.in);
		boolean passed = true;
		do 
		{
			passed = true;
			try 
			{
				var = input.nextInt();
			}
			//if input is not an integer
			catch (Exception error) 
			{
				System.out.print("Invalid input, please try again : ");
				//clear error
				input.nextLine();
				passed = false;
			}
			//if input is not in range
			if (passed == true && (var < min || var > max)) 
			{
				System.out.print("Value is out of range, please try again : ");
				passed = false;
			}
		}while(passed == false);
		return var;
		}
	public static int errorTrap()
	{
		int var = 0;
		Scanner input = new Scanner(System.in);
		boolean passed = true;
		do 
		{
			passed = true;
			try 
			{
				var = input.nextInt();
			}
			//if input is not an integer
			catch (Exception error) 
			{
				System.out.print("Invalid input, please try again : ");
				//clear error
				input.nextLine();
				passed = false;
			}
			
		}while(passed == false);
		return var;
	}
		//Double Error Trap
	public static double errorTrapDouble(double max, double min)
	{
		Double var = 0.00;
		Scanner input = new Scanner(System.in);
		boolean passed = true;
		do 
		{
			passed = true;
			try 
			{
				var = input.nextDouble();
			}
			//if input is not a double
			catch (Exception error) 
			{
				System.out.print("Invalid input, please try again : ");
				//clear error
				input.nextLine();
				passed = false;
			}
			//if input is not in range
			if (passed == true && (var < min || var > max)) 
			{
				System.out.print("Value is out of range, please try again : ");
				passed = false;
			}
		}while(passed == false);
		return var;
	}
	//Random Number generator
	public static int rng(int max, int min)
	{
		//Min and max switch
		if (min > max)
		{
			int temp = max;
			max = min;
			min = temp;
		}
		int rnd = (int) (Math.random() * (max - min + 1) + min);
		return rnd;
	}
	//Find a digit
	public static int digitAt(int number, int n)
	{
		int digit = 0;
		while (number >= (Math.pow(10, n-1)))
		{
			digit = (number % 10);
			number = (number / 10);
			
		}
		return digit;
	}
	//Get character input
	public static char getChar()
	{
		Scanner input = new Scanner(System.in);
		String in = input.next();
		char letter = in.charAt(0);
		return letter;
	}
}
