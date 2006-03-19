package agent.plan;

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

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

    // dimzar: include a diagram and documentation for what you're doing here
    public AgentLocation getGoalLocation( AgentLocation location, Area sensorView )
    {
        Rectangle2D bounds = sensorView.getBounds();
        double x = -1, y = -1;
        double newTheta = location.getTheta();
        double wingSpan = agentConfig.getWingSpan();


        boolean placed = false;
        while ( !placed )
        {
            x = bounds.getX() + bounds.getWidth() * rand.nextDouble();
            y = bounds.getY() + bounds.getHeight() * rand.nextDouble();
            newTheta = atan2( y - location.getY(), x - location.getX() );

            double pathX = wingSpan / 2 * cos( newTheta ), pathY = wingSpan / 2 * sin( newTheta );

            Area path = new Area( new Rectangle2D.Double( pathX, pathY, wingSpan, hypot( x - location.getX(), y - location.getY() ) ) );
            path.transform( AffineTransform.getRotateInstance( newTheta ) );
            Area pathSensorIntersect = (Area) sensorView.clone();
            pathSensorIntersect.intersect( path );

            if ( pathSensorIntersect.equals( path ) )
            {
                placed = true;
            }
        }
        return new AgentLocation( x, y, newTheta );
    }
}
