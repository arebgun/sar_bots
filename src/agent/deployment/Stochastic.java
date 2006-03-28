package agent.deployment;

import agent.AgentLocation;
import config.ConfigAgent;
import env.Environment;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *         Date:   Feb 24, 2006
 *         Time:   8:51:45 PM
 */
public class Stochastic extends DeploymentStrategy
{
    private static Random rand = null;

    public Stochastic( ConfigAgent config )
    {
        super( config );
        if ( rand == null ) { rand = new Random( agentConfig.getDeploymentSeed() ); }
    }

    public AgentLocation getNextLocation( int id )
    {
        Area unoccupied = Environment.unoccupiedArea();
        Rectangle2D bounds = unoccupied.getBounds2D();
        double x = -1;
        double y = -1;
        int limit = 1000;
        boolean found = false;

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
