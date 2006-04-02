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

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getTheta()
    {
        return theta;
    }


    public void setX( double newX )
    {
        if ( !Environment.contains( newX, y ) )
        {
            throw new IndexOutOfBoundsException( "new agent horizontal postion " + newX + " is out of world bounds" );
        }

        x = newX;
    }

    public void setY( double newY )
    {
        if ( !Environment.contains( x, newY ) )
        {
            throw new IndexOutOfBoundsException( "new agent vertical postion " + newY + " is out of world bounds" );
        }

        y = newY;
    }

    public void setTheta( double newTheta )
    {
        theta = newTheta;
    }
}
