package sim;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.Agent;
import config.ConfigAgent;
import config.ConfigSim;
import env.Environment;
import ui.GUI;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;


public class Simulator
{
    private static ConfigSim config;
    private static long time;
    private static Logger logger;
    private static ArrayList<Agent> agents;

    public static void run( String configFilePath ) throws Exception
    {
        time   = 0;
        config = new ConfigSim( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );

        Environment.load( config.getEnvConfigFileName() );

        agents = new ArrayList<Agent>();
        String agentConfigFiles[] = config.getAgentConfigFileNames();

	for (String aFile : agentConfigFiles)
        {
            ConfigAgent agentConfig = new ConfigAgent( aFile );
            Class loader            = Class.forName( agentConfig.getClassName() );

            for ( int i = 0; i < agentConfig.getSwarmSize(); i++ )
            {
                agents.add( (Agent) loader.getConstructor( ConfigAgent.class ).newInstance( agentConfig ) );
            }
        }

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

    public static void main( String[] args )
    {
        try
        {
	    StreamHandler handler = new ConsoleHandler();
	    //StreamHandler handler = new FileHandler( "some-log-filename-for-srbots-project.xml" );
	    handler.setLevel( Level.ALL );

            logger = Logger.getLogger( "sim.Simulator" );
	    logger.setLevel( Level.ALL );
	    logger.addHandler( handler );

            if ( args.length < 1 )
            {
                logger.config( "using the default simulator configuration file 'usr/conf/default.ConfigSim'" );
                Simulator.run( "usr/conf/default.ConfigSim" );
            }
            else
            {
                logger.config( "using the 'usr/conf/" + args[0] + "' simulator configuration file" );
                Simulator.run( "usr/conf/" + args[0] );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
