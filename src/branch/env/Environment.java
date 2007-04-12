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


import config.ConfigEnv;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.*;
import java.util.*;
// TODO: remove Area and all of its functions from Environment including
// import java.awt.geom.Area and import java.awt.geom.Rectangle2D
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

// TODO: Remove Area from Environment (such as buildings)
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
    private static Rectangle worldSize;
    
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
        worldSize = new Rectangle(worldWidth, worldHeight);

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

    }

    /**
     * Resets the world to the initial state, initializes the statistical measures, clears the fire information.
     */
    public static void reset()
    {
        sensCoverageRatios.clear();
        Arrays.fill( sensCoverageFrequency, 0 );
        totalCoveragePercentage = 0;
        wellCoveredPercentage   = 0;
    }

    /**
     * Returns the size of the world in the form of a Rectangle
     */
    public static Rectangle groundShape()
    {
    	return worldSize;
    }
 
    //TODO: Update for environment might be used to update the statistics for each "turn"
    /**
     * Advances the state of the simulation one time step; introduces fires, computes detection and coverage statistics.
     */
    public static void update()
    {
    	
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
}
