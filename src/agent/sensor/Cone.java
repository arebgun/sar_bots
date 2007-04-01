package agent.sensor;

import java.util.ArrayList;
import java.util.Iterator;

import sim.Simulator;
import agent.Agent;
import baseobject.Bobject;
import baseobject.Flag;
import baseobject.Obstacle;
import config.ConfigBobject;

public class Cone extends SensorModule{


	static final double radToDegConvert = 57.298;
	
	public Cone( ConfigBobject config )
	{
		super ( config );
		arcAngle = config.getSensorArcAngle();
		length = config.getSensorLength();		
	}
	
    public ArrayList<Agent> getSightAgents(Agent a)
    {
    	ArrayList<Agent> temp = null;
    	Iterator<Bobject> iter = Simulator.objectIterator();
    	temp = new ArrayList<Agent>();
    	double aX = a.getLocation().getX();
    	double aY = a.getLocation().getY();
    	double aT = a.getLocation().getTheta();
    	
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isAgent())
    		{
    			double bX = b.getLocation().getX();
    	    	double bY = b.getLocation().getY();
    			double dist = Math.hypot(aX-bX, aY-bY);
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID() )
    			{
    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				//TODO: Add code for determinig if an agent is in the viewing cone (by angle)
    				double xOffSet = aX - bX;
    				double yOffSet = aY - bY;
    				if (xOffSet == 0)
    					xOffSet = .01;
    				double coneAngle = Math.atan(yOffSet/xOffSet)*radToDegConvert;
    				
    		/*		if(coneAngle < 0) {
    					if(bY > aY)
    						coneAngle += 180;
    					else
    						coneAngle += 360;
    				}
    				else if(coneAngle > 0) {
    					if(bY < aY)
    						coneAngle += 180;
    				}
    				///// radians???
    				coneAngle = coneAngle % 360;*/
    				if(Math.abs(aT-coneAngle) <= arcAngle/2) {
    					temp.add((Agent)b);
    				}
    				
    				
    				
    				
    				
    				
    				
    				//if b is in the viewing cone then add it to the sight array
    				temp.add((Agent)b);
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
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isObstacle())
    		{
    			double dist = Math.sqrt(a.getLocation().getX() * b.getLocation().getX()+
    				a.getLocation().getY() * b.getLocation().getY());
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID() )
    			{
    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				//TODO: Add code for determinig if an agent is in the viewing cone (by angle)
    				
    				//if b is in the viewing cone then add it to the sight array
    				temp.add((Obstacle)b);
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
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isAgent())
    		{
    			double dist = Math.sqrt(a.getLocation().getX() * b.getLocation().getX()+
    				a.getLocation().getY() * b.getLocation().getY());
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID() )
    			{
    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				//TODO: Add code for determinig if an agent is in the viewing cone (by angle)
    				
    				//if b is in the viewing cone then add it to the sight array
    				temp.add((Flag)b);
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
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		if (b.isAgent())
    		{
    			double dist = Math.sqrt(a.getLocation().getX() * b.getLocation().getX()+
    				a.getLocation().getY() * b.getLocation().getY());
    			//is any part of agent b within the length of my viewing cone?
    			if (length + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID() )
    			{
    				//b is within the length of my viewing cone, now is it within
    				//my viewing arc?
    				//TODO: Add code for determinig if an agent is in the viewing cone (by angle)
    				
    				//if b is in the viewing cone then add it to the sight array
    				temp.add((Agent)b);
    			}
    		}

    	}
    	return temp;
    }
}
