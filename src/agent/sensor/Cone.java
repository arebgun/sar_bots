package agent.sensor;

/*
 * Class Name:    agent.sensor.Cone
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

import java.awt.geom.*;
import static java.lang.Math.toDegrees;
// TODO: remove Area from Cone.java (possibly remove Cone.java as well)
/**
 * Implements a pie-shaped agent sensor.
 */
public class Cone extends SensorModule
{
    public Cone( ConfigAgent config )
    {
        super( config );
    }

    /**
     * This sensor model starts out with a 60 degree pie shaped view, which is then intersected with the
     * unoccupied areas of the environment to produce the final set of visible world locations.
     *
     * @param loc Agent's current location.
     * @return a set of points visible to the agent and that are potentially accessible to the navigation planner
     */
    public Area getView( AgentLocation loc )
    {
        Rectangle2D bounds = new Rectangle2D.Double( loc.getX() - radius, loc.getY() - radius, 2 * radius, 2 * radius );
        // NOTE-dimzar-20060328: the "native" coordinate system of the Arc2D is "x to the right, y to the top";
        //                       however, our y goes to the bottom, thus the negation on the agent theta
        Arc2D cone     = new Arc2D.Double( bounds, -toDegrees( loc.getTheta() ) - 30, 60, Arc2D.PIE );
        Area footprint = new Area( cone );

        footprint.intersect( Environment.unoccupiedArea() );

        return footprint;
    }
}
