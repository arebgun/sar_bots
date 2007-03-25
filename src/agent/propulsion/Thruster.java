package agent.propulsion;

/*
 * Class Name:    agent.propulsion.Thruster
 * Last Modified: 4/2/2006 3:1
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
import config.ConfigBobject;

import static java.lang.Math.*;

/**
 * Simple thruster that can turn agent instantly and move the
 * agent with constant speed (no acceleration, inertia, etc.).
 */
public class Thruster extends PropulsionModule
{
    public Thruster( ConfigBobject config )
    {
        super( config );
    }

    /**
     * Moves the agent from current to goal location using a
     * straight line trajectory. If the distance between current and
     * goal location exceeds the maximum speed, then the new goal
     * location is set to current location + maximum speed. This is also
     * the result returned by tjis method.
     *
     * @param location current location of the agent
     * @param goal goal location of the agent
     * @return new agent location after moving
     */
    public AgentLocation move( AgentLocation location, AgentLocation goal )
    {
        double curX     = location.getX();
        double curY     = location.getY();
        double goalX    = goal.getX();
        double goalY    = goal.getY();
        double goalDist = hypot( goalX - curX, goalY - curY );

        if ( goalDist > maxSpeed )
        {
            double theta = atan2( goalY - curY, goalX - curX );
            goalX = curX + maxSpeed * cos( theta );
            goalY = curY + maxSpeed * sin( theta );
        }

        return new AgentLocation( goalX, goalY, goal.getTheta() );
    }
}
