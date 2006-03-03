package sim;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.Agent;
import agent.Scout;
import agent.Worker;
import config.Configuration;
import env.Environment;
import ui.GUI;

import java.util.ArrayList;

public class Simulator
{
    public static Configuration config;

    private static long time;

    private static ArrayList<Agent> agents;

    //private static GUI gui;  // do we need this guy?

    public static void run( String configFilePath ) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        time = 0;
        config = new Configuration( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );
        Environment.load();
        agents = new ArrayList<Agent>( config.numberOfScouts() + config.numberOfWorkers() );

        for ( int i = 0; i < config.numberOfScouts(); i++ )
        {
            agents.add( new Scout() );
        }

        for ( int i = 0; i < config.numberOfWorkers(); i++ )
        {
            agents.add( new Worker() );
        }

        javax.swing.SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                GUI.getInstance().show();
            }
        } );
    }

    public static long getTime()
    {
        return time;
    }

    public static void step()
    {
        for ( Agent agent : agents )
        {
            agent.move();
        }

        Environment.update();
        GUI.getInstance().update();

        time++;
    }

    public static void main( String[] arg )
    {
        try
        {
            Simulator.run( "conf/base.conf" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
