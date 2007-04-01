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
    private double getAngle(double arc, double theta, double curY, double newY)
    {
    	double newTheta = theta;
    	newTheta = rand.nextDouble() * arc + (theta - arc/2);
    	newTheta *= 57.298;
    	if(newTheta < 0) {
			if(curY > newY)
				newTheta += 180;
			else
				newTheta += 360;
		}
		else if(newTheta > 0) {
			if(curY < newY)
				newTheta += 180;
		}
		if (Math.abs(newTheta - theta) > arc/2)
			newTheta = 360 - newTheta;
		return Math.toRadians(newTheta);
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
        double curTheta    = a.getLocation().getTheta() ;
        
        double newX     = -1;
        double newY     = -1;
        double newTheta = a.getLocation().getTheta() ;
        
        double dist = 0;
        double bound = 0;

        int range = a.getSoundRadius();
        
        int limit      = 1000;
        boolean found = false;
        boolean good = true;
        double arc = a.sensorSight.getArcAngle();
        
        //rand.setSeed(System.currentTimeMillis());
        while (!found && --limit > 0)
        {
        	double arcX = Math.sin(getAngle(arc, newTheta, curY, newY));
            double arcY = Math.cos(getAngle(arc, newTheta, curY, newY));
        	newX = curX + range * arcX;
        	newY = curY + range * arcY;
  //      	if (a.getObjectID() == 0)
    //    		System.out.println("agent " + range + "  " +(int)curX + "  " + (int)curY + "  " + (int)newX + "  " + (int)newY + "  " + arcX + "  " + arcY);
        	
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
        	
//        	check against agents heard
        	Iterator<Agent> heard = a.getAgentsHeard();
        	while ( heard.hasNext() && good)
        	{
        		Agent b = heard.next();
        		if (a.getObjectID() != b.getObjectID())
        		{
        			dist = Math.hypot((newX - b.getLocation().getX()), (newY - b.getLocation().getY()));
        			bound = a.getBoundingRadius() + b.getBoundingRadius();
        			if ( bound >= dist)
        				good = false;
        		}
        	}
        	found = good;
        	
        }
        	
       	//check to make sure we are within the borders
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
            newTheta += PI/6;
        }
        double xOffSet = newX - curX;
		double yOffSet = newY - curY;
		if (xOffSet == 0)
			xOffSet = .01;
		double coneAngle = Math.atan(yOffSet/xOffSet)*57.298;

			
		
		if(coneAngle < 0) {
			if(curY > newY)
				coneAngle += 180;
			else
				coneAngle += 360;
		}
		else if(coneAngle > 0) {
			if(curY < newY)
				coneAngle += 180;
		}
		if (Math.abs(coneAngle - curTheta) > arc/2)
			coneAngle = 360 - coneAngle;
		
		if (a.getObjectID() == 0)
			System.out.println("heading " + (int)coneAngle + " previous headig " + (int)newTheta);
		
		return new AgentLocation( newX, newY, coneAngle );
    }
    
 
}
