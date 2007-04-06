package agent.sensor;

import java.util.ArrayList;
import java.util.Iterator;

import obstacle.Obstacle;

import sim.Simulator;
import agent.Agent;
import baseobject.Bobject;
import baseobject.Flag;
import config.ConfigBobject;

public class Cone extends SensorModule{


	static final double radToDegConvert = 57.298;
	
	
	public Cone( ConfigBobject config )
	{
		super ( config );
		arcAngle = config.getSensorArcAngle();
		length = config.getSensorLength();
		halfAngle = arcAngle / 2.0;
		
	}

	
	private boolean inAngle( Agent a, double x, double y, double b)
	{
		boolean isIn = false;
		double aX = a.getLocation().getX();
		double aY = a.getLocation().getY();
		double aT = a.getLocation().getTheta();
		double halfArcLength = length * Math.tan(halfAngle) + b;
		double xOffSet = aX - x;
		double yOffSet = aY - y;
		if (xOffSet == 0)
			xOffSet = .0001;
		double dist = Math.hypot(xOffSet, yOffSet);
		
		double newHalfAngle = Math.asin(halfArcLength/dist)+aT/5;
		newHalfAngle = Math.toDegrees(newHalfAngle);
		
		double angle = Math.atan(yOffSet/xOffSet);
		
		angle = Math.toDegrees(angle);
		if ( aX <= x)
		{
			if ( aY >= y)
			{
				//quad 1
				angle = Math.abs(angle);
				if((Math.abs(aT - angle) <= newHalfAngle) ||
						(Math.abs(aT - (360 + angle)) <= newHalfAngle))
				{
					System.out.println("Quad 1 : " + (int)angle + "  Heading : " + (int)aT);
					isIn = true;
				}
			}
			else
			{
				//quad 4
				angle = 360 - angle;
				if((Math.abs(aT - angle) <= newHalfAngle) ||
						(Math.abs((aT + 360) - angle) <= newHalfAngle))
				{
					System.out.println("Quad 4 : " + (int)angle + "  Heading : " + (int)aT);
					isIn = true;
				}
			}
		}
		else
		{
			if (aY >= y)
			{
				//quad 2
				angle = 180 - angle;
				if(Math.abs(aT - angle) <= newHalfAngle)
				{
					System.out.println("Quad 2 : " + (int)angle + "  Heading : " + (int)aT);
					isIn = true;
				}
			}
			else
			{
				//quad 3
				angle = Math.abs(angle);
				angle = 180 + angle;
				if(Math.abs(aT - angle) <= newHalfAngle)
				{
					System.out.println("Quad 3 : " + (int)angle + "  Heading : " + (int)aT);
					isIn = true;
				}
			}
		}
		return isIn;
	}
	
    public ArrayList<Agent> getSightAgents(Agent a)
    {
    	ArrayList<Agent> temp = null;
    	Iterator<Bobject> iter = Simulator.objectIterator();
    	temp = new ArrayList<Agent>();
    	double aX = a.getLocation().getX();
    	double aY = a.getLocation().getY();
    	
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isAgent())
    		{
    			double bX = b.getLocation().getX();
    	    	double bY = b.getLocation().getY();
    	    	double bB = b.getBoundingRadius();
    			double dist = Math.hypot(aX-bX, aY-bY);
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() >= dist &&
    				a.getObjectID() != bB )
    			{
    				
    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				if(inAngle(a,bX,bY,bB)) 
    				{
    					temp.add((Agent)b);
    				}
    			}
    		}
    	}
    	return temp;
    }
    
    public ArrayList<Obstacle> getSightObstacles(Agent a)
    {
    	ArrayList<Obstacle> temp = null;
    	Iterator<Bobject> iter = Simulator.objectIterator();
    	temp = new ArrayList<Obstacle>();
    	double aX = a.getLocation().getX();
    	double aY = a.getLocation().getY();
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isObstacle())
    		{
    			double bX = b.getLocation().getX();
    	    	double bY = b.getLocation().getY();
    	    	double bB = b.getBoundingRadius();
    			double dist = Math.hypot(aX-bX, aY-bY);
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() >= dist &&
    				a.getObjectID() != bB )
    			{
//    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				if(inAngle(a,bX,bY,bB)) 
    				{
    					temp.add((Obstacle)b);
    				}
    			}
    		}

    	}
    	return temp;
    }
    
    public ArrayList<Flag> getSightFlags(Agent a)
    {
    	ArrayList<Flag> temp = null;
    	Iterator<Bobject> iter = Simulator.objectIterator();
    	temp = new ArrayList<Flag>();
    	double aX = a.getLocation().getX();
    	double aY = a.getLocation().getY();
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isFlag())
    		{
    			double bX = b.getLocation().getX();
    	    	double bY = b.getLocation().getY();
    	    	double bB = b.getBoundingRadius();
    			double dist = Math.hypot(aX-bX, aY-bY);
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() >= dist &&
    				a.getObjectID() != bB )
    			{
//    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				if(inAngle(a,bX,bY,bB)) 
    				{
    				//	System.out.println("Flag added to seen : X : " + (int)aX + " Y : " + (int)aY);
    					temp.add((Flag)b);
    				}
    			}
    		}
    	}
    	return temp;
    }
    
    public ArrayList<Agent> getHeardAgents(Agent a)
    {
    	ArrayList<Agent> temp = null;
    	Iterator<Bobject> iter = Simulator.objectIterator();
    	temp = new ArrayList<Agent>();
    	double aX = a.getLocation().getX();
    	double aY = a.getLocation().getY();
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isAgent())
    		{
    			double bX = b.getLocation().getX();
    	    	double bY = b.getLocation().getY();
    	    	double bB = b.getBoundingRadius();
    			double dist = Math.hypot(aX-bX, aY-bY);
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID() )
    			{
//    				b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				if(inAngle(a,bX,bY,bB)) 
    				{
    					temp.add((Agent)b);
    				}
    			}
    		}

    	}
    	return temp;
    }
}
