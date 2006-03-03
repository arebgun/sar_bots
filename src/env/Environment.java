package env;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Environment
{
    private ArrayList<Polygon> buidings;
    private HashMap<Integer, ArrayList<Ellipse2D.Double>> fires;
    private ArrayList<AgentLocation> agents;

    private Environment()
    {
    }

    /*
    public static Environment getInstance()
    {
        if ( environmentInstance == null )
        {
            environmentInstance = new Environment();
            sim = Simulator.getSimulator();
        }

        return environmentInstance;
    }
    @SuppressWarnings( { "CloneDoesntCallSuperClone" } )
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
    */

    public static void load()
    {
        // parse the map file through addtion of a EnvMapParser object (dimzar)
        // populate the buildings list based on stuff in the map file, through exception if a vertex is out of bounds or x/y lists are different in length
        // populate the fires hashmap using the EnvFireParser object (dimzar)
        // process the BlackBoard agent movement queue to build the agents ArrrayList
    }

    public static void update()
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    // provide a buch of Iterator-based query methods for the GUI to get info on where the Buildings, Fire, and Agents are.

}
