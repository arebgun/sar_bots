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

	protected double arcAngle;
	protected double length;
	
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
