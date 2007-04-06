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
import config.ConfigBobject;
import baseobject.*;
import java.util.ArrayList;
import java.util.Iterator; 
import java.awt.*;
import java.awt.geom.Ellipse2D;

import obstacle.Obstacle;


public abstract class Agent extends Bobject implements Runnable
{
	//sensor arrays for agent, these arrays are to hold only those
	//objects that can be seen or heard.
	protected ArrayList<Agent> agentsSeen = null;
	protected ArrayList<Agent> agentsHeard = null;
	protected ArrayList<Obstacle> obstaclesSeen = null;
	protected ArrayList<Flag> flagsSeen = null;
	int teamID;
    /**
     * Used to identify an agent thread. This becomes thread name.
     */
    protected String idString;

    /**
     * Sensor colors, sightColor and hearColor
     */
    protected Color sightColor;
    protected Color hearColor;

    /**
     * Agent's current speed. Currently not used.
     */
    protected double velocity = 0;
    
    /**
     * Agent's "hit points" - might be used to keep track of damage.
     * Currently not used.
     */
    protected int health;

    /**
     * Deployment strategy subsystem (detremines agent initial position).
     */
    public DeploymentStrategy deployStrategy;

    /**
     * Sensor subsystem (sesnor shape).
     */
    public SensorModule sensorSight;
    public SensorModule sensorHearing;

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
    public Agent( ConfigBobject config) throws Exception
    {
        this.config = config;
        String deployClass     = config.getDeploymentName();
        String sensorSightClass   = config.getSensorSightName();
        String sensorHearingClass = config.getSensorHearingName();
        String commClass       = config.getCommName();
        String planClass       = config.getPlanName();
        String propulsionClass = config.getPropulsionName();

        idString = "Agent unit id = " + objectID;
        initialize( deployClass, sensorSightClass, sensorHearingClass, planClass, commClass, propulsionClass );
        
        agentsSeen = new ArrayList<Agent>();
    	agentsHeard = new ArrayList<Agent>();
    	obstaclesSeen = new ArrayList<Obstacle>();
    	flagsSeen = new ArrayList<Flag>();
    	soundRadius = (int)config.getSoundRadius();
    	boundingRadius = config.getBoundingRadius();
    	color = config.getObjectColor();
    	teamID = config.getTeamID();
    	sightColor = config.getSightColor();
    	hearColor = config.getSoundColor();
    	type = types.AGENT;
    	
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
     * Gets the velocity of the agent.
     * @author jeff
     * @return agent velocity
     */
    public double getVelocity()
    {
        return velocity;
    }
    
    public int getSoundRadius()
    {
    	return (int)(velocity*soundRadius)+soundRadius;
    }

    /**
     *Gets the color of the sensor area.
     *
     * @return sightSensor color
     */
    public Color getSightColor()
    {
        return sightColor;
    }

    public Color getHearColor()
    {
    	return hearColor;
    }
    
     /**
     * Updates agent's location. Possible next location is selected according to
     * result returned from the planning module. Next location is within sensor
     * range of the agent.
     */
    public void move()
    {
    	checkSensors();
        AgentLocation goal = plan.getGoalLocation( this );
        location           =  goal;//propulsion.move( location, goal ); //
    }

    private void checkSensors()
    {
    	//wipe all sensor arrays then fill them again (no memory for the agents)
    	agentsSeen.clear();
    	agentsHeard.clear();
    	obstaclesSeen.clear();
    	flagsSeen.clear();
    	
    	//fill arraylists with all sensor information
    	agentsSeen.addAll(sensorSight.getSightAgents(this));
    	//agentsHeard.addAll(sensorHearing.getHeardAgents(this));
    	//obstaclesSeen.addAll(sensorSight.getSightObstacles(this));
    	flagsSeen.addAll(sensorSight.getSightFlags(this));
    }
    public Iterator<Agent> getAgentsSeen()
    {
    	return agentsSeen.iterator();
    }
    public Iterator<Agent> getAgentsHeard()
    {
    	return agentsHeard.iterator();
    }
    public Iterator<Obstacle> getObstaclesSeen()
    {
    	return obstaclesSeen.iterator();
    }
    public Iterator<Flag> getFlagsSeen()
    {
    	return flagsSeen.iterator();
    }
    /**
     * Resents agent properties. This is used to restart the simulation. Currently
     * only resets agent location, but all subsystem modules could be reset here,
     * if the need arises.
     */
    public void reset()
    {
        location = initialLocation;
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
    private void initialize( String deployClass, String sensorSightClass, String sensorHearingClass, String planClass, String commClass, String propulsionClass ) throws Exception
    {
        Class aC       = ConfigBobject.class;
        Class loader   = Class.forName( deployClass, true, this.getClass().getClassLoader() );
        deployStrategy = (DeploymentStrategy) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( sensorSightClass, true, this.getClass().getClassLoader() );
        sensorSight = (SensorModule) loader.getConstructor( aC ).newInstance( config );
        
        loader = Class.forName( sensorHearingClass, true, this.getClass().getClassLoader() );
        sensorHearing = (SensorModule) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( planClass, true, this.getClass().getClassLoader() );
        plan   = (PlanModule) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( commClass, true, this.getClass().getClassLoader() );
        comm   = (CommModule) loader.getConstructor( aC ).newInstance( config );

        loader     = Class.forName( propulsionClass, true, this.getClass().getClassLoader() );
        propulsion = (PropulsionModule) loader.getConstructor( aC ).newInstance( config );

      //  location    = deployStrategy.getNextLocation( this );
        //config.getSensorColor() is called for both atm, this needs to be changed
    }

    /**
     * Starts the simulation (creates a separate thread for current agent and executes
     * the code in run() method).
     *
     * @param oneStep specifies if an agnet should perform only one step of execution,
     * i.e. move only once
     */
    
    public void draw (Graphics2D g2, boolean sight, boolean hearing)
    {
    	g2.setColor(this.color);
		g2.fill(new Ellipse2D.Float((float)location.getX() - (float)boundingRadius,
				(float)location.getY() - (float)boundingRadius,
				2f * (float)boundingRadius,
				2f * (float)boundingRadius));
		if (sight)
		{
			g2.setColor(this.sightColor);
			g2.fillArc((int)location.getX() - (int)(sensorSight.getlength() / 2), 
					(int)location.getY() - (int)(sensorSight.getlength() / 2), 
					(int)sensorSight.getlength(), (int)sensorSight.getlength(),
					(int)location.getTheta()- (int)(sensorSight.getArcAngle() / 2),
					(int)sensorSight.getArcAngle());
		}
		
		if (hearing)
		{
			g2.setColor(this.hearColor);
			g2.fill(new Ellipse2D.Float((float)location.getX() - (float)sensorHearing.getHearingRadius(),
    				(float)location.getY() - (float)sensorHearing.getHearingRadius(),
    				2f * (float)sensorHearing.getHearingRadius(),
    				2f * (float)sensorHearing.getHearingRadius()));
		}
    }
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
