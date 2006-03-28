package agent.plan;

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.atan2;
import static java.lang.Math.hypot;
import java.util.Random;

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

    // dimzar: include a diagram and documentation for what you're doing here
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
                Area path = new Area( new Rectangle2D.Double( curX, curY, hypot( newX - curX, newY - curY ), wingSpan / 2 ) );
                path.transform( AffineTransform.getRotateInstance( newTheta, curX, curY ) );
                path.intersect( sensorView );

                if ( !path.isEmpty() && path.isSingular() ) { placed = true; }
            }
        }

        if ( !placed ) { throw new IllegalStateException( "unable to find agent next goal location" ); }

        return new AgentLocation( newX, newY, newTheta );
    }
}
