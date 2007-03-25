package agent.deployment;

/*
 * Class Name:    agent.deployment.Stochastic
 * Last Modified: 4/2/2006 3:5
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
import agent.AgentLocation;
import baseobject.Bobject;
import config.ConfigBobject;
import env.Environment;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Random;

import sim.Simulator;

/**
 * Stochastic deployment strategy will randomly place MAVs throughout the world.
 */
public class Stochastic extends DeploymentStrategy
{
    private static Random rand = null;

    public Stochastic( ConfigBobject config )
    {
        super( config );
        if ( rand == null ) { rand = new Random( objectConfig.getDeploymentSeed() ); }
    }

    /**
     * Return next feasible agent location. This deployment strategy will
     * return locations that random points in the world.
     *
     * @param id unique agent id
     * @return AgentLocation object for the agent specified by id
     */
    public AgentLocation getNextLocation( Agent a )
    {
    	Rectangle2D world = Environment.groundShape();
        double x           =   -1;
        double y           =   -1;
        int limit          = 1000;
        boolean found      = false;

        while ( !found && --limit > 0 )
        {
        	//find a random spot on the map
            x = rand.nextDouble() * world.getMaxX();
            y = rand.nextDouble() * world.getMaxY();
            //make sure nothing is in that spot too
           
            boolean good = true;
            Iterator<Bobject> iter = Simulator.objectIterator();
        	while ( iter.hasNext())
        	{
        		Bobject b = iter.next();
        		double dist = Math.sqrt(x * b.getLocation().getX() +
        				y * b.getLocation().getY());
        		//if object b is not within the bounding radius of a, do nothing.
        		//if object b is within the bounding radius of a, then good = false
        		//and this location is not a good one to start with
        		if (a.getBoundingRadius() + b.getBoundingRadius() <= dist &&
        				a.getObjectID() != b.getObjectID())
        			;
        		else
        			good = false;
        	}
        	found = good; 
        }

        if ( !found ) { throw new IllegalStateException( "unable to deploy agent #" + a.getObjectID() ); }

        return new AgentLocation( x, y, rand.nextGaussian() );
    }
}
