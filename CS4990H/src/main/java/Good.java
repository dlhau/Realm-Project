import java.util.Random;

/*
 * David Hau
 * CS4990H
 */

public class Good
{
	private int productionTime = 1; // Turns taken to create good
	private int cost = 1;           // How much resources needed to create good per turn
	private int decay = 1;          // Turns taken for static characteristics to decay
	private double necessityStatic = 0.0;
	private double necessityDynamic = 0.0;
	private double luxuryStatic = 0.0;
	private double luxuryDynamic = 0.0;
	
	private int goodLifetime = 0;   // Tracks good decay
	private boolean consumed = false;
	private boolean producing = false;
	
	Good()
	{
		Random random = new Random();
		int goodNumber = Math.abs(random.nextInt(99) + 1);
		random = new Random();
		int priorityNumberNecessity = Math.abs(random.nextInt(1) + 1);
		random = new Random();
		int priorityNumberLuxury = Math.abs(random.nextInt(1) + 1);
		random = new Random();
		int goodType = 0;
		boolean defaultGood = false;
		
		// Chance of good with both necessity and luxury
		if(goodNumber <= 10)
		{
			goodType = 1;
		}
		// Chance of necessity good
		else if(goodNumber <= 55)
		{
			goodType = 2;
		}
		// Chance of luxury good
		else
		{
			goodType = 3;
		}
		
		// Necessity & Luxury
		if(goodType == 1)
		{
			random = new Random();
			necessityStatic = Math.abs(random.nextInt(99) + 1);
			random = new Random();
			necessityDynamic = Math.abs(random.nextInt(99) + 1);
			
			// Necessity Settings
			setNecessitySettings(priorityNumberNecessity);
			
			random = new Random();
			luxuryStatic = Math.abs(random.nextInt(99) + 1);
			random = new Random();
			luxuryDynamic = Math.abs(random.nextInt(99) + 1);
			
			// Luxury Settings
			setLuxurySettings(priorityNumberLuxury);
			
		}
		// Necessity Only
		else if(goodType == 2)
		{
			random = new Random();
			necessityStatic = Math.abs(random.nextInt(99) + 1);
			random = new Random();
			necessityDynamic = Math.abs(random.nextInt(99) + 1);
			
			setNecessitySettings(priorityNumberNecessity);
		}
		// Luxury Only
		else if(goodType == 3)
		{
			
			random = new Random();
			luxuryStatic = Math.abs(random.nextInt(99) + 1);
			random = new Random();
			luxuryDynamic = Math.abs(random.nextInt(99) + 1);
			
			setLuxurySettings(priorityNumberLuxury);
		}
		// Default
		else
		{
			necessityDynamic = 1.0;
			necessityStatic = 1.0;
			luxuryDynamic = 1.0;
			luxuryStatic = 1.0;
			defaultGood = true;
		}
		
		// Check if default good
		if(!defaultGood)
		{
			setProductionTimeSetting();
			setCostSetting();
			setDecaySetting();
		}
	}
	
	public void setNecessitySettings(int priorityNumberNecessity)
	{
		if(getNecessityTotal() > 100.0)
		{
			if(priorityNumberNecessity == 1)
			{
				double tempNum = getNecessityTotal();
				necessityDynamic = necessityDynamic - (tempNum - 100.0);
				
				if(necessityDynamic < 0)
				{
					tempNum = necessityDynamic;
					necessityDynamic = 0;
					necessityStatic = necessityStatic - tempNum;
				}
			}
			else
			{
				double tempNum = getNecessityTotal();
				necessityStatic = necessityStatic - (tempNum - 100.0);
				
				if(necessityStatic < 0)
				{
					tempNum = necessityStatic;
					necessityStatic = 0;
					necessityDynamic = necessityDynamic - tempNum;
				}
			}
		}
	}
	
	public void setLuxurySettings(int priorityNumberLuxury)
	{
		if(getLuxuryTotal() > 100.0)
		{
			if(priorityNumberLuxury == 1)
			{
				double tempNum = getLuxuryTotal();
				luxuryDynamic = luxuryDynamic - (tempNum - 100.0);
				
				if(luxuryDynamic < 0)
				{
					tempNum = luxuryDynamic;
					luxuryDynamic = 0;
					luxuryStatic = luxuryStatic - tempNum;
				}
			}
			else
			{
				double tempNum = getLuxuryTotal();
				luxuryStatic = luxuryStatic - (tempNum - 100.0);
				
				if(luxuryStatic < 0)
				{
					tempNum = luxuryStatic;
					luxuryStatic = 0;
					luxuryDynamic = luxuryDynamic - tempNum;
				}
			}
		}
	}
	
	public void setProductionTimeSetting()
	{
		double timeNecessityStatic = 0;
		double timeNecessityDynamic = 0;
		double timeLuxuryStatic = 0;
		double timeLuxuryDynamic = 0;
		
		if(necessityStatic > 0)
		{
			timeNecessityStatic = ((necessityStatic / 3) + 1);
		}
		
		if(necessityDynamic > 0)
		{
			timeNecessityDynamic = ((necessityDynamic / 10) + 1);
		}
		
		if(luxuryStatic > 0)
		{
			timeLuxuryStatic = ((luxuryStatic / 4) + 1);
		}
		
		if(luxuryDynamic > 0)
		{
			timeLuxuryDynamic = ((luxuryStatic / 8) + 1);
		}
		
		productionTime = ((int) (timeNecessityStatic + timeNecessityDynamic + timeLuxuryStatic + timeLuxuryDynamic));
		
		if(productionTime <= 0)
		{
			productionTime = 1;
		}
	}
	
	public void setCostSetting()
	{
		double costNecessityStatic = 0;
		double costNecessityDynamic = 0;
		double costLuxuryStatic = 0;
		double costLuxuryDynamic = 0;
		
		if(necessityStatic > 0)
		{
			costNecessityStatic = (necessityStatic * 5);
		}
		
		if(necessityDynamic > 0)
		{
			costNecessityDynamic = (necessityDynamic * 1);
		}
		
		if(luxuryStatic > 0)
		{
			costLuxuryStatic = (luxuryStatic * 20);
		}
		
		if(luxuryDynamic > 0)
		{
			costLuxuryDynamic = (luxuryDynamic * 10);
		}
		
		if(cost <= 0)
		{
			cost = 1;
		}
	}
	
	public void setDecaySetting()
	{
		double necessityDecay = 0;
		double luxuryDecay = 0;
		
		if(necessityStatic > 0)
		{
			necessityDecay = ((necessityStatic / 3) + 1);
		}
		
		if(luxuryStatic > 0)
		{
			luxuryDecay = ((luxuryStatic / 4) + 1);
		}
		
		if(necessityDecay > luxuryDecay)
		{
			decay = (int) necessityDecay;
		}
		else
		{
			decay = (int) luxuryDecay;
		}
		
		goodLifetime = decay;

	}
	
	public void decayGood()
	{
		if(goodLifetime > 0)
		{
			goodLifetime = goodLifetime - 1;
		}
	}
	
	public boolean obtainResources(Environment environment)
	{
		return environment.consumeResource(cost);
	}
	
	public void setConsumed(boolean consumed) { this.consumed = consumed; }
	public void setProducing(boolean producing) { this.producing = producing; }
	
	public int getProductionTime() { return productionTime; }
	public int getCost() { return cost; }
	public int getDecay() { return decay; }
	public double getNecessityStatic() { return necessityStatic; }
	public double getNecesssityDynamic() { return necessityDynamic; }
	public double getNecessityTotal() { return necessityStatic + necessityDynamic; }
	public double getLuxuryStatic() { return luxuryStatic; }
	public double getLuxuryDynamic( ) { return luxuryDynamic; }
	public double getLuxuryTotal() { return luxuryStatic + luxuryDynamic; }
	public int getGoodLifetime() { return goodLifetime; }
	public boolean getConsumed() { return consumed; }
	public boolean getProducing() { return producing; }
	
}
