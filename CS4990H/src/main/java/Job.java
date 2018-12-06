import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/*
 * David Hau
 * CS4990H
 */

public class Job
{
	ArrayList<Commoner> workers = new ArrayList<Commoner>();
	
	private int budget = 0;
	private int jobOpenings = 0;
	private int workforce = 0;
	private String uniqueID = UUID.randomUUID().toString();
	private Good good;
	
	private int goodStockpile = 0;
	private int goodPrice = 0;
	private int jobSalary = 0;
	private int goodSold = 0;
	
	private double productionQueue = 0.0;
	private boolean taxExempt = false;
	
	Job(int budget, int jobOpenings)
	{
		this.budget = budget;
		this.jobOpenings = jobOpenings;
		good = new Good();
	}
	
	public void addBudget(int funds)
	{
		if(funds > 0)
		{
			budget = budget + funds;
		}
	}
	
	public void setGoodPrice(int price)
	{
		if(price >= 0)
		{
			goodPrice = price;
		}
	}
	
	public void hireWorker(Commoner commoner)
	{
		jobOpenings = jobOpenings - 1;
		workforce = workforce + 1;
		workers.add(commoner);
	}
	
	public void fireRandomWorker()
	{
		Random random = new Random();
		workers.remove(Math.abs(random.nextInt(workers.size())));
		jobOpenings = jobOpenings + 1;
		workforce = workforce - 1;
	}
	
	public void fireWorker(Commoner worker)
	{
		workers.remove(worker);
		jobOpenings = jobOpenings + 1;
		workforce = workforce - 1;
	}
	
	public void payWorkers()
	{
		for(int i = 0; i < workers.size(); i++)
		{
			if(jobSalary <= budget)
			{
				workers.get(i).receivePayment(jobSalary);
				budget = budget - jobSalary;
			}
			else
			{
				fireWorker(workers.get(i));
				setJobOpenings(0);
			}
		}
	}
	
	public void produceGoods(Environment environment)
	{
		for(int i = 0; i < workers.size(); i++)
		{
			workers.get(i).evaluateResourceModifier();
			if(good.obtainResources(environment))
			{
				productionQueue = productionQueue + 
						(workers.get(i).getResourceModifier() * good.getProductionTime());
			}
		}
		
		goodStockpile = (goodStockpile + ((int) productionQueue));
		productionQueue = productionQueue - ((int) productionQueue);
	}
	
	public void sellGood(Commoner commoner)
	{
		// check if there is a good stockpile
		if(goodStockpile > 0)
		{
			// check if commoner can afford good
			if(commoner.getGold() >= getGoodPrice())
			{
				// deduct the good and exchange gold
				commoner.removeGold(getGoodPrice());
				budget = budget + getGoodPrice();
				goodStockpile = goodStockpile - 1;
				commoner.obtainGood(good);
				goodSold = goodSold + 1;
			}
		}
	}
	
	public void vacancy(Commoner commoner)
	{
		jobOpenings = jobOpenings + 1;
		workforce = workforce - 1;
		workers.remove(commoner);
	}
	
	public void basicGoodPriceSet()
	{
		int calculatedPrice = 0;
		calculatedPrice = good.getCost() * good.getProductionTime();
		calculatedPrice = (int) (calculatedPrice * 1.02);
		goodPrice = calculatedPrice;
	}
	
	public void determineGoodPrice()
	{
		// To be done
	}
	
	public void basicSalarySet()
	{
		int calculatedSalary = 0;
		calculatedSalary = good.getCost() * good.getProductionTime();
		calculatedSalary = (int) (calculatedSalary * 1.05);
		jobSalary = calculatedSalary;
	}
	
	public void setJobOpenings(int jobOpenings)
	{
		if(jobOpenings > 0)
		{
			this.jobOpenings = jobOpenings;
		}
	}
	
	public void setJobSalary(int salary)
	{
		if(salary >= 0)
		{
			jobSalary = salary;
		}
	}
	
	public void setGood(Good good)
	{
		this.good = good;
	}
	
	public void removeBudget(int budget)
	{
		this.budget = this.budget - budget;
	}
	
	public void removeGoodStockpile(int amount)
	{
		if(amount > goodStockpile)
		{
			goodStockpile = goodStockpile - amount;
		}
	}
	
	public void resetGoodSold() { goodSold = 0; }
	public void setTaxExempt(boolean taxExempt) { this.taxExempt = taxExempt; }
	
	public int getBudget() { return budget; }
	public int getJobOpenings() { return jobOpenings; }
	public int getWorkforce() { return workforce; }
	public String getUniqueID() { return uniqueID; }
	public Good getGood() { return good; }
	public int getGoodStockpile() { return goodStockpile; }
	public int getGoodPrice() { return goodPrice; }
	public int getJobSalary() { return jobSalary; }
	public int getGoodSold() { return goodSold; }
	public double getProductionQueue() { return productionQueue; }
	public boolean getTaxExempt() { return taxExempt; }
}
