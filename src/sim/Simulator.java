package sim;

import agent.Agent;
import config.ConfigAgent;
import config.ConfigSim;
import env.Environment;
import ui.GUI;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.*;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */
public class Simulator
{
    public static Logger logger;

    private static ConfigSim config;
    private static int time;
    private static ArrayList<Agent> agents;

    public static void run( String configFilePath ) throws Exception
    {
        time = 0;
        config = new ConfigSim( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );

        logger.info( "loading ENVIRONMENT data ..." );
        Environment.load( config.getEnvConfigFileName() );

        logger.info( "loading AGENT data ..." );
        agents = new ArrayList<Agent>();
        String agentConfigFiles[] = config.getAgentConfigFileNames();

        for ( String aFile : agentConfigFiles )
        {
            ConfigAgent agentConfig = new ConfigAgent( aFile );
            Class loader = Class.forName( agentConfig.getClassName(), true, agentConfig.getClass().getClassLoader() );

            for ( int i = 0; i < agentConfig.getSwarmSize(); i++ )
            {
                agents.add( (Agent) loader.getConstructor( ConfigAgent.class ).newInstance( agentConfig ) );
            }
        }

        logger.info( "displaying the GUI ..." );
        GUI.getInstance().show();
    }

    public static void reset()
    {
        time = 0;

        for(Agent agent : agents)
        {
            agent.reset();
        }

        Environment.reset();
    }

    public static int getTime()
    {
        return time;
    }

    public static void step()
    {
        time++;

        for ( Agent agent : agents )
        {
            agent.move();
        }

        Environment.update();
        GUI.getInstance().update();
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

    public static Iterator<Agent> agentsIterator()
    {
        return agents.iterator();
    }

    public static void main( String[] args )
    {
        final String DEFAULT_CONF_FILE = "usr/conf/default.ConfigSim";

        try
        {
            StreamHandler handler = new ConsoleHandler();
            //StreamHandler handler = new FileHandler( "some-log-filename-for-srbots-project.xml" );
            handler.setLevel( Level.ALL );

            logger = Logger.getLogger( "sim.Simulator" );
            logger.setUseParentHandlers( false );
            logger.setLevel( Level.ALL );
            logger.addHandler( handler );

            if ( args.length < 1 )
            {
                logger.config( "using the default simulator configuration file '" + DEFAULT_CONF_FILE + "'" );
                Simulator.run( DEFAULT_CONF_FILE );
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
