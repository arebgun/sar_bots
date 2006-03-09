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

import java.awt.geom.Area;
import java.util.ArrayList;

public class Simulator
{
    public static Configuration config;

    private static long time;

    private static ArrayList<Agent> agents;

    public static void run( String configFilePath ) throws Exception
    {
        time = 0;
        config = new Configuration( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );
        agents = new ArrayList<Agent>( config.getNumberOfScouts() + config.getNumberOfWorkers() );

        for ( int i = 0; i < config.getNumberOfScouts(); i++ )
        {
            agents.add( new Scout() );
        }

        for ( int i = 0; i < config.getNumberOfWorkers(); i++ )
        {
            agents.add( new Worker() );
        }

        Environment.load();
        GUI.getInstance().show();
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

    public static Area agentSpace()
    {
        Area space = new Area();

        for ( Agent agent : agents )
        {
            space.add( agent.getBodyArea() );
        }

        return space;
    }

    public static void main( String[] arg )
    {
        try
        {
            Simulator.run( "config/base.conf" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
