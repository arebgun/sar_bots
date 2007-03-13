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

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.*;
import static java.lang.Math.*;
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
    public Stochastic( ConfigAgent config )
    {
        super( config );

        if ( rand == null )
        {
            rand = new Random( agentConfig.getPlanSeed() );
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
// TODO: remove Area from getGoalLocation (used for sensor's range and a silly navigation computation)
    public AgentLocation getGoalLocation( AgentLocation location, Area sensorView )
    {
        Rectangle2D bounds = sensorView.getBounds();
        double curX        = location.getX();
        double curY        = location.getY();

        double newX     = -1;
        double newY     = -1;
        double newTheta = location.getTheta();

        int limit      = 1000;
        boolean placed = false;

        while ( !placed && --limit > 0 )
        {
            newX = bounds.getX() + bounds.getWidth()  * rand.nextDouble();
            newY = bounds.getY() + bounds.getHeight() * rand.nextDouble();

            if ( sensorView.contains( newX, newY ) )
            {
                newTheta = atan2( newY - curY, newX - curX );
                // the reachability of the path is determined by creating a narrow rectangular box anchored at the
                // current agent's location, rotated to touch the currently chosen goal point.
                Area path = new Area( new Rectangle2D.Double( curX, curY, hypot( newX - curX, newY - curY ), .5/*wingSpan/2*/ ) );
                path.transform( AffineTransform.getRotateInstance( newTheta, curX, curY ) );
                path.intersect( sensorView );

                if ( !path.isEmpty() && path.isSingular() ) { placed = true; }
            }
        }

        if ( !placed )
        {
            newX     = curX;
            newY     = curY;
            newTheta = location.getTheta() + PI/6;
        }

        return new AgentLocation( newX, newY, newTheta );
    }
}
