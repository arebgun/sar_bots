package agent;

/*
 * Class Name:    agent.AgentLocation
 * Last Modified: 4/2/2006 3:5
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import env.Environment;

public class AgentLocation
{
    private double x     = 0;
    private double y     = 0;
    private double theta = 0;

    public AgentLocation( double x, double y, double theta )
    {
        setX( x );
        setY( y );
        setTheta( theta );
    }

    /**
     * Gets x coordinate of the agent.
     *
     * @return x coordinate of the agent
     */
    public double getX()
    {
        return x;
    }

    /**
     * Gets y coordinate of the agent.
     *
     * @return y coordinate of the agent
     */
    public double getY()
    {
        return y;
    }

    /**
     * Gets the direction of the agent (in radians).
     *
     * @return x coordinate of the agent (in radians)
     */
    public double getTheta()
    {
        return theta;
    }

    /**
     * Sets a new x coordiante of the agent. If the new coordiante is out
     * of the world bounds throws an exception.
     *
     * @param newX new x coordinate of the agent
     */
    public void setX( double newX )
    {
        if ( !Environment.contains( newX, y ) )
        {
            throw new IndexOutOfBoundsException( "new agent horizontal postion " + newX + " is out of world bounds" );
        }

        x = newX;
    }

    /**
     * Sets a new y coordiante of the agent. If the new coordiante is out
     * of the world bounds throws an exception.
     *
     * @param newY new y coordinate of the agent
     */
    public void setY( double newY )
    {
        if ( !Environment.contains( x, newY ) )
        {
            throw new IndexOutOfBoundsException( "new agent vertical postion " + newY + " is out of world bounds" );
        }

        y = newY;
    }

    /**
     * Sets a new direction of the agent.
     *
     * @param newTheta new direction of the agent (in radians)
     */
    public void setTheta( double newTheta )
    {
        theta = newTheta;
    }
}
