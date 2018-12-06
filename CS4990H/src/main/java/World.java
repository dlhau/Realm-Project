import java.util.ArrayList;
import java.util.Random;

/*
 * David Hau
 * CS4990H
 */
public class World
{
	Governor governor = new Governor();
	Environment environment;
	ArrayList<Commoner> populace = new ArrayList<Commoner>();
	ArrayList<Job> jobs = new ArrayList<Job>();
	
	private int turns = 0;
	private int maxTurns = Integer.MAX_VALUE;
	private boolean complete = false;
	
	private int initialEnvironmentResources = 0;
	private int totalGoodsProduced = 0;
	private int totalGoodsSold = 0;
	private int totalDead = 0;
	private int totalTaxesCollected = 0;
	private int gdpCount = 0;
	private double gdpScore = 0.0;
	private double gdpAverage = 0.0;
	
	World()
	{
		environment = new Environment();
		populateCommoner();
	}
	
	World(int seed)
	{
		environment = new Environment(seed);
		populateCommoner();
	}
	
	World(int seed, int turns)
	{
		maxTurns = turns;
		environment = new Environment(seed);
		populateCommoner();
	}
	
	private void populateCommoner()
	{
		for(int i = 0; i < 100; i++)
		{
			populace.add(i, new Commoner());;
		}
	}
	
	// Produces a random negative event in the world
	public void useDisaster()
	{
		Random random = new Random();
		int chance = Math.abs(random.nextInt(99) + 1);
		
		if(chance <= 40)
		{
			// kill population
			for(int i = populace.size() - 1; i >= 0; i--)
			{
				Random disasterRandom = new Random();
				int disasterModifier = Math.abs(disasterRandom.nextInt(99) + 1);
				if(disasterModifier <= 10)
				{
					Job tempJob = populace.get(i).getJob();
					for(int j = 0; j < jobs.size(); j++)
					{
						if(jobs.get(i) == tempJob)
						{
							jobs.get(i).fireWorker(populace.get(i));
							break;
						}
					}
					populace.get(i).kill();
					populace.remove(i);
				}
			}
		}
		else if (chance <= 80)
		{
			// destroy goods
			for(int i = 0; i < jobs.size(); i++)
			{
				Random disasterRandom = new Random();
				int disasterModifier = Math.abs(disasterRandom.nextInt(99) + 1);
				if(disasterModifier <= 10)
				{
					int goodsDestroyed = jobs.get(i).getGoodStockpile() / 2;
					jobs.get(i).removeGoodStockpile(goodsDestroyed);
				}
			}
		}
		else
		{
			// kill population & destroy goods
			// kill population
			for(int i = populace.size() - 1; i >= 0; i--)
			{
				Random disasterRandom = new Random();
				int disasterModifier = Math.abs(disasterRandom.nextInt(99) + 1);
				if(disasterModifier <= 10)
				{
					Job tempJob = populace.get(i).getJob();
					for(int j = 0; j < jobs.size(); j++)
					{
						if(jobs.get(i) == tempJob)
						{
							jobs.get(i).fireWorker(populace.get(i));
							break;
						}
					}
					populace.get(i).kill();
					populace.remove(i);
				}
			}
			
			// destroy goods
			for(int i = 0; i < jobs.size(); i++)
			{
				Random disasterRandom = new Random();
				int disasterModifier = Math.abs(disasterRandom.nextInt(99) + 1);
				if(disasterModifier <= 10)
				{
					int goodsDestroyed = jobs.get(i).getGoodStockpile() / 2;
					jobs.get(i).removeGoodStockpile(goodsDestroyed);
				}
			}
		}
		
	}
	
	public int getGDP()
	{
		int gdp = 0;
		
		for(int i = 0; i < jobs.size(); i++)
		{
			gdp = gdp + (jobs.get(i).getGoodPrice() * jobs.get(i).getGoodSold());
		}
		
		return gdp;
	}
	
	public void calculateGDPAverage()
	{
		gdpAverage = gdpScore / gdpCount;
	}
	
	public void simulationResults()
	{
		
		System.out.println("\nSimulation Finished in " + getTurns() +" days.");
	}
	
	public void simulate()
	{
		if(turns <= 0)
		{
			// initial setup
			System.out.println("INITIAL SETUP\n");
			// Generate four Jobs
			System.out.println("GENERATING JOBS\n");
			for(int i = 0; i < 4; i++)
			{
				// 100000 Budget, 25 available jobs
				jobs.add(new Job(100000, 25));
			}
			
			// Generate 100 commoners
			System.out.println("GENERATING COMMONERS\n");
			for(int i = 0; i < 100; i++)
			{
				populace.add(new Commoner());
			}
			
			// Give jobs
			System.out.println("ALLOCATING JOBS\n");
			for(int i = 0; i < jobs.size(); i++)
			{
				for(int j = 0; j < populace.size(); j++)
				{
					if(jobs.get(i).getJobOpenings() > 0)
					{
						if(!populace.get(j).getHasJob())
						{
							populace.get(j).obtainJob(jobs.get(i));
							
						}
					}
				}
			}
			
			// determine jobs
			System.out.println("UPDATING JOBS\n");
			for(int i = 0; i < jobs.size(); i++)
			{
				jobs.get(i).basicSalarySet();
				//System.out.println("Salary " + i + ": " + jobs.get(i).getJobSalary());
				jobs.get(i).basicGoodPriceSet();
				//System.out.println("Good Price " + i + ": " + jobs.get(i).getGoodPrice());
			}
			
			initialEnvironmentResources = environment.getResource();
			System.out.println("Current Environment Resources: " + environment.getResource());
		}
		
		nextDay();
		
		if(turns >= maxTurns)
		{
			complete = true;
			endReport();
		}
	}
	
	public void nextDay()
	{
		System.out.println("\nTURN " + (turns + 1) + "\n");
		
		// create goods
		System.out.println("\nCREATING GOODS");
		int goodTotal = 0;
		for(int i = 0; i < jobs.size(); i++)
		{
			jobs.get(i).produceGoods(environment);
			goodTotal = goodTotal + jobs.get(i).getGoodStockpile();
		}
		System.out.println("Goods Total: " + goodTotal);
		totalGoodsProduced = totalGoodsProduced + goodTotal;
		
		// pay workers
		System.out.println("\nPAYING WORKERS");
		int workersPaid = 0;
		for(int i = 0; i < jobs.size(); i++)
		{
			jobs.get(i).payWorkers();
			workersPaid = workersPaid + jobs.get(i).getWorkforce();
		}
		System.out.println("Workers Paid: " + workersPaid);
		
		// buy goods
		System.out.println("\nBUYING GOODS");
		int soldGoods = 0;
		for(int i = 0; i < populace.size(); i++)
		{
			Random random = new Random();
			int goods = Math.abs(random.nextInt(jobs.size()));
			jobs.get(goods).sellGood(populace.get(i));
			soldGoods = soldGoods + jobs.get(goods).getGoodSold();
		}
		System.out.println("Goods Sold: " + soldGoods);
		totalGoodsSold = totalGoodsSold + soldGoods;
		
		// consume goods
		System.out.println("\nCONSUMING GOODS");
		for(int i = 0; i < populace.size(); i++)
		{
			populace.get(i).consumeRecentGood();
			populace.get(i).updateCommonerStats();
		}
		
		// check commoner life status
		System.out.println("\nCHECKING FOR DEAD");
		int dead = 0;
		for(int i = populace.size() - 1; i >= 0; i--)
		{
			if(!populace.get(i).getLife())
			{
				populace.remove(i);
				dead = dead + 1;
			}
		}
		System.out.println("Total Dead This Turn: " + dead);
		totalDead = totalDead + dead;
		
		// collect taxes
		System.out.println("\nCOLLECTING TAXES");
		governor.collectTax(populace, jobs);
		System.out.println("Taxes Collected This Turn: " + governor.getTaxCollected());
		totalTaxesCollected = totalTaxesCollected + governor.getTaxCollected();
		governor.resetTaxCollected();
		
		// Reset Payments
		for(int i = 0; i < populace.size(); i++)
		{
			populace.get(i).resetReceivedPayment();
		}
		
		System.out.println("\nCurrent Environment Resources: " + environment.getResource());
		System.out.println("GDP This Turn: " + getGDP());
		gdpScore = gdpScore + getGDP();
		gdpCount = gdpCount + 1;
		calculateGDPAverage();
		
		for(int i = 0; i < jobs.size(); i++)
		{
			jobs.get(i).resetGoodSold();
		}
		
		turns = turns + 1; // End turn
	}
	
	public void endReport()
	{
		System.out.println("\n------ AFTER-SIMULATION REPORT ------\n");
		
		System.out.println("Total Resources Used: " + (initialEnvironmentResources - environment.getResource()));
		System.out.println("Total Goods Produced: " + totalGoodsProduced);
		System.out.println("Total Goods Sold: " + totalGoodsSold);
		System.out.println("Total Deaths: " + totalDead);
		System.out.println("Total Taxes Collected: " + totalTaxesCollected);
		System.out.println("Average GDP: " + gdpAverage);
		
		System.out.println("\n------ AFTER-SIMULATION REPORT ------\n");
	}
	
	public int getTurns() { return turns; }
	public boolean getComplete() { return complete; }
	public Governor getGovernor() { return governor; }
	public Environment getEnvironment() { return environment; }
	public ArrayList<Commoner> getPopulace() { return populace; }
	public int getInitialEnvironmentResources() { return initialEnvironmentResources; }
	public int getTotalGoodsProduced() { return totalGoodsProduced; }
	public int getTotalGoodsSold() { return totalGoodsSold; }
	public int getTotalDead() { return totalDead; }
	public int getTotalTaxesCollected() { return totalTaxesCollected; }
	public int getGDPCount() { return gdpCount; }
	public double getGDPScore() { return gdpScore; }
	public double getGDPAverage() { return gdpAverage; }
}