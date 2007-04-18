
 package agent.sensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Polygon;
import obstacle.*;
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
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// code for determining the line circle intersection for cone to bounding radius
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
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

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// collision detection using line polygon (line-line) intersection
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/*
	 * lines are as follows Line A =(x1,y1) -> (x2,y2) and Line B = (x3,y3) -> (x4,y4)
	 * Line A intersects Line B iff one of the intersection points is on between
	 * (x1,y1) -> (x2,y2) on Line A
	 */
	private boolean lineLine(double x1, double y1, double x2, double y2,
							 double x3, double y3, double x4, double y4)
	{
		boolean isIn = false;
		double lowerX = x1;
		double lowerY = y1;
		double upperX = x2;
		double upperY = y2;
		
		//make sure lower values are the smallest value for each point
		if (lowerX > upperX)
		{
			double temp = upperX;
			upperX = lowerX;
			lowerX = temp;
		}
		if(lowerY > upperY)
		{
			double temp = upperY;
			upperY = lowerY;
			lowerY = temp;
		}
		
		double denom = (y4 - y3)*(x2 - x1) - (x4 - x3)*(y2 - y1);
		if (denom == 0)
			return isIn;
		double ua = ((x4 - x3)*(y1 - y3) - (y4 - y3)*(x1 - x3))/denom;
		double xa = x1 + ua*(x2-x1);
		double ya = y1 + ua*(y2-y1);
		if (lowerX <= xa && xa <= upperX && lowerY <= ya && ya <= upperY)
			isIn = true;
		
		if (!isIn)
		{
			double ub = ((x2 - x1)*(y1 - y3) - (y2 - y1)*(x1 - x3))/denom;
			double xb = x3 + ub*(x4-x3);
			double yb = y3 + ub*(y4-y3);
			if (lowerX <= xb && xb <= upperX && lowerY <= yb && yb <= upperY)
				isIn = true;
		}
		
		return isIn;
	}
	
	private boolean inAngle(Agent a, Polygon p)
	{
		boolean isIn = false;
		
		int points = p.npoints;
		double startX = a.getLocation().getX();
		double startY = a.getLocation().getY();
		double heading = a.getLocation().getTheta();
		
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
		
		//check each line in the polygon for collision until either
		//we have a collision or we run out of lines in the polygon
		double dist = 0; //this is the distance of each line segment of the polygon
		double incrementAngle = 0; //this is the angle to increment the ray trace to
		int n = 0;
		while (!isIn && n < (points - 1))
		{
			dist = Math.hypot((p.xpoints[n] - p.xpoints[(n+1)]), (p.ypoints[n] - p.ypoints[(n-1)]));
			incrementAngle = Math.asin((dist - 1)/length);
			incrementAngle *= RADTODEG;
			int iterations = (int)(topLength/(dist - 1));
			int steps = 0;
		
			while(!isIn && steps <= iterations)
			{
				double newAngle = startArc + steps * incrementAngle;
				newAngle *= DEGTORAD;
				double endX = startX + length * Math.cos(newAngle);
				double endY = startX + length * Math.sin(newAngle);
				int x3 = p.xpoints[n];
				int y3 = p.ypoints[n];
				int x4 = p.xpoints[(n+1)];
				int y4 = p.ypoints[(n+1)];
				isIn = lineLine(startX,startY,endX,endY,x3,y3,x4,y4);
				steps++;
			}
			if(!isIn)
			{
				double newAngle = startArc + arcAngle;
				newAngle *= DEGTORAD;
				double endX = startX + length * Math.cos(newAngle);
				double endY = startY + length * Math.sin(newAngle);
				int x3 = p.xpoints[n];
				int y3 = p.ypoints[n];
				int x4 = p.xpoints[0];
				int y4 = p.ypoints[0];
				isIn = lineLine(startX,startY,endX,endY,x3,y3,x4,y4);
			}
			n++;
		}
		return isIn;	
	}
	
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// collision detection for agent seeing agents using a cone
//*******************************************************************************
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
    			if (x.getIsAlive())
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

//  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//   collision detection for agent seeing obstacles using a cone
//  *******************************************************************************   
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
    				if (b.isCircle() && inAngle(a,bX,bY,bR)) 
    				{
    					temp.add((Obstacle)b);
    				}
    				else if (b.isPolygon())
    				{
    					PolygonObstacle p = (PolygonObstacle)b;
    					if(inAngle(a,p.getPolygon()))
    						temp.add((Obstacle)b);
    				}
    			}
    		}

    	}
    	return temp;
    }

//  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//   collision detection for agent seeing flags using a cone
//  *******************************************************************************   
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

//  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//   collision detection for agent hearing obstacles using a cone
//  *******************************************************************************    
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
