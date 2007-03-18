package baseobject;

import agent.AgentLocation;
import java.awt.Color;

public abstract class Bobject {
	protected int ObjectID;
	protected AgentLocation Location;
	protected double Orientation;
	protected int BoundingRadius;
	protected int SoundRadius;
	protected Color color;
	
	public int getObjectID()
	{
		return ObjectID;
	}
	
	public AgentLocation getLocation()
	{
		return Location;
	}
	
	public double getOrientation()
	{
		return Orientation;
	}
	
	public int getBoundingRadius()
	{
		return BoundingRadius;
	}
	
	public abstract int getSoundRadius();
	
	public Color getColor()
	{
		return color;
	}

	public void setObjectID(int newID)
	{
		if (newID >= 0)
			ObjectID = newID;
	}
	
	public void setLocation(AgentLocation newLoc)
	{
		Location = newLoc;
	}
	
	public void setOrientation(double newOrient)
	{
		Orientation = newOrient % 360;
	}
	
	public void setBoundingRadius(int newRad)
	{
		if (newRad > 0)
			BoundingRadius = newRad;
	}
	
	public void setSoundRadius(int newSound)
	{
		if (newSound > 0)
			SoundRadius = newSound;
	}
	
	public void setColor(Color newColor)
	{
		color = newColor;
	}
}
