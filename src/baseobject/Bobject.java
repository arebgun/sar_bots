package baseobject;

import agent.AgentLocation;
import java.awt.Color;

import config.ConfigBobject;

public abstract class Bobject {
	protected int objectID;
	protected AgentLocation location;
	protected AgentLocation initialLocation;
	protected int boundingRadius;
	protected int soundRadius;
	protected Color color;
	protected enum types {AGENT,FLAG,OBSTACLE}
	protected enum shapes {CIRCLE,POLYGON} 
	protected types type;
	protected shapes boundingShape;
	protected boolean placed = false;
    protected ConfigBobject config;
	
	public int getObjectID()
	{
		return objectID;
	}
	
	public AgentLocation getLocation()
	{
		return location;
	}
	
	public AgentLocation getInitialLocation()
	{
		return initialLocation;
	}
	
	public int getBoundingRadius()
	{
		return boundingRadius;
	}
	
	public abstract int getSoundRadius();
	public abstract void reset();
	
	public Color getColor()
	{
		return color;
	}

	public boolean isAgent()
	{
		return (types.AGENT == type);
	}
	
	public boolean isFlag()
	{
		return (types.FLAG == type);
	}
	
	public boolean isObstacle()
	{
		return (types.OBSTACLE == type);
	}
	
	public void setObjectID(int newID)
	{
		if (newID >= 0)
			objectID = newID;
	}
	
	public void setLocation(AgentLocation newLoc)
	{
		location = newLoc;
	}
	public void setInitialLocation(AgentLocation initLoc)
	{
		initialLocation = initLoc;
	}
	
	public void setBoundingRadius(int newRad)
	{
		if (newRad > 0)
			boundingRadius = newRad;
	}
	
	public void setSoundRadius(int newSound)
	{
		if (newSound > 0)
			soundRadius = newSound;
	}
	
	public void setColor(Color newColor)
	{
		color = newColor;
	}
	
	public void setType(types newType)
	{
		type = newType;
	}
	
	public boolean isPlaced()
	{
		return placed;
	}
	
	public void togglePlaced()
	{
		placed = !placed;
	}
	
	public void setPlaced(boolean tog)
	{
		placed = tog;
	}
	
	public abstract void update();
}
