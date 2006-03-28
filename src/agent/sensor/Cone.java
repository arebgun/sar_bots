package agent.sensor;

import agent.AgentLocation;
import config.ConfigAgent;
import env.Environment;

import java.awt.geom.*;

import static java.lang.Math.toDegrees;
/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */
public class Cone extends SensorModule
{
    public Cone( ConfigAgent config )
    {
	super( config );
    }

    public Area getView( AgentLocation loc )
    {
	Rectangle2D bounds = new Rectangle2D.Double( loc.getX()-radius, loc.getY()-radius, 2*radius, 2*radius );
	// NOTE-dimzar-20060328: the "native" coordinate system of the Arc2D is "x to the right, y to the top";
	//                       however, our y goes to the bottom, thus the negation on the agent theta
	Arc2D cone         = new Arc2D.Double( bounds, -toDegrees( loc.getTheta() )-30, 60, Arc2D.PIE );
	Area footprint     = new Area( cone );

	footprint.intersect( Environment.unoccupiedArea() );
        return footprint;
    }
}
