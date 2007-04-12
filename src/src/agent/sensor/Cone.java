
 package agent.sensor;

import java.util.ArrayList;
import java.util.Iterator;

import obstacle.Obstacle;
import env.Environment;
import sim.Simulator;
import agent.Agent;
import baseobject.Bobject;
import baseobject.Flag;
import config.ConfigBobject;

public class Cone extends SensorModule{


	protected double topLength = 0;
	private static final double DEGTORAD = .0174532925;
	private static final double RADTODEG = 57.29577951;
	
	public Cone( ConfigBobject config )
	{
		super ( config );
		arcAngle = config.getSensorArcAngle();
		length = config.getSensorLength();
		halfAngle = arcAngle / 2.0;
		topLength = 2.0 * length * Math.sin(halfAngle * DEGTORAD);
		
	}
	
	private boolean inFront(double startX, double startY, double endX, double endY, double heading, double x, double y)
	{
		heading *= 57.29577951;
		while (heading >= 360)
			heading -= 360;
		while (heading < 0)
			heading += 360;
		double atangent = Math.atan((startX-x)/(startY-y)) * 57.29577951;
		if (startX <= x)
		{
			if (startY <= y)
			{
				atangent = 90 - Math.abs(atangent);
				if (Math.abs(heading - atangent) <= halfAngle ||
					(Math.abs(heading - (atangent + 360)) <= halfAngle))
					return true;
			}
			else
			{
				atangent = 270 + Math.abs(atangent);
				if (Math.abs(heading - atangent) <= halfAngle ||
					(Math.abs((heading + 360) - atangent) <= halfAngle ))
					return true;
			}
		}
		else
		{
			if (startY <= y)
			{
				atangent = 90 + Math.abs(atangent);
				if (Math.abs(heading - atangent) <= halfAngle)
					return true;
			}
			else
			{
				atangent = Math.abs(atangent);
				atangent = 270 - atangent;
				if (Math.abs(heading - atangent) <= halfAngle)
					return true;
			}

		}
		return false;
	}
	private int sign(double x)
	{
		if (x < 0)
			return -1;
		else
			return 1;
	}
	private boolean lineCircle(double startX, double startY, double angle, double circleX, double circleY, double radius)
	{
		boolean isIn = false;
		startX -= circleX;
		startY -= circleY;
		
		double endX = startX + length * Math.cos(angle);
		double endY = startY + length * Math.sin(angle);
		
		double dX = endX - startX;
		double dY = endY - startY;
		double dist = length * length;
		double D = startX*endY - endX*startY;
		
		double det = radius * radius * dist - D * D;
		if (det > 0)
		{
			int x_p = (int)((D * dY + sign(dY) * dX * Math.sqrt(det))/dist);
			int y_p = (int)((-D * dX + Math.abs(dY) * Math.sqrt(det))/dist);
			isIn = (inFront(startX, startY, endX, endY, angle, x_p, y_p));
		}
		return isIn;
	}
	
	private boolean inAngle(Agent a, double circleX, double circleY, double radius)
	{
		boolean isIn = false;
		double startX = a.getLocation().getX();
		double startY = a.getLocation().getY();
		double heading = a.getLocation().getTheta();
		double diameterMinusOne = 2*radius-1;
		//System.out.println("In : sX : " + (int)startX + " sY : " + (int)startY + " A : " + (int)heading + " cX : " + (int)circleX + " cY : " + (int)circleY + " r : " + (int)radius );
		
		//transformation to a normal coordinate system

		if(heading >= 0 && heading < 90)
			heading = 360 - heading;
		else if (heading >= 90 && heading < 180)
			heading = 180 + (180 - heading);
		else if (heading >= 180 && heading < 270)
			heading = 180 - (heading - 180);
		else
			heading = 360 - heading;
		
		double startArc = heading - halfAngle;
		while(startArc < 360)
			startArc += 360;
		double incrementAngle = Math.asin(diameterMinusOne/length);
		incrementAngle *= RADTODEG;
		int iterations = (int)(topLength/diameterMinusOne);
		int steps = 0;
		
		while(!isIn && steps <= iterations)
		{
			double newAngle = startArc + steps * incrementAngle;
			newAngle *= DEGTORAD;
			isIn = lineCircle(startX,startY,newAngle,circleX,circleY,radius);
			steps++;
		}
		if(!isIn)
		{
			double newAngle = startArc + arcAngle;
			newAngle *= DEGTORAD;
			isIn = lineCircle(startX,startY,newAngle,circleX,circleY,radius);
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
    			Agent x = (Agent)b;
    			if (x.getHealth() > 0)
    			{
    				double bX = b.getLocation().getX();
    				double bY = b.getLocation().getY();
    				double bR = b.getBoundingRadius();
    				double dist = Math.hypot(aX-bX, aY-bY);
    				//is any part of agent b within the length of my viewing cone?
    				if (length + b.getBoundingRadius() >= dist &&
    						a.getObjectID() != b.getObjectID() )
    				{
    			//	System.out.println("bound : " + (length + b.getBoundingRadius()) + " dist " + dist);
    					//b is within the length of my viewing cone, now is it within
    					//my viewing arc?
    					if(inAngle(a,bX,bY,bR)) 
    					{
    						temp.add((Agent)b);
    					}
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
    	    	double bR = b.getBoundingRadius();
    			double dist = Math.hypot(aX-bX, aY-bY);
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() >= dist &&
    				a.getObjectID() != b.getObjectID() )
    			{
//    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				if(inAngle(a,bX,bY,bR)) 
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
    	    	double bR = b.getBoundingRadius();
    			double dist = Math.hypot(aX-bX, aY-bY);
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() >= dist &&
    				a.getObjectID() != b.getObjectID() )
    			{
//    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				if(inAngle(a,bX,bY,bR)) 
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
    			Agent x = (Agent)b;
    			if (x.getHealth() > 0)
    			{
    				double bX = b.getLocation().getX();
    				double bY = b.getLocation().getY();
    				double bR = b.getBoundingRadius();
    				double dist = Math.hypot(aX-bX, aY-bY);
    				//is any part of agent b within the length of my viewing cone?
    				if (length + b.getBoundingRadius() <= dist &&
    						a.getObjectID() != b.getObjectID() )
    				{
//    					b is within the length of my viewing cone, now is it within
    					//my viewing arc?
    					if(inAngle(a,bX,bY,bR)) 
    					{
    						temp.add((Agent)b);
    					}
    				}
    			}
    		}

    	}
    	return temp;
    }
}
