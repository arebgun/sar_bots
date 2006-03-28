package agent.sensor;

import agent.AgentLocation;
import config.ConfigAgent;
import env.Environment;

import java.awt.geom.*;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */
public class Circle extends SensorModule
{
    public Circle( ConfigAgent config )
    {
	super( config );
    }

    public Area getView( AgentLocation loc )
    {
        Area footprint = new Area( new Ellipse2D.Double( loc.getX()-radius, loc.getY()-radius, 2*radius, 2*radius ) );
        footprint.intersect( Environment.unoccupiedArea() );
        return footprint;
    }
}
