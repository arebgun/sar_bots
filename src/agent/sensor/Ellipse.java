package agent.sensor;

/*
 * Class Name:    agent.sensor.Ellipse
 * Last Modified: 5/3/2006 8:30
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

import java.awt.geom.*;

/**
 * Implements an elliptical agent sensor.
 */
public class Ellipse extends SensorModule
{
    public Ellipse( ConfigAgent config )
    {
        super( config );
    }

    /**
     * Starts with an elliptical view, centered at the agents present location and radii specified by the sensor module
     * configuration file, with the longer axis along the main fuselage of the UAV.  Returns the intersection of the
     * sensor view and unoocupied areas of the environment.
     *
     * @param loc Agent's current location.
     * @return Area suitable for use by the agent's navigation planner module.
     */
    public Area getView( AgentLocation loc )
    {
        Ellipse2D ellipse  = new Ellipse2D.Double( loc.getX() - radius / 2, loc.getY() - radius * 2, radius, 2 * radius );
        Area footprint = new Area( ellipse );
        footprint.transform( AffineTransform.getRotateInstance( -loc.getTheta(), loc.getX(), loc.getY() ) );

        footprint.intersect( Environment.unoccupiedArea() );

        return footprint;
    }
}