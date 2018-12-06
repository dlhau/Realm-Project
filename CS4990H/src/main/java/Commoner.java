import java.util.ArrayList;
import java.util.Random;

/*
 * David Hau
 * CS4990H
 */

public class Commoner
{
	ArrayList<Good> goods = new ArrayList<Good>();
	ArrayList<Good> usedGoods = new ArrayList<Good>();
	
	private final double MINIMUM = 0.0;
	private final double MAXIMUM = 100.0;
	private final double MINIMUM_NECESSITY = 3.0;
	
	private double necessityCurrent = MAXIMUM;
	private double luxuryCurrent = MAXIMUM;
	
	private int gold = 0;
	private boolean life = true;
	private double necessityRequired = 30.0;
	private double luxuryRequired = 0.0;
	
	
	private double necessityDynamic = 0.0;
	private double luxuryDynamic = 0.0;
	private double necessityStatic = 0.0;
	private double luxuryStatic = 0.0;
	
	private String jobID = "UNEMPLOYED";
	private Job job;
	private boolean hasJob = false;
	private boolean receivedPayment = false;
	
	private double resourceModifier = 1.0;
	
	private Commoner partner;
	private boolean mated = false;
	private boolean mature = false;
	
	private int lifetime = 0;
	private int deathCounter = 0;
	
	private boolean taxExempt = false;
	
	Commoner()
	{
		Random random = new Random();
		necessityRequired = Math.abs(random.nextInt(99) + 1);;
		if(necessityRequired < MINIMUM_NECESSITY)
		{
			necessityRequired = MINIMUM_NECESSITY;
		}
		else if(necessityRequired > MAXIMUM)
		{
			necessityRequired = MAXIMUM;
		}
		
		random = new Random();
		luxuryRequired = Math.abs(random.nextInt(99) + 1);;
		if(luxuryRequired < MINIMUM)
		{
			luxuryRequired = MINIMUM;
		}
		else if (luxuryRequired > MAXIMUM)
		{
			luxuryRequired = MAXIMUM;
		}
		
		
	}
	
	public void necessityDecay()
	{
		necessityDynamic = necessityDynamic - 10;
		
		if(necessityDynamic < MINIMUM)
		{
			necessityDynamic = MINIMUM;
		}
	}
	
	public void necessityDecay(int decayRate)
	{
		necessityDynamic = necessityDynamic - decayRate;
		
		if(necessityDynamic < MINIMUM)
		{
			necessityDynamic = MINIMUM;
		}
	}
	
	public void luxuryDecay()
	{
		luxuryDynamic = luxuryDynamic - 20;
	}
	
	public void luxuryDecay(int decayRate)
	{
		luxuryDynamic = luxuryDynamic - decayRate;
		
		if(luxuryDynamic < MINIMUM)
		{
			luxuryDynamic = MINIMUM;
		}
	}
	
	public void obtainJob(Job job)
	{
		if(job.getJobOpenings() > 0)
		{
			job.hireWorker(this);
			this.jobID = job.getUniqueID();
			this.job = job;
			setHasJob(true);
		}
	}
	
	public void loseJob(Job job)
	{
		jobID = "UNEMPLOYED";
		job.vacancy(this);
		setHasJob(false);
	}
	
	public void receivePayment(int payment)
	{
		if(!receivedPayment)
		{
			gold = gold + payment;
		}
		receivedPayment = true;
	}

	public void evaluate()
	{
		// To be Added
	}
	
	public void evaluateResourceModifier()
	{
		// Necessity-based Modifier
		if(getNecessityCurrent() < getNecessityRequired())
		{
			resourceModifier = resourceModifier * 0.75;
		}
		else if (getNecessityCurrent() >= (getMaximum() + getNecessityRequired()) / 2)
		{
			resourceModifier = resourceModifier * 1.25;
		}
		
		// Luxury-based Modifier
		if(getLuxuryCurrent() < getLuxuryRequired())
		{
			resourceModifier = resourceModifier * 0.5;
		}
		else if (getLuxuryCurrent() >= (getMaximum() + getLuxuryRequired() / 2))
		{
			resourceModifier = resourceModifier * 1.5;
		}
		else
		{
			resourceModifier = resourceModifier * 2.0;
		}
	}
	
	public void obtainGood(Good good)
	{
		goods.add(good);
	}
	
	public void matured()
	{
		if(lifetime >= 18)
		{
			mature = true;
		}
	}
	
	public void mate(Commoner commoner)
	{
		if(getMature() && commoner.getMature())
		{
			partner = commoner;
			mated = true;
		}
	}
	
	public void separate(Commoner commoner)
	{
		if(getMated() && commoner.getMated())
		{
			if(partner == commoner)
			{
				mated = false;
				commoner.setMated(false);
				partner = null;
				commoner.removePartner();
			}
		}
	}
	
	public void consumeRecentGood()
	{
		if(!goods.isEmpty())
		{
			useGood(goods.get(goods.size() - 1));
			goods.remove(goods.size() - 1);
		}
	}
	
	public void consumeGood(Good good)
	{
		for(int i = 0; i < goods.size(); i++)
		{
			if(goods.get(i) == good)
			{
				useGood(goods.get(i));
				goods.remove(i);
				break;
			}
		}
	}
	
	public void useGood(Good good)
	{
		if(!good.getConsumed())
		{
			good.setConsumed(true);
			updateCurrentUsedGood(good);
			usedGoods.add(good);
		}
	}
	
	public void updateCurrentUsedGood(Good good)
	{
		necessityDynamic = necessityDynamic + good.getNecesssityDynamic();
		luxuryDynamic = luxuryDynamic + good.getLuxuryDynamic();
		necessityStatic = necessityStatic + good.getNecessityStatic();
		luxuryStatic = luxuryStatic + good.getNecessityStatic();
		updateCommonerStats();
		good.decayGood();
	}
	
	public void updateUsedGoods()
	{
		for(int i = usedGoods.size() - 1; i >= 0; i--)
		{
			if(usedGoods.get(i).getGoodLifetime() <= 0)
			{
				removeNecessityStatic(usedGoods.get(i).getNecessityStatic());
				removeLuxuryStatic(usedGoods.get(i).getLuxuryStatic());
				usedGoods.remove(i);
			}
			else
			{
				usedGoods.get(i).decayGood();
			}
		}
		updateCommonerStats();
	}
	
	public void updateCommonerStats()
	{
		necessityDecay();
		luxuryDecay();
		
		necessityCurrent = necessityDynamic + necessityStatic;
		luxuryCurrent = luxuryDynamic + luxuryStatic;
		
		// include life/death check and other stats
		if(necessityCurrent < necessityRequired)
		{
			deathCounter = deathCounter + 1;
			if(deathCounter >= 3)
			{
				kill();
			}
		}
	}
	
	public void resetReceivedPayment() { receivedPayment = false; }
	
	public void removeGold(int gold)
	{
		this.gold = this.gold - gold;
	}
	
	public void addNecessityStatic(double necessityStatic)
	{
		this.necessityStatic = this.necessityStatic + necessityStatic;
	}
	
	public void addLuxuryStatic(double luxuryStatic)
	{
		this.luxuryStatic = this.luxuryStatic + luxuryStatic;
	}
	
	public void removeNecessityStatic(double necessityStatic)
	{
		if(this.necessityStatic - necessityStatic > 0)
		{
			this.necessityStatic = this.necessityStatic - necessityStatic;
		}
		else
		{
			this.necessityStatic = 0;
		}
	}
	
	public void removeLuxuryStatic(double luxuryStatic)
	{
		if(this.luxuryStatic - luxuryStatic > 0)
		{
			this.luxuryStatic = this.luxuryStatic - luxuryStatic;
		}
		else
		{
			this.luxuryStatic = 0;
		}
	}
	
	public void kill() { life = false; }
	public void removePartner() { partner = null; }
	public void ageCommoner() { lifetime = lifetime + 1; }

	public void setNecessityStatic(double necessityStatic) { this.necessityStatic = necessityStatic; }
	public void setLuxuryStatic(double luxuryStatic) { this.luxuryStatic = luxuryStatic; }
	public void setMated(boolean mated) { this.mated = mated; }
	public void setParnter(Commoner partner) { this.partner = partner; }
	public void setTaxExempt(boolean taxExempt) { this.taxExempt = taxExempt; }
	public void setGold(int gold) { this.gold = gold; }
	public void setHasJob(boolean hasJob) { this.hasJob = hasJob; }
	
	public int getGold() { return gold; }
	public boolean getLife() { return life; }
	public int getDeathCounter() { return deathCounter; }
	public String getjobID() { return this.jobID; }
	public Job getJob() { return job; }
	public boolean getHasJob() { return hasJob; }
	public boolean getReceivedPayment() { return receivedPayment; }
	public double getNecessityRequired() { return necessityRequired; }
	public double getLuxuryRequired() { return luxuryRequired; }
	public double getNecessityDynamic() { return necessityDynamic; }
	public double getLuxuryDynamic() { return luxuryDynamic; }
	public double getNecessityStatic() { return necessityStatic; }
	public double getLuxuryStatic() { return luxuryStatic; }
	public double getNecessityCurrent() { return necessityCurrent; }
	public double getLuxuryCurrent() { return luxuryCurrent; }
	public double getMaximum() { return MAXIMUM; }
	public double getMinimum() { return MINIMUM; }
	public double getResourceModifier() { return resourceModifier; }
	public Commoner getPartner() { return partner; }
	public boolean getMated() { return mated; }
	public boolean getMature() { return mature; }
	public boolean getTaxExempt() { return taxExempt; }
}
