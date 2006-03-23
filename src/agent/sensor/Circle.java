package agent.sensor;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import config.ConfigAgent;
import env.Environment;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Circle extends SensorModule
{
    private double radius;

    public Circle( ConfigAgent config )
    {
        super( config );
        radius = agentConfig.getSensorRange();
    }

    public Area getView( AgentLocation loc )
    {
        Area footprint = new Area( new Ellipse2D.Double( loc.getX() - radius, loc.getY() - radius, 2 * radius, 2 * radius ) );
        footprint.intersect( Environment.unoccupiedArea() );

        return footprint;
    }
}
