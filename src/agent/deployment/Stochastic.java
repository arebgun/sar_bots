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

import agent.AgentLocation;
import config.ConfigAgent;
import env.Environment;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Stochastic deployment strategy will randomly place MAVs throughout the world.
 */
public class Stochastic extends DeploymentStrategy
{
    private static Random rand = null;

    public Stochastic( ConfigAgent config )
    {
        super( config );
        if ( rand == null ) { rand = new Random( agentConfig.getDeploymentSeed() ); }
    }

    /**
     * Return next feasible agent location. This deployment strategy will
     * return locations that random points in the world.
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
            x = rand.nextDouble() * bounds.getWidth();
            y = rand.nextDouble() * bounds.getHeight();

            if ( unoccupied.contains( x, y ) ) { found = true; }
        }

        if ( !found ) { throw new IllegalStateException( "unable to deploy agent #" + id ); }

        return new AgentLocation( x, y, rand.nextGaussian() );
    }
}
