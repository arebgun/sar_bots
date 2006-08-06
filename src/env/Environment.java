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
import config.ConfigEnv;
import sim.Simulator;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.*;
import static java.lang.Math.*;
import java.text.ParseException;
import java.util.*;

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
    private static ArrayList<Polygon> buildingList;

    public static int getTotalCoveragePercentage()
    {
        return totalCoveragePercentage;
    }

    public static int getWellCoveredPercentage()
    {
        return wellCoveredPercentage;
    }

    public static boolean contains( double x, double y )
    {
        return ( x >= 0 && x <= config.getWorldWidth() && y >= 0 && y <= config.getWorldHeight() );
    }

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
    }

    public static void reset()
    {
        Fire.reset();
        sensCoverageRatios.clear();
        Arrays.fill( sensCoverageFrequency, 0 );
        totalCoveragePercentage = 0;
        wellCoveredPercentage   = 0;
    }

    public static Area occupiedArea()
    {
        Area occupied = new Area( buildings );
        occupied.add( Simulator.agentSpace() ); // dimzar: speed up line right here :)

        return occupied;
    }

    public static Area unoccupiedArea()
    {
        Area world = new Area( new Rectangle2D.Double( 0, 0, worldWidth, worldHeight ) );
        world.subtract( occupiedArea() );

        return world;
    }

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

    public static void scaleGraphics( Graphics2D g2, Dimension pixelScreenSize )
    {
        g2.scale( pixelScreenSize.width / worldWidth, pixelScreenSize.height / worldHeight );
    }

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
