package agent.plan;

/*
 * Class Name:    agent.plan.Stochastic
 * Last Modified: 4/2/2006 2:57
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

public class Stochastic extends PlanModule
{
    private static Random rand = null;

    public Stochastic( ConfigAgent config )
    {
        super( config );

        if ( rand == null )
        {
            rand = new Random( agentConfig.getPlanSeed() );
        }
    }

    // TODO-dimzar-20060328: include a diagram and documentation for what you're doing here
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
            newTheta = location.getTheta() + PI;
        }

        return new AgentLocation( newX, newY, newTheta );
    }
}
