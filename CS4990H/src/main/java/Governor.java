import java.util.ArrayList;

/*
 * David Hau
 * CS4990H
 */

public class Governor
{
	private int treasuryFunds = 0;
	private int taxCollected = 0;
	private boolean life = true;
	
	private double taxRatePopulace = 0.0;
	private double taxRateBusiness = 0.0;
	
	Governor()
	{
		treasuryFunds = 1000;
		taxRatePopulace = 0.05;
		taxRateBusiness = 0.05;
	}
	
	Governor(int treasuryFunds)
	{
		this.treasuryFunds = treasuryFunds;
		taxRatePopulace = 0.05;
		taxRateBusiness = 0.05;
	}
	
	public void printCurrency(int currency, Job job)
	{
		job.addBudget(currency);
	}
	
	public void allocateFunds(int funds, Job job)
	{
		// check to see if enough funds are available
		if(funds <= treasuryFunds)
		{
			job.addBudget(funds);
			treasuryFunds = treasuryFunds - funds;
		}
	}
	
	// Taxes Commoners
	public void taxPopulace(ArrayList<Commoner> populace)
	{
		for(int i = 0; i < populace.size(); i++)
		{
			int taxCollected = (int) (populace.get(i).getGold() * taxRatePopulace);
			populace.get(i).removeGold(taxCollected);
			this.taxCollected = this.taxCollected + taxCollected;
			treasuryFunds = treasuryFunds + taxCollected;
		}
	}
	
	// Taxes Jobs
	public void taxBusiness(ArrayList<Job> jobs)
	{
		for(int i = 0; i < jobs.size(); i++)
		{
			int taxCollected = (int) (jobs.get(i).getBudget() * taxRateBusiness);
			jobs.get(i).removeBudget(taxCollected);
			this.taxCollected = this.taxCollected + taxCollected;
			treasuryFunds = treasuryFunds + taxCollected;
		}
	}
	
	public void collectTax(ArrayList<Commoner> populace, ArrayList<Job> jobs)
	{
		taxPopulace(populace);
		taxBusiness(jobs);
	}
	
	public void grantTaxExempt(Commoner commoner)
	{
		commoner.setTaxExempt(true);
	}
	
	public void grantTaxExempt(Job job)
	{
		job.setTaxExempt(true);
	}
	
	public void removeTaxExempt(Commoner commoner)
	{
		commoner.setTaxExempt(false);
	}
	
	public void removeTaxExempt(Job job)
	{
		job.setTaxExempt(false);
	}
	
	public void evaluate()
	{
		// To be added
	}
	
	public void evaluateLife(ArrayList<Commoner> populace)
	{
		// if overthrown
		// if population dies
		if(populace.isEmpty())
		{
			life = false;
		}
	}
	
	public void resetTaxCollected() { taxCollected = 0; }
	
	public void setLife() { life = false; }
	public void setTaxRatePopulace(double taxRate) { this.taxRatePopulace = taxRate; }
	public void setTaxRateBusiness(double taxRate) { this.taxRateBusiness = taxRate; }
	
	public int getTreasuryFunds() { return treasuryFunds; }
	public int getTaxCollected() { return taxCollected; }
	public boolean getLife() { return life; }
	public double getTaxRatePopulace() { return taxRatePopulace; }
	public double getTaxRateBusiness() { return taxRateBusiness; }
}