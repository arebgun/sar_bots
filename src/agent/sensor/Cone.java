
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


	protected double topLength = 0;
	
	public Cone( ConfigBobject config )
	{
		super ( config );
		arcAngle = config.getSensorArcAngle();
		length = config.getSensorLength();
		halfAngle = arcAngle / 2.0;
		topLength = 2.0 * length * Math.sin(Math.toRadians(halfAngle));
		
	}
	//angle is the angle of the arc and must be in radians and is the heading plus the angle into the sight cone
	private boolean lineCircle(double startX, double startY, double angle, double circleX, double circleY, double radius)
	{
		boolean isIn = false;
		//end of the ray from the origin
		double endX = startX + length * Math.cos(angle);
		double endY = startY - length * Math.sin(angle);
		//radius of the circle squared
		double radiusSquared = radius * radius;
		
		//A B and C for the determinant
		double A = endX * endX + endY * endY;
		double B = 2 * (endX * endX * (startX - circleX) + endY * endY * (startY - circleY));
		double C = ((startX - circleX) * (startX - circleX) + (startY - circleY) * (startY - circleY)) - radiusSquared;
		
		//the determinant
		double determinant = B * B - 4 * A * C;
		if (determinant > 0)
			isIn = true;
		return isIn;
	}
	
	private boolean inAngle( Agent a, double circleX, double circleY, double radius)
	{
		boolean isIn = false;
		double diameterMinusOne = 2.0 * radius - 1.0;
		double startX = a.getLocation().getX();
		double startY = a.getLocation().getY();
		double heading = a.getLocation().getTheta();
		double startArc = heading - halfAngle;
		
		//angle increment is the arcsin of diamterMinusOne/length
		double incrementAngle = Math.asin(diameterMinusOne/length);
		incrementAngle = Math.toDegrees(incrementAngle);
		
		//iterations are the number of steps to take within the sight cone + 1 for the initial edge case
		int iterations = (int)(topLength/diameterMinusOne);
		//steps is the number iterations we have done so far
		int steps = 0;
		
		while (!isIn && steps <= iterations)
		{
			double newAngle = startArc + steps * incrementAngle;
			newAngle = Math.toRadians(newAngle);
			
			isIn = lineCircle(startX,startY,newAngle,circleX,circleY,radius);
			steps++;
		}
		
		//if we still haven't hit, we need to check the far angle of the of the sight cone
		if (!isIn)
		{
			double newAngle = startArc + arcAngle;
			newAngle = Math.toRadians(newAngle);
			
			isIn = lineCircle(startX, startY, newAngle, circleX, circleY, radius);
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
