package agent.plan;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import sim.Simulator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.*;
import java.util.Random;

public class Stochastic implements PlanModule
{
    private static Random rand = null;

    public Stochastic()
    {
        if ( rand == null )
        {
            rand = new Random( Simulator.config.getPlanModuleSeed() );
        }
    }

    // dimzar: include a diagram and documentation for what you're doing here
    public AgentLocation getGoalLocation( AgentLocation location, Area sensorView )
    {
        Rectangle bounds = sensorView.getBounds();
        double x = -1, y = -1;
        double newTheta = location.getTheta();
        double wingSpan = Simulator.config.getWingSpan();


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
