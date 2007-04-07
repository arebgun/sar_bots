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


import config.*;
import env.Environment;
import ui.GUI;
import ui.CLI;
import baseobject.*;
import agent.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.*;

import obstacle.Obstacle;
/**
 * The main entrypoint for the UAV Search and Rescue Bot simulator.  This object keeps track of the main components,
 * such as the agents, the enviroment, and configuration.
*/
public class Simulator
{
    public static Logger logger;

    private static ConfigSim config;
    private static int time;
   //our world objects and the number of them (numberWorldObjects is used as
    //the index value and object id)
    private static int numberWorldObjects = 0;
    private static ArrayList<Bobject> worldObjects;
    
    /**
     * The driver method of the simulation which instructs all of the components to initialize, update, and produce
     * output.
     *
     * @param configFilePath The relative pathname for the configuation files.
     * @param uiType The selector for the output method (e.g., GUI, CLI, etc)
     * @throws Exception
     */
    public static void run( String configFilePath, String uiType ) throws Exception
    {
       
        startUp(configFilePath);
        mainSim(uiType);
        cleanUp();
       
    }
/*========================================================================
 * Start Up functions for the simulation
 *========================================================================*/
    private static void startUp(String configFilePath) throws Exception
    {       
    	time   = 0;
    	loadObjects(configFilePath);
    	placeObjects();
    }
    
    private static void loadObjects(String configFilePath) throws Exception
    {
    	config = new ConfigSim( ClassLoader.getSystemClassLoader().getResource( configFilePath ).getPath() );

        logger.info( "loading ENVIRONMENT data ..." );
        Environment.load( config.getEnvConfigFileName() );

        logger.info( "loading AGENT data ..." );
        worldObjects = new ArrayList<Bobject>();

        String objectConfigFiles[] = config.getObjectConfigFileNames();
        
        for ( String objFile : objectConfigFiles )
        {
        	ConfigBobject objConfig = new ConfigBobject( objFile );
        	Class loader = Class.forName(objConfig.getClassName(), true, objConfig.getClass().getClassLoader() );
        	
        	for ( int o = numberWorldObjects; o < objConfig.getSwarmSize() + numberWorldObjects; o++)
        	{
        		worldObjects.add( (Bobject) loader.getConstructor(ConfigBobject.class).newInstance(objConfig));
        		worldObjects.get(o).setObjectID(o);
        		//numberWorldObjects++;
        	}
        	numberWorldObjects += objConfig.getSwarmSize();
        }
    }
    
    private static void placeObjects()
    {
    	Iterator<Bobject> i = objectIterator();
        while (i.hasNext())
        {
        	Bobject b = i.next();
        	if (b.isAgent())
        	{
        		Agent a = (Agent)b;
        		AgentLocation loc;
        		loc = a.deployStrategy.getNextLocation(a);
        		a.setInitialLocation(loc);
        		a.setLocation(loc);
        	}
        	//finish obstacles placement and make one for flag too
        	if (b.isObstacle())
        	{
        		Obstacle o = (Obstacle)b;
        		AgentLocation loc;
        		loc = o.deployStrategy.getNextLocation(o);
        		o.setInitialLocation(loc);
        		o.setLocation(loc);
        	}
        }
     }
    
/*================================================================================
 * MainSim functions
 *================================================================================*/
    private static void mainSim(String uiType ) throws Exception
    {
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
    
/*===================================================================================
 * cleanUp functions
 *===================================================================================*/
    private static void cleanUp()
    {
    	//i'm sure somehting will go here
    }
   
    /**
     * Implements the reset functionality by delegating to the reset method of all of the components.
     */
    public static void reset()
    {
        time = 0;

        for(Bobject obj : worldObjects)
        {
            obj.reset();
        }

        Environment.reset();
    }

    public static int getTime()
    {
        return time;
    }

    /**
     * Advances the simulation time by increasing the current simulation step, and calling the update method of each
     * component.
    */
    public static void step()
    {
        time++;

        /*if (time % 3 == 0)*/ { Environment.update(); }

    }

    public static Iterator<Bobject> objectIterator()
    {
    	return worldObjects.iterator();
    }
    
    public static Bobject getObjectByID(int id)
    {
    	return worldObjects.get(id);
    }
    
    /**
     * Initializes the simulator state, sets up the logging facilities, and parses the command line arguments.
     *
     * @param args
     */
    public static void main( String[] args )
    {
        final String DEFAULT_CONF_FILE = "usr/conf/default.ConfigSim";

        try
        {
            StreamHandler handler = new ConsoleHandler();
            //StreamHandler handler = new FileHandler( "some-log-filename-for-srbots-project.log" );
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
