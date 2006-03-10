package sim;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.Agent;
import agent.Scout;
import agent.Worker;
import config.ConfigEnv;
import env.Environment;
import ui.GUI;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Simulator
{
    private static ConfigEnv config;

    private static long time;

    private static Logger logger;

    private static ArrayList<Agent> agents;

    public static void run( String configFilePath ) throws Exception
    {
        time   = 0;
        config = new Configuration( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );
	logger = Logger.getLogger("search & rescue bots simulator");
        agents = new ArrayList<Agent>( config.getNumberOfScouts() + config.getNumberOfWorkers() );

	ArrayList<String> agentConfigFiles = config.getAgentConfigFileNames();
	for ( String aConfig : agentConfigFiles )
	    {
		ConfigAgent agentConfig = new ConfigAgent( aConfig );

		Class loader = Class.forName( agentConfig.getAgentClassName() ); //, true, this.getClass().getClassLoader() );

		for ( int i = 0; i < agentConfig.getNumberOfAgents(); i++ )
		    {
			agents.add( (Agent) loader.newInstance();  );
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
	    if ( args.length < 1 ) {
		logger.config("using the default simulator configuration file 'config/default.ConfigSim'");
		Simulator.run( "config/default.ConfigSim" );
	    } else {
		logger.config("using the '" + args[0] + "' simulator configuration file");
		Simulator.run( "config/" + args[0] );
	    }		
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
