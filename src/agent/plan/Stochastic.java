package agent.plan;

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.*;
import java.util.Random;

import static java.lang.Math.*;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */
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
        double curX = location.getX(), curY = location.getY();
        double newX = -1, newY = -1, newTheta = location.getTheta();
        double wingSpan = agentConfig.getWingSpan();
        int limit = 1000;
        boolean placed = false;

        while ( !placed && --limit > 0 )
        {
            newX = bounds.getX() + bounds.getWidth() * rand.nextDouble();
            newY = bounds.getY() + bounds.getHeight() * rand.nextDouble();

            if ( sensorView.contains( newX, newY ) )
            {
                newTheta = atan2( newY - curY, newX - curX );
                Area path = new Area( new Rectangle2D.Double( curX, curY, hypot( newX - curX, newY - curY ), .5/*wingSpan/2*/) );
                path.transform( AffineTransform.getRotateInstance( newTheta, curX, curY ) );
                path.intersect( sensorView );

                if ( !path.isEmpty() && path.isSingular() ) { placed = true; }
            }
        }
        //if ( !placed ) { throw new IllegalStateException( "unable to find agent next goal location" ); }
	if ( !placed ) {
	    newX     = curX;
	    newY     = curY;
	    newTheta = location.getTheta() + PI;
	    sim.Simulator.logger.finest("agent rotated");
	}
        return new AgentLocation( newX, newY, newTheta );
    }
}
