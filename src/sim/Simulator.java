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
import java.util.logging.Logger;

public class Simulator
{
    private static ConfigSim config;
    private static long time;
    private static Logger logger;
    private static ArrayList<Agent> agents;

    public static void run( String configFilePath ) throws Exception
    {
        time = 0;
        config = new ConfigSim( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );
        agents = new ArrayList<Agent>();
        ArrayList<String> agentConfigFiles = config.getAgentConfigFileNames();

        for ( String aConfig : agentConfigFiles )
        {
            ConfigAgent agentConfig = new ConfigAgent( aConfig );
            Class loader = Class.forName( agentConfig.getAgentClassName() );

            for ( int i = 0; i < agentConfig.getNumberOfAgents(); i++ )
            {
                agents.add( (Agent) loader.getConstructor( ConfigAgent.class ).newInstance( agentConfig ) );
            }
        }

        Environment.load( config.getEnvConfigFileName() );
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
            logger = Logger.getLogger( "search & rescue bots simulator" );

            if ( args.length < 1 )
            {
                logger.config( "using the default simulator configuration file 'config/default.ConfigSim'" );
                Simulator.run( "usr/conf/default.ConfigSim" );
            }
            else
            {
                logger.config( "using the '" + args[0] + "' simulator configuration file" );
                Simulator.run( "usr/conf/" + args[0] );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
