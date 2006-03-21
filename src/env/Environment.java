package env;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import config.ConfigEnv;
import sim.Simulator;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Environment
{
    private static ArrayList<Polygon> buildings;
    private static HashMap<Integer, ArrayList<Ellipse2D.Double>> fires;
    private static ConfigEnv config;

    private Environment()
    {
    }

    public static boolean contains( double x, double y )
    {
        return ( x >= 0 && x <= config.getWorldWidth() && y >= 0 && y <= config.getWorldHeight() );
    }

    public static void load( String envConfigFileName ) throws Exception
    {
        config = new ConfigEnv( ClassLoader.getSystemClassLoader().getResource( envConfigFileName ).getPath() );

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
            if ( n != yPoints.length )
            {
                throw new ParseException( "building vertex X/Y list length mismatch", n );
            }

            int x[] = new int[n], y[] = new int[n];
            for ( int i = 0; i < n; i++ )
            {
                x[i] = Double.valueOf( xPoints[i] ).intValue();
                y[i] = Double.valueOf( yPoints[i] ).intValue();
            }
            buildings.add( new Polygon( x, y, n ) );
        }
    }

    public static void scaleGraphics( Graphics2D g2, int pixelWidth, int pixelHeight )
    {
        g2.scale( pixelWidth / config.getWorldWidth(), pixelHeight / config.getWorldHeight() );
    }

    // provide a buch of Iterator-based query methods for the GUI to get info on where the Buildings, Fire, and Agents are.
    public static Iterator<Polygon> buildingsIterator()
    {
        return buildings.iterator();
    }
}
