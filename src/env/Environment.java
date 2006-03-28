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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;
import static java.lang.Math.max;
import static java.lang.Math.round;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Environment
{
    private static ConfigEnv config;
    private static int gridSize;
    private static ArrayList<Rectangle2D> grid;
    private static int[] sensCoverageFrequency;
    private static ArrayList<Double> sensCoverageRatios;
    private static ArrayList<Polygon> buildings;
    private static HashMap<Integer, ArrayList<Ellipse2D.Double>> fires;

    private Environment() {}

    public static boolean contains( double x, double y )
    {
        return ( x >= 0 && x <= config.getWorldWidth() && y >= 0 && y <= config.getWorldHeight() );
    }

    public static void load( String envConfigFileName ) throws Exception
    {
        config = new ConfigEnv( ClassLoader.getSystemClassLoader().getResource( envConfigFileName ).getPath() );
        grid = new ArrayList<Rectangle2D>();
        gridSize = config.getGridSize();

        for ( int i = 0; i < config.getWorldWidth(); i += gridSize )
        {
            for ( int j = 0; j < config.getWorldHeight(); j += gridSize )
            {
                grid.add( new Rectangle2D.Double( i, j, gridSize, gridSize ) );
            }
        }

        sensCoverageFrequency = new int[grid.size()];
        sensCoverageRatios = new ArrayList<Double>( grid.size() );

        loadBuildings( config.getBuildingsFileName() );
        // populate the fires hashmap (dimzar)
    }

    public static Area occupiedArea()
    {
        Area occupied = new Area();

        for ( Polygon building : buildings )
        {
            occupied.add( new Area( building ) );
        }

        // dimzar: speed up line right here :)
        occupied.add( Simulator.agentSpace() );

        return occupied;
    }

    public static Area unoccupiedArea()
    {
        Area world = new Area( new Rectangle2D.Double( 0, 0, config.getWorldWidth(), config.getWorldHeight() ) );
        world.subtract( occupiedArea() );

        return world;
    }

    public static void update()
    {
        int gridRowWidth = config.getWorldWidth() / gridSize;
        Iterator<Agent> iter = Simulator.agentsIterator();

        while ( iter.hasNext() )
        {
            Agent a = iter.next();

            Area sensFootprint = a.getSensorView();
            Rectangle2D bounds = sensFootprint.getBounds2D();

            int startX = (int)round( bounds.getX() / gridSize );
            int startY = (int)round( bounds.getY() / gridSize );
            int endX = (int)round( ( bounds.getX() + bounds.getWidth() ) / gridSize );
            int endY = (int)round( ( bounds.getY() + bounds.getHeight() ) / gridSize );

            for ( int i = startX; i < endX; i++ )
            {
                for ( int j = startY; j < endY; j++ )
                {
                    int index = i * gridRowWidth + j;
                    if ( sensFootprint.intersects( grid.get( index ).getBounds2D() ) ) { sensCoverageFrequency[index]++; }
                }
            }
        }
    }

    private static void loadBuildings( String buildingsFileName ) throws Exception
    {
        StreamTokenizer st = new StreamTokenizer( new BufferedReader( new FileReader( buildingsFileName ) ) );
        st.ordinaryChars( '+', '9' );
        st.wordChars( ' ', '~' );
        st.commentChar( '#' );

        buildings = new ArrayList<Polygon>();

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
                x[i] = Double.valueOf( xPoints[i] ).intValue();
                y[i] = Double.valueOf( yPoints[i] ).intValue();
            }

            buildings.add( new Polygon( x, y, n ) );
        }
    }

    public static void scaleGraphics( Graphics2D g2, Dimension pixelScreenSize )
    {
        g2.scale( pixelScreenSize.width / config.getWorldWidth(), pixelScreenSize.height / config.getWorldHeight() );
    }

    public static void scaleDrawing( Component drawing, Dimension pixelScreenSize )
    {
        int zoom = optimalZoom( pixelScreenSize );
        Dimension scaledSize = new Dimension( zoom * config.getWorldWidth() + 1, zoom * config.getWorldHeight() + 1 );

        drawing.setSize( scaledSize );
        drawing.setPreferredSize( scaledSize );
    }

    public static double aspectRatio()
    {
        return config.getWorldWidth() / config.getWorldHeight();
    }

    public static int optimalZoom( Dimension pixelScreenSize )
    {
        return max( 1, max( pixelScreenSize.width / config.getWorldWidth(), pixelScreenSize.height / config.getWorldHeight() ) );
    }

    public static Iterator<Polygon> buildingsIterator()
    {
        return buildings.iterator();
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
