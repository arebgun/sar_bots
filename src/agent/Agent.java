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
    protected int unitID;
    protected String idString;

    protected ConfigAgent config;
    protected Color sensorColor;

    /**
     * Agent deployment strategy. Determines initial position.
     */
    protected AgentLocation location;

    protected double velocity;
    protected double health;

    protected DeploymentStrategy deployStrategy;
    protected SensorModule sensor;
    protected PlanModule plan;
    protected CommModule comm;
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

    public int getSleepTime()
    {
        return sleepTime;
    }

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

    public Area getSensorView()
    {
        return sensor.getView( location );
    }

    public Color getSensorColor()
    {
        return sensorColor;
    }

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
     * Updates agent's location.
     */
    public void move()
    {
        Area sensorView    = sensor.getView( location );
        AgentLocation goal = plan.getGoalLocation( location, sensorView );
        location           = propulsion.move( location, goal );
    }

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

    public void start(boolean oneStep)
    {
        if ( agentThread == null ) { agentThread = new Thread( this, idString ); }
        this.oneStep = oneStep;
        agentThread.start();
    }

    public void stop()
    {
        agentThread = null;
    }

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
