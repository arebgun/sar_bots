package env;

/*
 * Class Name:    env.Fire
 * Last Modified: 4/30/2006 8:41
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import sim.Simulator;

import java.awt.*;
import java.awt.geom.Area;
import java.text.ParseException;
import java.util.*;

import static java.lang.Math.*;
// TODO: remove Area from Fire (possibly remove Fire altogether, though might be used
// as extra fluff for damaged units)
/**
 * Encapsulator class to provide access to fire object management.  Stores the random seed for circular Gaussian
 * fire shapes (i.e., a circle with jagged edges).  Keeps track of currently active and detected fires, as well as
 * all fires specified in the configuration file.
 *
 */
public class Fire
{
    private static Random fireRand                            = new Random( Environment.config.getFireSeed() );
    private static Area fires                                 = new Area();
    private static HashMap<Integer, ArrayList<Fire>> fireList = new HashMap<Integer, ArrayList<Fire>>();
    private static ArrayList<Fire> curFires                   = new ArrayList<Fire>();
    private static ArrayList<Fire> detFires                   = new ArrayList<Fire>();

    private boolean detected;
    private int time;
    private Polygon shape;

    private Fire( String time, String x, String y, String width, String height )
    {
        detected  = false;
        this.time = Integer.parseInt( time );
        shape     = new Polygon();

        double originX = Double.parseDouble( x )       + Double.parseDouble( width )    / 2;
        double originY = Double.parseDouble( y )       + Double.parseDouble( height )   / 2;
        double avgR    = ( Double.parseDouble( width ) + Double.parseDouble( height ) ) / 2;

        for ( int i = 0; i < 300; i++ )
        {
            double theta = i * PI / 150, r = ( 1 + .5 * fireRand.nextGaussian() ) * avgR;
            shape.addPoint( (int) round( r * cos( theta ) + originX ), (int) round( r * sin( theta ) + originY ) );
        }
    }

    public static Area getFires()
    {
        return fires;
    }

    public static int getCurFires()
    {
        return curFires.size();
    }

    public static int getDetFires()
    {
        return detFires.size();
    }

    /**
     * Restore the initial state as far as the current and detected fires, but leaves the all fires hashtables intact.
     */
    public static void reset()
    {
        fires.reset();
        //fireList.clear(); //can't clear fire list - not reloading the config file
        curFires.clear();
        detFires.clear();
    }

    /**
     * Checks the current simulation time against the all fires hashtable, and adds fires if the times match.  Also
     * computes fire detection statistic.
     *
     * @param curTime Current simulation step.  This matches directly to the time steps specified in the fire configuration
     * file.
     */
    public static void update( Integer curTime )
    {
        // introduce new fires
        ArrayList<Fire>list = fireList.get( curTime );

        if ( list != null )
        {
            for ( Fire fire : list )
            {
                fires.add( new Area( fire.shape ) );
                curFires.add( fire );
            }
        }

        Area sensorCoverage = Simulator.agentSensorSpace();

        for ( Fire fire : curFires )
        {
            Area intersection = new Area( fire.shape );
            intersection.intersect( sensorCoverage );

            if ( !intersection.isEmpty() )
            {
                if ( !fire.detected )
                {
                    fire.detected = true;
                    detFires.add( fire );
                }
            }
        }
    }

    /**
     * Fire description line, which consits of time, location, and size (specified as a bounding box)
     *
     * @param fireDesc
     * @throws Exception
     */
    public static void add( String[]fireDesc ) throws Exception
    {
        if ( fireDesc.length != 5 ){ throw new ParseException( "fire description number of arguments", fireDesc.length ); }
        Fire fire = new Fire( fireDesc[0], fireDesc[1], fireDesc[2], fireDesc[3], fireDesc[4] );

        if ( fireList.containsKey( fire.time ) )
        {
            fireList.get( fire.time ).add( fire );
        }
        else
        {
            ArrayList<Fire>value = new ArrayList<Fire>();
            value.add( fire );
            fireList.put( fire.time, value );
        }
    }
}
