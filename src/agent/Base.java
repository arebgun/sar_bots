package agent;

import config.ConfigBobject;
import baseobject.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import sim.Simulator;
import messageBoard.MessageBoard;

public class Base extends Agent 
{
	private int colorCount = 100;
	private int radiusCount = 1;
	private boolean up = true;
	public Base( ConfigBobject config) throws Exception
    {
		 super(config);
		 location = config.objectLocation();
		 MessageBoard mb = Simulator.teamBoards.get(teamID);
		 mb.setBaseLocation(location);
		 myType = agentType.BASE;
	}
	public void pickUpFlag(Flag flag){}
	public void dropFlag() {}
	public void update() 
	{
		 if (!isAlive)
		 {
			 health += 100000000;
			 isAlive = true;
		 }
		 
	}
	public void draw (Graphics2D g2, boolean sight, boolean hearing)
	{
		if (up && radiusCount <= 100)
		{
			up = true;
			radiusCount += 10;
		}
		else
		{
			up = false;
			radiusCount -= 10;
			if (radiusCount < 12)
				up = true;
		}
		
		double radius = boundingRadius * radiusCount / colorCount;
		double x = location.getX() - radius;
		double y = location.getY() - radius;
		g2.setColor(this.color);
		g2.fill(new Ellipse2D.Double(x,y,(2*radius),(2*radius)));
	}
	 
}

