package agent.sensor;

/*
 * Class Name:    agent.sensor.Circle
 * Last Modified: 4/30/2006 4:3
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
// TODO: remove Area from Circle.java (possibly remove Circle.java as well)
/**
 * Implements a circular shaped agent sensor.
 *
 */
public class Circle extends SensorModule
{
    public Circle( ConfigAgent config )
    {
        super( config );
    }

    /**
     * Starts with a circular view, centered at the agents present location and radius specified by the sensor module
     * configuration file.  Returns the intersection of the sensor view and unoocupied areas of the environment.
     *
     * @param loc Agent's current location.
     * @return Area suitable for use by the agent's navigation planner module.
     */
    public Area getView( AgentLocation loc )
    {
        Area footprint = new Area( new Ellipse2D.Double( loc.getX() - radius, loc.getY() - radius, 2 * radius, 2 * radius ) );
        footprint.intersect( Environment.unoccupiedArea() );

        return footprint;
    }
}
