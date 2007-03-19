package baseobject;

import agent.AgentLocation;

public class Flag extends Bobject{
	private AgentLocation initialPosition;
	protected int owner;
	protected boolean isOwned;
	
	public boolean getOwned()
	{
		return isOwned;
	}
	public void setOwned(boolean newOwned)
	{
		isOwned = newOwned;
	}
	
	public void toggleOwned()
	{
		isOwned = !isOwned;
	}
	
	public int getOwner()
	{
		return owner;
	}
	
	public void setOwner(int newOwner)
	{
		owner = newOwner;
	}
	
	public int getSoundRadius()
	{
		return soundRadius;
	}
	
	public void reset()
	{
		location = initialPosition;
	}
}
