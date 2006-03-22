package agent.sensor;

import agent.AgentLocation;
import config.ConfigAgent;
import env.Environment;

import java.awt.*;
import java.awt.geom.*;

/**
 * @(#) Circle.java
 */
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
	Area footprint = new Area( new Ellipse2D.Double(loc.getX() - radius, loc.getY() - radius, 2*radius, 2*radius) );
	footprint.intersect( Environment.unoccupiedArea() );
	return footprint;
    }
}
