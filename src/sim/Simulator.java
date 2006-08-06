package sim;

/*
 * Class Name:    sim.Simulator
 * Last Modified: 4/30/2006 10:48
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import agent.Agent;
import config.ConfigAgent;
import config.ConfigSim;
import env.Environment;
import env.Fire;
import ui.GUI;
import ui.CLI;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.*;

public class Simulator
{
    public static Logger logger;

    private static ConfigSim config;
    private static int time;
    private static ArrayList<Agent> agents;

    public static void run( String configFilePath, String uiType ) throws Exception
    {
        time   = 0;
        config = new ConfigSim( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );

        logger.info( "loading ENVIRONMENT data ..." );
        Environment.load( config.getEnvConfigFileName() );

        logger.info( "loading AGENT data ..." );
        agents = new ArrayList<Agent>();
        String agentConfigFiles[] = config.getAgentConfigFileNames();

        for ( String aFile : agentConfigFiles )
        {
            ConfigAgent agentConfig = new ConfigAgent( aFile );
            Class loader            = Class.forName( agentConfig.getClassName(), true, agentConfig.getClass().getClassLoader() );

            for ( int i = 0; i < agentConfig.getSwarmSize(); i++ )
            {
                agents.add( (Agent) loader.getConstructor( ConfigAgent.class ).newInstance( agentConfig ) );
            }
        }

        if ( uiType.equalsIgnoreCase( "gui" ) )
        {
            logger.info( "displaying the GUI ..." );
            GUI.getInstance().show();
        }
        else
        {
            logger.info( "displaying the CLI ..." );
            CLI.getInstance().show();
        }
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

        /*if (time % 3 == 0)*/ { Environment.update(); }

        // introduce fires
        Fire.update( time );
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

    public static Area agentSensorSpace()
    {
        Area space = new Area();

        for ( Agent agent : agents )
        {
            space.add( agent.getSensorView() );
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

            if ( args.length < 2 )
            {
                logger.config( "using the default simulator configuration file '" + DEFAULT_CONF_FILE + "'" );
                logger.config( "loading the default user interface");
                Simulator.run( DEFAULT_CONF_FILE, "gui" );
            }
            else
            {
                logger.config( "using the 'usr/conf/" + args[0] + ".ConfigSim' simulator configuration file" );
                logger.config( "loading user interface: " + (args[1].equalsIgnoreCase( "cli") ? "'Command Line Interface'" : "'Graphical User Interface'"));
                Simulator.run( "usr/conf/" + args[0] + ".ConfigSim", args[1] );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
