package agent;

/*
 * Class Name:    agent.Agent
 * Last Modified: 4/30/2006 10:38
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import agent.comm.CommModule;
import agent.deployment.DeploymentStrategy;
import agent.plan.PlanModule;
import agent.propulsion.PropulsionModule;
import agent.sensor.SensorModule;
import config.ConfigAgent;

import java.awt.*;
import java.awt.geom.*;

public abstract class Agent implements Runnable
{
    /**
     * Agent unique ID. Can be used to identify agents onscreen or
     * perform certain operations on specified agent.
     */
    protected int unitID;

    /**
     * Used to identify an agent thread. This becomes thread name.
     */
    protected String idString;

    /**
     * Configuration class that specifies all agent properties.
     */
    protected ConfigAgent config;

    /**
     * Sensor color.
     */
    protected Color sensorColor;

    /**
     * Agent deployment strategy. Determines initial position.
     */
    protected AgentLocation location;

    /**
     * Agent's current speed. Currently not used.
     */
    protected double velocity;

    /**
     * @author jeff
     * Agent's current sound radius.
     */
    protected double soundRadius;
    
    /**
     * Agent's "hit points" - might be used to keep track of damage.
     * Currently not used.
     */
    protected int health;

    /**
     * Deployment strategy subsystem (detremines agent initial position).
     */
    protected DeploymentStrategy deployStrategy;

    /**
     * Sensor subsystem (sesnor shape).
     */
    protected SensorModule sensor;

    /**
     * Planning subsystem (AI algorithm goes here)
     */
    protected PlanModule plan;

    /**
     * Communication subsystem (inter-agent communication).
     */
    protected CommModule comm;

    /**
     * Propulsion subsystem (max speed, intertia, current speed, etc.).
     */
    protected PropulsionModule propulsion;

    private Thread agentThread;
    private int sleepTime;
    private boolean oneStep;

    /**
     * Agent constructor. Creates a new agent.
     */
    public Agent( ConfigAgent config) throws Exception
    {
        this.config = config;
        String deployClass     = config.getDeploymentName();
        String sensorClass     = config.getSensorName();
        String commClass       = config.getCommName();
        String planClass       = config.getPlanName();
        String propulsionClass = config.getPropulsionName();

        idString = "Agent unit id = " + unitID;
        initialize( deployClass, sensorClass, planClass, commClass, propulsionClass );
    }

    /**
     * Gets the Identification Number of the agent.
     *
     * @return unique agent ID
     */
    public int getID()
    {
        return unitID;
    }

    /**
     * Sets the sleep time for agent thread (time between move() call executions).
     *
     * @param sleepTime thread sleep time in milliseconds
     */
    public void setSleepTime( int sleepTime )
    {
        this.sleepTime = sleepTime;
    }

    /**
     * Gets the location of the agent.
     *
     * @return agent location on the map
     */
    public AgentLocation getLocation()
    {
        return location;
    }
    
    /**
     * Gets the velocity of the agent.
     * @author jeff
     * @return agent velocity
     */
    public double getVelocity()
    {
        return velocity;
    }

    /**
     * Gets the sound radius of the agent.
     * @author jeff
     * @return agent soundRadius
     */
    public double getSoundRadius()
    {
    	return soundRadius;
    }
    
    public Area getSensorView()
    {
        return sensor.getView( location );
    }

    /**
     *Gtes the color of the sensor area.
     *
     * @return sensor color
     */
    public Color getSensorColor()
    {
        return sensorColor;
    }

    /**
     * Calculates and returns agent total body area (wings + body).
     *
     * @return total area of the agent (wings + body)
     */
// TODO: remove Area from getBodyArea (possibly remove the entire fuction)
    public Area getBodyArea()
    {
        double wingSpan   = config.getWingSpan();
        double dimUnit    = wingSpan / 3;
        double wingWidth  = dimUnit;
        double bodyWidth  = dimUnit;
        double bodyLength = 5 * dimUnit;

        double x = location.getX();
        double y = location.getY();
        double t = location.getTheta();

        Area body  = new Area( new Ellipse2D.Double( x - 3 * dimUnit,   y - dimUnit / 2,  bodyLength, bodyWidth ) );
        Area wings = new Area( new Ellipse2D.Double( x - wingWidth / 2, y - wingSpan / 2, wingWidth,  wingSpan ) );
        body.add( wings );
        //TODO: DIMZAR-20060320: is this the right point to rotate about?  Do we care?
        body.transform( AffineTransform.getRotateInstance( t, x, y ) );

        return body;
    }

    /**
     * Updates agent's location. Possible next location is selected according to
     * result returned from the planning module. Next location is within sensor
     * range of the agent.
     */
// TODO: remove Area from move
    public void move()
    {
        Area sensorView    = sensor.getView( location );
        AgentLocation goal = plan.getGoalLocation( location, sensorView );
        location           = propulsion.move( location, goal );
    }

    /**
     * Resents agent properties. This is used to restart the simulation. Currently
     * only resets agent location, but all subsystem modules could be reset here,
     * if the need arises.
     */
    public void reset()
    {
        location = deployStrategy.getNextLocation( unitID );
        // TODO: add code to read a config file in case its changed
    }

    /**
     * Initializes agent deployment strategy and all subsystems:
     * sesnor module, planning module, communication module,
     * propulsion module and initial (deployment location).
     *
     * @param deployClass class to use for deployment strategy
     * @param sensorClass class to use for sensor module (must be a subclass of SensorModule)
     * @param planClass class to use for planning module (must be a subclass of PlanModule)
     * @param commClass class to use for communication module (must be a subclass of CommunicationModule)
     * @param propulsionClass class to use for propulsion module (must be a subclass of PropulsionModule)
     * @throws Exception
     */
    private void initialize( String deployClass, String sensorClass, String planClass, String commClass, String propulsionClass ) throws Exception
    {
        Class aC       = ConfigAgent.class;
        Class loader   = Class.forName( deployClass, true, this.getClass().getClassLoader() );
        deployStrategy = (DeploymentStrategy) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( sensorClass, true, this.getClass().getClassLoader() );
        sensor = (SensorModule) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( planClass, true, this.getClass().getClassLoader() );
        plan   = (PlanModule) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( commClass, true, this.getClass().getClassLoader() );
        comm   = (CommModule) loader.getConstructor( aC ).newInstance( config );

        loader     = Class.forName( propulsionClass, true, this.getClass().getClassLoader() );
        propulsion = (PropulsionModule) loader.getConstructor( aC ).newInstance( config );

        location    = deployStrategy.getNextLocation( unitID );
        sensorColor = config.getSensorColor();
    }

    /**
     * Starts the simulation (creates a separate thread for current agent and executes
     * the code in run() method).
     *
     * @param oneStep specifies if an agnet should perform only one step of execution,
     * i.e. move only once
     */
    public void start( boolean oneStep )
    {
        if ( agentThread == null ) { agentThread = new Thread( this, idString ); }
        this.oneStep = oneStep;
        agentThread.start();
    }

    /**
     * Stops current agent.
     */
    public void stop()
    {
        agentThread = null;
    }

    /**
     * Moves the current agent. If oneStep is true moves only one step,
     * otherwise agent moves until Stop button is pressed.
     */
    public void run()
    {
        if( oneStep && agentThread != null )
        {
            move();
            stop();
        }
        else
        {
            while( agentThread != null )
            {
                move();

                try
                {
                    Thread.sleep( sleepTime );
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
