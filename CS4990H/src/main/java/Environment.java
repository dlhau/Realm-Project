import java.util.Random;

/*
 * David Hau
 * CS4990H
 */

public class Environment
{
	private final int MIN_RESOURCE = 0;
	private final int MAX_RESOURCE = Integer.MAX_VALUE;
	
	public int resource = 0;
	
	Environment()
	{
		Random random = new Random();
		generateResource(Math.abs(random.nextInt()));
	}
	
	Environment(int seed)
	{
		generateResource(seed);
	}
	
	// Generates resource. Always has at least 10,000 and varies by 1,000,000/
	public void generateResource(int seed)
	{
		Random random = new Random(seed);
		resource = Math.abs((random.nextInt(999999)) + 10000);
	}
	
	// Checks if enough resource is available for the current resource cost
	public boolean checkResourceAvailability(int resourceCost)
	{
		if(resource - resourceCost < MIN_RESOURCE)
		{
			return false;
		}
		
		return true;
	}
	
	// Deducts resource from the Environment
	public void useResource(int resourceCost)
	{
		resource =  resource - resourceCost;
	}
	
	// Consumes resource if enough are available
	public boolean consumeResource(int resourceCost)
	{
		boolean usedResource = checkResourceAvailability(resourceCost);
		
		if(usedResource)
		{
			useResource(resourceCost);
		}
		
		return usedResource;
	}
	
	public int getResource() { return resource; }
}
