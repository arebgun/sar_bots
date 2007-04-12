package baseobject;
import sim.Simulator;

import config.ConfigBobject;
import java.awt.*;
import java.awt.geom.Ellipse2D;

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
		color = config.getObjectColor();
		boundingRadius = config.getBoundingRadius();
		soundRadius = boundingRadius;
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
		isOwned = false;
		owner = 0;
	}
	
	public void setLocation()
	{
		if(this.isOwned)
			this.location = Simulator.getObjectByID(this.getOwner()).location;
	}
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(color);
		g2.fill(new Ellipse2D.Float((float)location.getX() - boundingRadius,
				(float)location.getY() - boundingRadius,
				2f * (float)boundingRadius,
				2f * (float)boundingRadius));
	}
}
