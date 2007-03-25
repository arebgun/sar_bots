package baseobject;

import agent.AgentLocation;
import config.ConfigBobject;

public class Flag extends Bobject{
	protected int owner;
	protected boolean isOwned;
	protected int teamID; //used to associate the flag with a specific team
	
	//Consturctors for Flag
	public Flag(ConfigBobject conf)
	{
		this.config = conf;
		initialLocation = config.objectLocation();
		location = config.objectLocation();
		teamID = config.getTeamID();
		owner = 0;
		isOwned = false;
		type = types.FLAG;
	}
	public Flag(AgentLocation loc, int id)
	{
		initialLocation = loc;
		location = loc;
		teamID = id;
		owner = 0;
		isOwned = false;
		type = types.FLAG;
	}
	
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
		location = initialLocation;
	}
}
