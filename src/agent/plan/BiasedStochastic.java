package agent.plan;

/*
 * Class Name:    agent.plan.BiasedStochastic
 * Last Modified: 5/3/2006 9:2
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

/**
 * Author: Anton Rebgun
 * Date:   May 3, 2006
 * Time:   9:02:18 PM
 */

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.*;
import static java.lang.Math.*;
import java.util.Random;

public class BiasedStochastic extends PlanModule
{
    private static Random rand = null;

    public BiasedStochastic( ConfigAgent config )
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
        double curTheta    = location.getTheta();

        double newX     = -1;
        double newY     = -1;
        double newTheta = location.getTheta();

        int limit      = 1000;
        boolean placed = false;

        while ( !placed && --limit > 0 )
        {
            if ( Math.random() < 0.7 )
            {
                if ( curTheta < PI / 4 )
                {
                    newX = curX + ( bounds.getWidth() / 2 ) * rand.nextDouble();
                    newY = bounds.getY() + ( curY - bounds.getY() ) * rand.nextDouble();
                }
                else if ( curTheta < PI / 2 )
                {
                    newX = bounds.getX() + ( bounds.getWidth() / 2 ) * rand.nextDouble();
                    newY = bounds.getY() + ( curY - bounds.getY() ) * rand.nextDouble();
                }
                else if ( curTheta < 3 * PI / 2 )
                {
                    newX = bounds.getX() + ( bounds.getWidth() / 2 ) * rand.nextDouble();
                    newY = curY + ( bounds.getY() + bounds.getHeight() - curY ) * rand.nextDouble();
                }
                else
                {
                    newX = ( bounds.getX() + bounds.getWidth() ) / 2 + ( bounds.getWidth() / 2 ) * rand.nextDouble();
                    newY = curY + ( bounds.getY() + bounds.getHeight() - curY ) * rand.nextDouble();
                }
            }
            else
            {
                newX = bounds.getX() + bounds.getWidth()  * rand.nextDouble();
                newY = bounds.getY() + bounds.getHeight() * rand.nextDouble();
            }

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
            newTheta = curTheta + PI;
        }

        return new AgentLocation( newX, newY, newTheta );
    }
}
