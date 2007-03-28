package agent.plan;

/*
 * Class Name:    agent.plan.Stochastic
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

import agent.*;
import baseobject.*;
import config.ConfigBobject;
import env.Environment;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.*;

import java.util.Iterator;
import java.util.Random;

/**
 * Implements a random path planner.
 */
public class Stochastic extends PlanModule
{
    private static Random rand = null;

    /**
     * Majority of the work is performed by the super class; This constructor just initializes
     * the random number generator based on the "planSeed" in the agent config file.
     * @param config Agent configuration object provided by the Simulator
     */
    public Stochastic( ConfigBobject config )
    {
        super( config );
        //System.currentTimeMillis()
        if ( rand == null )
        {
            rand = new Random( objectConfig.getPlanSeed() );
        }
    }

    /**
     * For stochastic navigation, the UAV agent selects a random point inside its
     * sensor view, and then performs a reachability test to ensure that it can
     * reach that location.  If there is a continuous path from the present location
     * to the target, the agent will return that point as the goal location.  Otherwise,
     * another target will be selected.
     *
     * An error check is performed to make sure that an infinite loop does not occur
     * in the special case when there are no reachable points within the sensor view.
     * This case may occur when the UAV is at the world boundary, facing outward.  The
     * behavior is to rotate the agent in 30 degree increments.
     *
     * @param location Agent's current location.
     * @param sensorView Agent's current sensor view.
     * @return The goal location selected by the planner.  Note that this does not mean
     * that the agent will end up in that location on the next step; the next actual location
     * will depend on the @see Propulsion module.
     */
    public AgentLocation getGoalLocation( Agent a )
    {
        double curX        = a.getLocation().getX();
        double curY        = a.getLocation().getY();
        
        double newX     = -1;
        double newY     = -1;
        double newTheta = a.getLocation().getTheta();
        
        double dist = 0;
        double bound = 0;

        int range = a.getSoundRadius();
        
        int limit      = 1000;
        boolean found = false;
        boolean good = true;
        //rand.setSeed(System.currentTimeMillis());
        while (!found && --limit > 0)
        {
        	newX = curX + (range * Math.sin(rand.nextDouble()*360));
        	newY = curY + (range * Math.cos(rand.nextDouble()*360));
        	
        	good = true;
        	
        	//check against agents
        	Iterator<Agent> iter = a.getAgentsSeen();
        	while ( iter.hasNext() && good)
        	{
        		Agent b = iter.next();
        		if (a.getObjectID() != b.getObjectID())
        		{
        			dist = Math.hypot((newX - b.getLocation().getX()), (newY - b.getLocation().getY()));
        			bound = a.getBoundingRadius() + b.getBoundingRadius();
        			if ( bound >= dist)
        				good = false;
        		}
        	}
        	found = good;
        	
        	//check against obstacles
        	Iterator<Obstacle> obs = a.getObstaclesSeen();
        	while ( obs.hasNext() && good)
        	{
        		Obstacle o = obs.next();
        		dist = Math.hypot((newX - o.getLocation().getX()),(newY - o.getLocation().getY()));
        		bound = a.getBoundingRadius() + o.getBoundingRadius();
        		if (bound >= dist)
        			good = false;
        	}
        	found = good;
        }
        	
       	//check to make sure we are withing the borders
        // only need to check if the location is a good one so far
        if (good)
       	{
        	Rectangle2D world = Environment.groundShape();
        	if (newX > 15 && newX < (world.getMaxX()-15) && newY > 15 && newY < (world.getMaxY()-15))
        		found = true;
        	else
        		found = false;  	
        }

        if ( !found )
        {
            newX     = curX;
            newY     = curY;
            newTheta = a.getLocation().getTheta() + PI/6;
        }
       return new AgentLocation( newX, newY, newTheta );
    }
}
