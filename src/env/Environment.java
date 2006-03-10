package env;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import config.ConfigEnv;
import sim.Simulator;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Environment
{
    private static ArrayList<Polygon> buildings;
    private static HashMap<Integer, ArrayList<Ellipse2D.Double>> fires;
    private static ConfigEnv config;

    private Environment()
    {
    }

    public static void load( String envConfigFileName ) throws Exception
    {
        config = new ConfigEnv( ClassLoader.getSystemClassLoader().getResource( envConfigFileName ).getPath() );

        // parse the map file through addtion of a EnvMapParser object (dimzar)
        // populate the buildings list based on stuff in the map file, through exception if a vertex is out of bounds or x/y lists are different in length
        // populate the fires hashmap using the EnvFireParser object (dimzar)
        // process the BlackBoard agent movement queue to build the agents ArrrayList
    }

    public static ConfigEnv getConfig()
    {
        return config;
    }

    public static Area occupiedArea()
    {
        Area occupied = new Area();

        for ( Polygon building : buildings )
        {
            occupied.add( new Area( building ) );
        }

        occupied.add( Simulator.agentSpace() );

        return occupied;
    }


    public static void update()
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    // provide a buch of Iterator-based query methods for the GUI to get info on where the Buildings, Fire, and Agents are.

}
