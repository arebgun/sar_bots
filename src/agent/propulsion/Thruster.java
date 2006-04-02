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
import config.ConfigAgent;

import static java.lang.Math.*;

public class Thruster extends PropulsionModule
{
    public Thruster( ConfigAgent config )
    {
        super( config );
    }

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
