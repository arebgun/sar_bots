package agent.plan;

/*
 * Class Name:    agent.plan.Spiral
 * Last Modified: 5/2/2006 10:53
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
 * Date:   May 2, 2006
 * Time:   10:53:38 PM
 */
import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;
// TODO: remove Area from Spiral.java (probably remove the entire class, no need for it)
public class Spiral extends PlanModule
{
    private static Random rand  = null;
    private ArrayList<AgentLocation> goals = new ArrayList<AgentLocation>();

    public Spiral( ConfigAgent config )
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

        if ( !goals.isEmpty() )
        {
            AgentLocation p = goals.get( 0 );

            newX = p.getX();
            newY = p.getY();
            newTheta = p.getTheta();

            goals.remove( 0 );
        }
        else
        {
            while ( !placed && --limit > 0 )
            {
                double avgR = ( bounds.getWidth() + bounds.getHeight() ) / 2 ;

                for ( int i = 0; i < 200; i++ )
                {
                    double theta = i * PI / 50;
                    double r = ( 1 + .5 * rand.nextGaussian() ) * avgR;
                    newX = (int) round( r * cos( theta ) + curX ) + 5;
                    newY = (int) round( r * sin( theta ) + curY ) + 5;
                    newTheta = atan2( newY - curY, newX - curX );

                    if ( sensorView.contains( newX, newY ) )
                    {
                        AgentLocation goal  = new AgentLocation( newX, newY, newTheta );
                        Area path = new Area( new Rectangle2D.Double( curX, curY, hypot( newX - curX, newY - curY ), .5/*wingSpan/2*/ ) );
                        path.transform( AffineTransform.getRotateInstance( newTheta, curX, curY ) );
                        path.intersect( sensorView );
                        goals.add( goal );
                    }
                }

                newX = goals.get( 0 ).getX();
                newY = goals.get( 0 ).getY();
                goals.remove( 0 );

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
        }

        return new AgentLocation( newX, newY, newTheta );
    }
}
