package env;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
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
    private static ConfigEnv config;
    private static int worldWidth, worldHeight;
    private static int gridSize, gridRowSize, gridColSize;
    private static ArrayList<Rectangle2D> grid;
    private static int[] sensCoverageFrequency;
    private static ArrayList<Double> sensCoverageRatios;
    private static Area buildings;
    private static ArrayList<Polygon> buildingList;
    private static Area fires;
    private static HashMap<Integer, ArrayList<Polygon>> fireList;
    private static Random fireRand;

    public static boolean contains( double x, double y )
    {
        return ( x >= 0 && x <= config.getWorldWidth() && y >= 0 && y <= config.getWorldHeight() );
    }

    public static void load( String envConfigFileName ) throws Exception
    {
        config = new ConfigEnv( ClassLoader.getSystemClassLoader().getResource( envConfigFileName ).getPath() );
        worldWidth = config.getWorldWidth();
        worldHeight = config.getWorldHeight();
        grid = new ArrayList<Rectangle2D>();
        gridSize = config.getGridSize();
        gridRowSize = worldWidth / gridSize;
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
        sensCoverageRatios = new ArrayList<Double>( grid.size() );

        loadBuildings( config.getBuildingsFileName() );
        loadFires( config.getFiresFileName() );
    }

    public static void reset()
    {
        fires.reset();
        sensCoverageRatios.clear();

        for ( int i = 0; i < sensCoverageFrequency.length; i++ )
        {
            sensCoverageFrequency[i] = 0;
        }
    }

    public static Area occupiedArea()
    {
        Area occupied = new Area( buildings );
        occupied.add( Simulator.agentSpace() );  // dimzar: speed up line right here :)

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
            Agent a = iter.next();
            Area sensFootprint = a.getSensorView();
            Rectangle2D bounds = sensFootprint.getBounds2D();

            int startX = (int) max( 0, floor( bounds.getX() / gridSize ) );
            int startY = (int) max( 0, floor( bounds.getY() / gridSize ) );
            int endX = (int) min( gridRowSize, ceil( ( bounds.getX() + bounds.getWidth() ) / gridSize ) );
            int endY = (int) min( gridColSize, ceil( ( bounds.getY() + bounds.getHeight() ) / gridSize ) );

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

        // introduce fires
        Integer curTime = Simulator.getTime();
        ArrayList<Polygon> list = fireList.get( curTime );

        if ( list != null )
        {
            for ( Polygon fire : list )
            {
                fires.add( new Area( fire ) );
            }
        }
    }

    private static void loadBuildings( String buildingsFileName ) throws Exception
    {
        buildings = new Area();
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
            if ( n != yPoints.length ) { throw new ParseException( "building vertex X/Y list length mismatch", n ); }

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
        fireRand = new Random( config.getFireSeed() );
        fires = new Area();
        fireList = new HashMap<Integer, ArrayList<Polygon>>();

        StreamTokenizer st = new StreamTokenizer( new BufferedReader( new FileReader( firesFileName ) ) );
        st.ordinaryChars( '+', '9' );
        st.wordChars( ' ', '~' );
        st.commentChar( '#' );

        while ( st.nextToken() != StreamTokenizer.TT_EOF )
        {
            String[] fireDesc = st.sval.split( "\\," );
            if ( fireDesc.length != 5 ) { throw new ParseException( "fire description number of arguments", fireDesc.length ); }
            Integer key = Integer.valueOf( fireDesc[0] );

            if ( fireList.containsKey( key ) )
            {
                fireList.get( key ).add( buildFire( Double.parseDouble( fireDesc[1] ),
                                                    Double.parseDouble( fireDesc[2] ),
                                                    Double.parseDouble( fireDesc[3] ),
                                                    Double.parseDouble( fireDesc[4] ) ) );
            }
            else
            {
                ArrayList<Polygon> value = new ArrayList<Polygon>();
                value.add( buildFire( Double.parseDouble( fireDesc[1] ),
                                      Double.parseDouble( fireDesc[2] ),
                                      Double.parseDouble( fireDesc[3] ),
                                      Double.parseDouble( fireDesc[4] ) ) );
                fireList.put( key, value );
            }
        }
    }

    private static Polygon buildFire( double x, double y, double width, double height )
    {
        double originX = x + width / 2, originY = y + height / 2, avgR = ( width + height ) / 2;
        Polygon fire = new Polygon();

        for ( int i = 0; i < 300; i++ )
        {
            double theta = i * PI / 150, r = ( 1 + .5 * fireRand.nextGaussian() ) * avgR;
            fire.addPoint( (int) round( r * cos( theta ) + originX ), (int) round( r * sin( theta ) + originY ) );
        }

        return fire;
    }

    public static void scaleGraphics( Graphics2D g2, Dimension pixelScreenSize )
    {
        g2.scale( pixelScreenSize.width / worldWidth, pixelScreenSize.height / worldHeight );
    }

    public static void scaleDrawing( Component drawing, Dimension pixelScreenSize )
    {
        int zoom = optimalZoom( pixelScreenSize );
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
        return fires;
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
        sensCoverageRatios.clear();
        double max = 1;

        for ( int val : sensCoverageFrequency )
        {
            if ( val > max ) { max = val; }
        }

        for ( int ratio : sensCoverageFrequency )
        {
            sensCoverageRatios.add( ratio / max );
        }

        return sensCoverageRatios.iterator();
    }
}
