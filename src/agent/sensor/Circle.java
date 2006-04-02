package agent.sensor;

/*
 * Class Name:    agent.sensor.Circle
 * Last Modified: 4/2/2006 3:2
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
import java.awt.geom.Ellipse2D;

public class Circle extends SensorModule
{
    public Circle( ConfigAgent config )
    {
        super( config );
    }

    public Area getView( AgentLocation loc )
    {
        Area footprint = new Area( new Ellipse2D.Double( loc.getX() - radius, loc.getY() - radius, 2 * radius, 2 * radius ) );
        footprint.intersect( Environment.unoccupiedArea() );

        return footprint;
    }
}
