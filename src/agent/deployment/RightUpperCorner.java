package agent.deployment;

/*
 * Class Name:    agent.deployment.RightUpperCorner
 * Last Modified: 5/2/2006 9:24
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
import config.*;
import env.Environment;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Deployment strategy that puts all agent in right upper corner of the world.
 */
public class RightUpperCorner extends DeploymentStrategy
{
    private static Random rand = null;

    public RightUpperCorner( ConfigAgent config )
    {
        super( config );
        if ( rand == null ) { rand = new Random( agentConfig.getDeploymentSeed() ); }
    }

    /**
     * Return next feasible agent location. This deployment strategy will
     * return locations that are in the right upper corner of the world.
     *
     * @param id unique agent id
     * @return AgentLocation object for the agent specified by id
     */
    public AgentLocation getNextLocation( int id )
    {
        Area unoccupied    = Environment.unoccupiedArea();
        Rectangle2D bounds = unoccupied.getBounds2D();
        double x           =   -1;
        double y           =   -1;
        int limit          = 1000;
        boolean found      = false;

        while ( !found && --limit > 0 )
        {
            x = rand.nextDouble() * bounds.getWidth()  * 0.2 + bounds.getWidth() * 0.8;
            y = rand.nextDouble() * bounds.getHeight() * 0.2;

            if ( unoccupied.contains( x, y ) ) { found = true; }
        }

        if ( !found ) { throw new IllegalStateException( "unable to deploy agent #" + id ); }

        return new AgentLocation( x, y, rand.nextGaussian() );
    }
}
