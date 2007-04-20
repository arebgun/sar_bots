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
import obstacle.PolygonObstacle;
import java.util.ArrayList;
import messageBoard.MessageBoard;
import java.util.Iterator;
import java.util.logging.*;
import obstacle.Obstacle;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import statistics.Statistics;

/**
 * The main entrypoint for the Capture the Flag Bot Simulator.  This object keeps track of the main components,
 * such as the agents, the enviroment, and configuration.
*/
public class Simulator 
{
    public static Logger logger;

    public static Statistics stats = null;
    private static ConfigSim config;
    private static int time;
   //our world objects and the number of them (numberWorldObjects is used as
    //the index value and object id)
    public static int numberWorldObjects = 0;
    public static ArrayList<Bobject> worldObjects;
    public static ArrayList<MessageBoard> teamBoards;
    public static int numberOfTeams;
    public static ArrayList<Integer> numberOnTeam;
    public static String whoCapturedFlag = null;
    
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
        
        /*Adding the messageBoards for each team*/
        numberOfTeams = config.getNumberOfTeams();
        teamBoards = new ArrayList<MessageBoard>(); 
        numberOnTeam = new ArrayList<Integer>();
        for(int i = 0; i <= numberOfTeams; i++)
        {
        	numberOnTeam.add(0);
        	if(i == 0)
        	{
        		teamBoards.add(null);
        	} 
        	else
        	{
        		teamBoards.add(new MessageBoard());
        	}
        }
		//numberOnTeam.ensureCapacity(numberOfTeams);
        
        // load the buildings, if we have any
        // load the buildings first so they have the first positions 
        // in the worldObjects array. this way they get place where they are
        // supposed to be.
        String buildings = config.getBuildingsFileName();
        if (buildings != null)
        	loadBuildings(buildings);
        
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
        		if(worldObjects.get(o).isAgent())
        		{

        			Agent a = (Agent)worldObjects.get(o);
        			int temp = numberOnTeam.get(a.getTeamID());
        			a.setMsgID(temp);
        			temp++;
        			numberOnTeam.set(a.getTeamID(), temp);

        		}
        	}
        	numberWorldObjects += objConfig.getSwarmSize();
        }
        
        /*Initialize the message boards*/
        for(int i = 1; i <= numberOfTeams; i++)
        {
        	teamBoards.get(i).initialize(numberOnTeam.get(i));
        }
        
        stats = Statistics.getStatisticsInstance();
    }
    private static void loadBuildings( String buildingsFileName ) throws Exception
    {
        
        StreamTokenizer st = new StreamTokenizer( new BufferedReader( new FileReader( buildingsFileName ) ) );
        st.ordinaryChars( '+', '9' );
        st.wordChars( ' ', '~' );
        st.commentChar( '#' );

        while ( st.nextToken() != StreamTokenizer.TT_EOF )
        {
            String xPoints[] = st.sval.split( "\\," );
            st.nextToken();
            String yPoints[] = st.sval.split( "\\," );

            int n = xPoints.length;
            if ( n != yPoints.length ){ throw new ParseException( "building vertex X/Y list length mismatch", n ); }

            int x[] = new int[n];
            int y[] = new int[n];

            for ( int i = 0; i < n; i++ )
            {
                x[i] = Integer.parseInt( xPoints[i] );
                y[i] = Integer.parseInt( yPoints[i] );
            }

            Polygon building = new Polygon( x, y, n );
            Color newColor = new Color(0,0,0,0);
            numberWorldObjects++;
            worldObjects.add(new PolygonObstacle(building,newColor,numberWorldObjects));
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
        /*reset the message boards*/
        for(int i = 1; i <= numberOfTeams; i++)
        {
        	teamBoards.get(i).reset();
        }

        for(Bobject obj : worldObjects)
        {
            obj.reset();
        }

        Environment.reset();
        
        whoCapturedFlag = null;
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
