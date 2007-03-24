package agent.sensor;

/*
 * Class Name:    agent.sensor.Circle
 * Last Modified: 4/30/2006 4:3
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import agent.Agent;
import config.ConfigBobject;
import java.lang.Math;
import baseobject.*;
import java.util.ArrayList;
import java.util.Iterator;

import sim.Simulator;
/**
 * Implements a circular shaped agent sensor.
 *
 */
public class Circle extends SensorModule
{
	protected double radius;
	
    public Circle( ConfigBobject config )
    {
        super( config );
        radius      = objectConfig.getSensorRadius();
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
    			int dist = (int)Math.sqrt((double)a.getLocation().getX() * 
    				(double)b.getLocation().getX()+
    				(double)a.getLocation().getY() * 
    				(double)b.getLocation().getY());
    			if (a.getBoundingRadius() + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID())
    			temp.add((Agent)b);
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
    			int dist = (int)Math.sqrt((double)a.getLocation().getX() * 
    				(double)b.getLocation().getX()+
    				(double)a.getLocation().getY() * 
    				(double)b.getLocation().getY());
    			if (a.getBoundingRadius() + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID())
    			temp.add((Agent)b);
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
    			int dist = (int)Math.sqrt((double)a.getLocation().getX() * 
    				(double)b.getLocation().getX()+
    				(double)a.getLocation().getY() * 
    				(double)b.getLocation().getY());
    			if (a.getBoundingRadius() + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID())
    			temp.add((Obstacle)b);
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
    		if (b.isFlag())
    		{
    			int dist = (int)Math.sqrt((double)a.getLocation().getX() * 
    				(double)b.getLocation().getX()+
    				(double)a.getLocation().getY() * 
    				(double)b.getLocation().getY());
    			if (a.getBoundingRadius() + b.getBoundingRadius() <= dist &&
    				a.getObjectID() != b.getObjectID())
    			temp.add((Flag)b);
    		}

    	}
    	return temp;
    }
}
