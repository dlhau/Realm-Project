import java.util.Random;
import java.util.Scanner;

/*
 * David Hau
 * CS4990H
 */

public class Engine
{
	public void executeProgram()
	{
		Random random = new Random();
		Scanner keyboard = new Scanner(System.in);
		System.out.println("REALM SIMULATOR\n");
		
		int days = 0, seed = 0;
		World world;
		
		System.out.print("Enter the numbers of days to run (0 or less gives max days): ");
		days = keyboard.nextInt();
		System.out.println("Days: " + days + "\n");
		
		System.out.print("Enter a seed value (0 or less generates random seed): ");
		seed = keyboard.nextInt();
		System.out.println("Seed Value: " + seed + "\n");
		
		if(seed > 0 && days > 0)
		{
			world = new World(seed, days);
		}
		else if (seed <= 0 && days > 0)
		{
			seed = random.nextInt();
			world = new World(seed, days);
		}
		else if (seed > 0 && days <= 0)
		{
			world = new World(seed);
		}
		else
		{
			seed = random.nextInt();
			world = new World(seed);
		}
		
		System.out.println("World Generated: Resources Available - " + world.getEnvironment().getResource() + ", Seed - " + seed);
		
		while(!world.getComplete())
		{
			// Simulate world
			world.simulate();
		}
		
		keyboard.close();
	}
}
