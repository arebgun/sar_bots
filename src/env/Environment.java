package env;

/*
 * Class Name:    env.Environment
 * Last Modified: 4/30/2006 9:35
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
import agent.AgentLocation;
import config.ConfigEnv;
import sim.Simulator;
import env.Flag;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.*;
import static java.lang.Math.*;
import java.text.ParseException;
import java.util.*;

/**
 * The encapsulator object for keeping track of the simulated environment (i.e., world) state.
 * This class is responsible for keeping track of the buildings map, the appearance of fires.
 * It also provides a means for the simulator to obtain appropriate agent sensor coverage maps,
 * and to compute the unoccupied areas of the world.
 *
 * Although all of the internal state is kept in real (floating point) coordinates, for easier
 * collection of statistics and visualization, this class provides a grid (with a given granularity),
 * which can be queried by other objects.  Hence this object also keeps track of the agent performance
 * such as sensor coverage, fire detection, etc.
 */
public class Environment
{
    static ConfigEnv config;
    private static int worldWidth, worldHeight;
    private static int gridSize, gridRowSize, gridColSize;
    private static ArrayList<Rectangle2D> grid;
    private static int totalCoveragePercentage;
    private static int wellCoveredPercentage;
    private static int[] sensCoverageFrequency;
    private static ArrayList<Double> sensCoverageRatios;
    private static Area buildings;
    private static Flag flag1;
    private static ArrayList<Polygon> buildingList;

    public static int getTotalCoveragePercentage()
    {
        return totalCoveragePercentage;
    }

    public static int getWellCoveredPercentage()
    {
        return wellCoveredPercentage;
    }

    /**
     * Boolean query function which indicates whether a point (x,y) is within the world bounds.
     * @param x
     * @param y
     * @return TRUE if the point (x,y) is within the world boundaries, FALSE otherwise.
     */
    public static boolean contains( double x, double y )
    {
        return ( x >= 0 && x <= config.getWorldWidth() && y >= 0 && y <= config.getWorldHeight() );
    }

    /**
     * Sets up the world object based on the parameters in the configuration file.
     *
     * @param envConfigFileName relative pathname to the config file, relative to the Simulator main directory.
     * @throws Exception
     */
    public static void load( String envConfigFileName ) throws Exception
    {
        config      = new ConfigEnv( ClassLoader.getSystemClassLoader().getResource( envConfigFileName ).getPath() );
        worldWidth  = config.getWorldWidth();
        worldHeight = config.getWorldHeight();
        grid        = new ArrayList<Rectangle2D>();
        gridSize    = config.getGridSize();
        gridRowSize = worldWidth  / gridSize;
        gridColSize = worldHeight / gridSize;

        // NOTE: cells are stored in top-down, left-to-right order (column-major mode when looking at the grid)
        for ( int i = 0; i < gridRowSize; i++ )
        {
            for ( int j = 0; j < gridColSize; j++ )
            {
                grid.add( new Rectangle2D.Double( i * gridSize, j * gridSize, gridSize, gridSize ) );
            }
        }

        sensCoverageFrequency = new int[grid.size()];
        sensCoverageRatios    = new ArrayList<Double>( grid.size() );

        loadBuildings( config.getBuildingsFileName() );
        loadFires( config.getFiresFileName() );
        loadFlags( config.getFlagsFileName() );
 
    }

    /**
     * Resets the world to the initial state, initializes the statistical measures, clears the fire information.
     */
    public static void reset()
    {
        Fire.reset();
        sensCoverageRatios.clear();
        Arrays.fill( sensCoverageFrequency, 0 );
        totalCoveragePercentage = 0;
        wellCoveredPercentage   = 0;
    }

    /**
     * Computes the portion of the world that is occupied by objects, such as buildings and other agents.
     * In order to increase simulation performance, it is possible to disable the N^2 computation for computing
     * the area occupied by agents.  This will lead to much faster throughput, but will allow collisions between
     * the agents to occur.
     * @return
     */
    public static Area occupiedArea()
    {
        Area occupied = new Area( buildings );
        occupied.add( Simulator.agentSpace() ); // dimzar: speed up line right here :)

        return occupied;
    }

    /**
     * Uses the @see occupiedArea to compute the set of points accessible to the navigation planners.
     * @return
     */
    public static Area unoccupiedArea()
    {
        Area world = new Area( new Rectangle2D.Double( 0, 0, worldWidth, worldHeight ) );
        world.subtract( occupiedArea() );

        return world;
    }

    /**
     * Advances the state of the simulation one time step; introduces fires, computes detection and coverage statistics.
     */
    public static void update()
    {
        // compute sensor coverage frequency
        Iterator<Agent> iter = Simulator.agentsIterator();

        while ( iter.hasNext() )
        {
            Agent agent        = iter.next();
            Area sensFootprint = agent.getSensorView();
            Rectangle2D bounds = sensFootprint.getBounds2D();

            int startX = (int) max( 0, floor( bounds.getX() / gridSize ) );
            int startY = (int) max( 0, floor( bounds.getY() / gridSize ) );
            int endX   = (int) min( gridRowSize, ceil( ( bounds.getX() + bounds.getWidth()  ) / gridSize ) );
            int endY   = (int) min( gridColSize, ceil( ( bounds.getY() + bounds.getHeight() ) / gridSize ) );

            for ( int i = startX; i < endX; i++ )
            {
                for ( int j = startY; j < endY; j++ )
                {
                    // NOTE: cells are stored using column-major mode
                    int index = i * gridColSize + j;
                    if ( sensFootprint.intersects( grid.get( index ) ) ) { sensCoverageFrequency[index]++; }
                }
            }
        }

        calculateCoverage();
    }

    /**
     * Loads the building geometry from the configuration file.  The buildings are specified by a list of (x,y) points,
     * representing arbitrary polygon geometry.
     *
     * @param buildingsFileName Fully qualified pathname to the buildings configuration file.
     * @throws Exception
     */
    private static void loadBuildings( String buildingsFileName ) throws Exception
    {
        buildings    = new Area();
        buildingList = new ArrayList<Polygon>();

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
            buildingList.add( building );
            buildings.add( new Area( building ) );
        }
    }

    /**
     * Parses the timing and size information for fires, storing them into the <code>Fire</code> hashtable.
     *
     * @param firesFileName Fully qualified pathname to the fires configuration file.
     * @throws Exception
     */
    private static void loadFires( String firesFileName ) throws Exception
    {
        StreamTokenizer st = new StreamTokenizer( new BufferedReader( new FileReader( firesFileName ) ) );
        st.ordinaryChars( '+', '9' );
        st.wordChars( ' ', '~' );
        st.commentChar( '#' );

        while ( st.nextToken() != StreamTokenizer.TT_EOF )
        {
            Fire.add( st.sval.split( "\\," ) );
        }
    }
    
    /**
     * Parses the (x,y) coordinate information for flags and creates as many
     * flags as needed
     * 
     * @param flagsFileName Fully qualified pathname to the flags configuration file.
     * @throws Exception
     */
    private static void loadFlags( String flagsFileName ) throws Exception
    {
    	StreamTokenizer st = new StreamTokenizer( new BufferedReader( new FileReader( flagsFileName ) ) );
    	st.ordinaryChars('+', '9' );
    	st.wordChars( ' ', '~' );
    	st.commentChar( '#');
    	
    	while ( st.nextToken() != StreamTokenizer.TT_EOF )
    	{
    		String flagData[] = st.sval.split( "\\,");
    		int x = Integer.parseInt(flagData[1]);
    		int y = Integer.parseInt(flagData[2]);
    		AgentLocation temp = new AgentLocation(x,y,0);
    		flag1 = new Flag(temp);
    	}
    }

    /**
     * Pixel scaling function to enforce world aspect ratio for window resize events.
     *
     * @param g2 The @see Graphics object from the GUI.
     * @param pixelScreenSize Current size of the display frame.
     */
    public static void scaleGraphics( Graphics2D g2, Dimension pixelScreenSize )
    {
        g2.scale( pixelScreenSize.width / worldWidth, pixelScreenSize.height / worldHeight );
    }

    /**
     * Convenience method for computing the maximum zoom for world display.
     *
     * @param drawing The display component of the world graphic.
     * @param pixelScreenSize Current window size.
     */
    public static void scaleDrawing( Component drawing, Dimension pixelScreenSize )
    {
        int zoom             = optimalZoom( pixelScreenSize );
        Dimension scaledSize = new Dimension( zoom * worldWidth + 1, zoom * worldHeight + 1 );
        drawing.setSize( scaledSize );
        drawing.setPreferredSize( scaledSize );
    }

    public static double aspectRatio()
    {
        return worldWidth / worldHeight;
    }

    /**
     * Returns the maximum zoom factor for display the world given the current window size.
     *
     * @param pixelScreenSize Window size
     * @return Maximum zoom which will fit on the screen.
     */
    public static int optimalZoom( Dimension pixelScreenSize )
    {
        return max( 1, max( pixelScreenSize.width / worldWidth, pixelScreenSize.height / worldHeight ) );
    }

    public static Area getBuildings()
    {
        return buildings;
    }

    public static Area getFires()
    {
        return Fire.getFires();
    }

    public static int getActiveFires()
    {
        return Fire.getCurFires();
    }

    public static int getFoundFires()
    {
        return Fire.getDetFires();
    }

    /**
     * Graphics utility routine for positioning the texture maps of the world and buildings background.
     *
     * @param divisor
     * @return
     */
    public static Rectangle2D getTextureAnchor( double divisor )
    {
        return new Rectangle2D.Double( 0, 0, worldWidth / divisor, worldHeight / divisor );
    }

    public static Iterator<Rectangle2D> gridIterator()
    {
        return grid.iterator();
    }

    public static Iterator<Double> sensCoverageFractionIterator()
    {
        calculateCoverage();

        return sensCoverageRatios.iterator();
    }

    /**
     * Uses the grid-based coverage method to count the number of cells that have more than 50% exposure to agent sensors.
     */
    private static void calculateCoverage()
    {
        sensCoverageRatios.clear();
        double max = 1;

        int coveredCells     = 0;
        int wellCoveredCells = 0;

        for ( int val : sensCoverageFrequency )
        {
            if ( val > max ) { max = val; }
            if ( val != 0 )  { coveredCells++; }
        }

        for ( int ratio : sensCoverageFrequency )
        {
            sensCoverageRatios.add( ratio / max );
            if ( ratio / max > 0.5 ) { wellCoveredCells++; }
        }

        totalCoveragePercentage = ( 100 * coveredCells )     / sensCoverageFrequency.length;
        wellCoveredPercentage   = ( 100 * wellCoveredCells ) / sensCoverageFrequency.length;
    }
}
