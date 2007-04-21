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
import sim.Simulator;
import messageBoard.MessageBoard;
import env.Environment;
import statistics.Statistics;


public abstract class Agent extends Bobject implements Runnable
{
	//sensor arrays for agent, these arrays are to hold only those
	//objects that can be seen or heard.
	protected ArrayList<Agent> agentsSeen = null;
	protected ArrayList<Agent> agentsHeard = null;
	protected ArrayList<Obstacle> obstaclesSeen = null;
	protected ArrayList<Flag> flagsSeen = null;
	protected int msgID; //0 based
	protected int teamID;
	protected boolean isAlive = false;
	protected int damage = 1;
	protected int shotCounter = 0;
	protected int initialSoundRadius;
	protected double moveRadius;
	protected boolean beingShot = false;
	protected int threshold = 50;
	public enum agentType {BASE, AGENT};
	protected agentType myType;
	///AGENT STATES//////////////////
	public enum state {DEAD, FLAG_CARRIER, ATTACKING, FLEE, HIDE, CLEANUP, SEARCH, GUARD, RECOVER_FLAG, PATROL};
	protected state agent_state;
	//call planner
	public abstract void update();
	protected int fadeInRadius;
	protected int fadeInTransparency;
	protected boolean fadeIn;
	protected int currentFadeInRadius;
	
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
    protected Flag myFlag;
    protected boolean hasFlag = false;
    
    /**
     * Agent's "hit points" - might be used to keep track of damage.
     * Currently not used.
     */
    protected int health;
    protected int maxHealth;

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
    public PlanModule plan;

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
    	initialSoundRadius = soundRadius;
    	boundingRadius = config.getBoundingRadius();
    	color = config.getObjectColor();
    	teamID = config.getTeamID();
    	sightColor = config.getSightColor();
    	hearColor = config.getSoundColor();
    	maxHealth = config.getHealth();
    	health = maxHealth;
    	type = types.AGENT;
    	moveRadius = config.getSoundRadius();
    	fadeInRadius = config.getFadeInRadius();
    	fadeInTransparency = 0;
    	currentFadeInRadius = 0;
    	boundingShape = Bobject.shapes.CIRCLE;
    	isAlive = true;
    	myFlag = null;
    }

   

    /**
     * Sets the sleep time for agent thread (time between move() call executions).
     *
     * @param sleepTime thread sleep time in milliseconds
     */
    public void setObjectID(int newID)
	{
		if (newID >= 0)
			objectID = newID;
		idString = "Agent unit id = " + objectID;
        
	}
    public void setSleepTime( int sleepTime )
    {
        this.sleepTime = sleepTime;
    }
    
    public int getTeamID()
    {
    	return teamID;
    }
    public int getThreshold()
    {
    	return threshold;
    }
    public double getMoveRadius()
    {
    	return moveRadius;
    }
    
    public double getVelocity()
    {
        return velocity;
    }
    
    public void setIsAlive(boolean alive)
    {
    	isAlive = alive;
    }
    
    public void setTheta(double newT)
    {
    	location.setTheta(newT);
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
    
    public boolean getHasFlag()
    {
    	return hasFlag;
    }
    
    public boolean getIsBeingShot()
    {
    	return beingShot;
    }
    
    public Flag getMyFlag()
    {
    	return myFlag;
    }
    
    public boolean isMobile()
    {
    	return (myType == agentType.AGENT);
    }
    
    public boolean isBase()
    {
    	return (myType == agentType.BASE);
    }
     /**
     * Updates agent's location. Possible next location is selected according to
     * result returned from the planning module. Next location is within sensor
     * range of the agent.
     */
    public void move()
    {
    	checkSensors();
    	location = plan.getGoalLocation(this);
    }
    public boolean move(double heading)
    {
    	double radHeading = Math.toRadians(heading);
    	int tempRadius = (int)moveRadius;
    	boolean found = false;
    	AgentLocation newLoc = location;
    	while(!found && tempRadius > 0)
    	{
    		double newx = location.getX() + tempRadius * Math.cos(radHeading);
        	double newy = location.getY() - tempRadius * Math.sin(radHeading);
        	found = inWorld(newx, newy);
        	if (found)
        	{
	        	newLoc = new AgentLocation(newx, newy, heading);
	        	found = avoidObstacle(newLoc) && avoidAgent(newLoc);
        	}
        	tempRadius--;        	
    	}    	
    	if(found) {
    		location = newLoc;
    	}
    	//if(!found)
    //		System.out.println("move had "+getNumAgentsSeen()+" agents in the way and " + getNumObstaclesSeen() + " obstacles in the way");
    	return found;
    }
    
    public void turnMove(double heading)
    {
    	location.setTheta(heading);
    }
    
    public boolean maxMove(double heading)
    {
    	double radHeading = Math.toRadians(heading);
    	boolean found = false;
    	AgentLocation newLoc = location;
		double newx = location.getX() + moveRadius * Math.cos(radHeading);
    	double newy = location.getY() - moveRadius * Math.sin(radHeading);
    	found = inWorld(newx, newy);
    	if (found)
    	{
        	newLoc = new AgentLocation(newx, newy, heading);
        	found = avoidObstacle(newLoc) && avoidAgent(newLoc);
    	}
    	if(found)
    		location = newLoc;
    	return found;
    }

    public abstract void pickUpFlag(Flag f);
    public abstract void dropFlag();
    public void setAgentState(Agent.state newState)
    {
    	agent_state = newState; 
    }
    
    public int getHealth()
    {
    	return health;
    }
    public boolean getIsAlive()
    {
    	return isAlive;
    }
    public int getNumAgentsSeen()
    {
    	return agentsSeen.size();
    }
    public int getNumObstaclesSeen()
    {
    	return obstaclesSeen.size();
    }
    public void decrementHealth(int d)
    {
    	/*
    	int sightRed = sightColor.getRed();
    	int sightGreen = sightColor.getGreen();
    	int sightBlue = sightColor.getBlue();
    	int hearRed = hearColor.getRed();
    	int hearGreen = hearColor.getGreen();
    	int hearBlue = hearColor.getBlue();
    	*/
    	
    	if(isAlive)
    	{
    		beingShot = true;
        	
    		health = health - d;
	    	if (health <= 0)
	    		isAlive = false;
	    	/*
	    	if(sightColor.getRed()+d <= 252)
	    		sightRed = sightColor.getRed() + 3 * d;
	    	if(sightColor.getGreen()-d >= 3)
	    		sightGreen = sightColor.getGreen() - 3 * d;
	    	if(sightColor.getBlue()-d >= 3)
	    		sightBlue = sightColor.getBlue() - 3 * d;
	    	if(hearColor.getRed()+d <= 252)
	    		hearRed = hearColor.getRed() + d;
	    	if(hearColor.getGreen()-d >= 3)
	    		hearGreen = hearColor.getGreen() - d;
	    	if(hearColor.getBlue()-d >= 3)
	    		hearBlue = hearColor.getBlue() - d;
	    	
	    	sightColor = new Color(sightRed,sightGreen,sightBlue,sightColor.getAlpha());
	    	hearColor = new Color(hearRed,hearGreen,hearBlue,hearColor.getAlpha());
	    	*/
    	}
    	
    	if (!isAlive)
    	{
    		agent_state = state.DEAD;
    		System.out.println("Agent is Dead");
    	}
    }
    
    public void checkSensors()

    {
    	//wipe all sensor arrays then fill them again (no memory for the agents)
    	agentsSeen.clear();
    	agentsHeard.clear();
    	obstaclesSeen.clear();
    	flagsSeen.clear();
    	
    	//fill arraylists with all sensor information

    	agentsSeen.addAll(sensorSight.getSightAgents(this));
    	agentsHeard.addAll(sensorHearing.getHeardAgents(this));

    	obstaclesSeen.addAll(sensorSight.getSightObstacles(this));
    	flagsSeen.addAll(sensorSight.getSightFlags(this));
    }
    
    public void setMsgID(int newID)
    {
    	msgID = newID;
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
        health = config.getHealth();
        color = config.getObjectColor();
        hearColor = config.getSoundColor();
        sightColor = config.getSightColor();
        isAlive = true;
        agentsSeen.clear();
        agentsHeard.clear();
        obstaclesSeen.clear();
        flagsSeen.clear();
        hasFlag = false;
        shotCounter = 0;
        agent_state = plan.getInitialState();
        moveRadius = initialSoundRadius;
        fadeInRadius = config.getFadeInRadius();
        stop();
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
        agent_state = plan.getAgentState();

        loader = Class.forName( commClass, true, this.getClass().getClassLoader() );
        comm   = (CommModule) loader.getConstructor( aC ).newInstance( config );

        loader     = Class.forName( propulsionClass, true, this.getClass().getClassLoader() );
        propulsion = (PropulsionModule) loader.getConstructor( aC ).newInstance( config );

      //  location    = deployStrategy.getNextLocation( this );
        //config.getSensorColor() is called for both atm, this needs to be changed
    }
    
    protected boolean avoidObstacle(AgentLocation newLoc)
    {
    	boolean good = true;
    	double newX = newLoc.getX();
    	double newY = newLoc.getY();
    	Iterator<Obstacle> obs = this.getObstaclesSeen();
    	while ( obs.hasNext() && good)
    	{
    		Obstacle o = obs.next();
    		double dist = Math.hypot((newX - o.getLocation().getX()),(newY - o.getLocation().getY()));
    		double bound = this.getBoundingRadius() + o.getBoundingRadius();
    		if (bound >= dist)
    			good = false;
    	}
    	return good;
    }
    
    protected boolean avoidAgent(AgentLocation newLoc)
    {
    	boolean good = true;
    	double newX = newLoc.getX();
    	double newY = newLoc.getY();
    	Iterator<Agent> ag = this.getAgentsSeen();
    	while ( ag.hasNext())
    	{
    		Agent a = ag.next();
    		if (a.getIsAlive() && a.isMobile())
    		{
    			double dist  = Math.hypot((newX - a.getLocation().getX()), (newY - a.getLocation().getY()));
    			double bound = this.getBoundingRadius() + a.getBoundingRadius();
    			if (bound >= dist)
    				good = false;
    		}
    	}
    	return good;
    }
    
    protected boolean inWorld(double x, double y)
    {
    	double MaxX = Environment.groundShape().getMaxX();
    	double MaxY = Environment.groundShape().getMaxY();
    	return ((0 <= x) && (x < MaxX)) && ((0 <= y) && (y < MaxY)); 
    }
    
    public void shootAll()
    {
    	Iterator<Agent> ag = this.getAgentsSeen();
    	while (ag.hasNext())
    	{
    		Agent a = ag.next();
    		if (a.getIsAlive() && a.getTeamID() != teamID)
    		{
    			a.decrementHealth(damage);
    			Statistics.incDamageDone(objectID, damage);
    			Statistics.incEnemiesHit(objectID);
    	    	shotCounter = 5;
    		}
    	}
    }

    public void shootID(int ID)
    {
    	Iterator<Agent> ag = this.getAgentsSeen();
    	while (ag.hasNext())
    	{
    		Agent a = ag.next();
    		if (a.getObjectID() == ID && a.getIsAlive())
    		{
    			a.decrementHealth(damage);
    			Statistics.incDamageDone(objectID, damage);
    			Statistics.incEnemiesHit(objectID);
    			shotCounter = 5;
    		}
    	}
     }
    
    private double headingToCartesian(double head)
    {
    	double newHead = 0;
    	if(head >= 0 && head < 90)
    		newHead = 360 - head;
		else if (head >= 90 && head < 180)
			newHead = 180 + (180 - head);
		else if (head >= 180 && head < 270)
			newHead = 180 - (head - 180);
		else
			newHead = 360 - head;
    	return newHead;
    }
    private double cartesianToHeading(double cart)
    {
    	double head = 0;
    	if (cart >= 0 && cart < 90)
    		head = 360 - cart;
    	else if (head >= 90 && head < 180)
    		head = 180 + (180 - cart);
    	else if (head >= 180 && head < 270)
    		head = 180 - (cart - 180);
    	else
    		head = 360 - cart;
    	return head;
    }
    
    public double arctangentToGoal(AgentLocation goal)
    {
    	double x = location.getX();
    	double y = location.getY();
    	double x1 = goal.getX();
    	double y1 = goal.getY();
    	
    	double temp = Math.atan((x-x1)/(y-y1)) * 57.29577951;
    	temp = Math.abs(temp);
    	
    	if (x <= x1)
    	{
    		if (y <= y1)
    			temp = 90 - temp;
    		else
    			temp = 270 + temp;
    	}
    	else
    	{
    		if (y <= y1)
    			temp = 90 + temp;
    		else
    			temp = 270 - temp;
    	}
    	
    	return cartesianToHeading(temp);
    }
    
    public double moveToLocation( AgentLocation goal)
    {
    	double temp = headingToCartesian(location.getTheta());
    	double arc = arctangentToGoal(goal);
    	if (Math.abs(temp-arc) <= sensorSight.getHalfAngle() ||
    			Math.abs(temp - (arc + 360)) <= sensorSight.getHalfAngle() ||
    			Math.abs((temp + 360) - arc) <= sensorSight.getHalfAngle())
    		temp = arc;
    	else
    	{
    		//heading is in quad 2 or 3
    		if (location.getX() > goal.getX())
    		{
    			if (temp > arc)
    				temp = temp - sensorSight.getHalfAngle();
    			else
    				temp = temp + sensorSight.getHalfAngle();
    		}
    		//heading is in quad 1 or 4
    		else
    		{
    			if (location.getY() <= goal.getY())
    			{
    				if (temp > arc || (temp + 360) > arc)
    					temp = temp - sensorSight.getHalfAngle();
    				else
    					temp = temp + sensorSight.getHalfAngle();
    			}
    			else
    			{
    				if (temp > arc || temp > (arc + 360))
    					temp = temp - sensorSight.getHalfAngle();
    				else
    					temp = temp + sensorSight.getHalfAngle();
    			}
    		}
    	}
    	
    	temp = cartesianToHeading(temp);
    	return temp;
    	
    }
    
    public boolean closeToGoal(double threshold, AgentLocation goalLocation)
    {
    	double dist = Math.hypot(location.getX() - goalLocation.getX(), location.getY() - goalLocation.getY());
    	return dist <= threshold;
    }
    
    //TODO finish flee
    public double flee()
    {
    	return location.getTheta();
    }
    
    public void sendMessage(boolean needHelp, boolean opponentFlagSeen, 
    		AgentLocation opponentFlagLocation, boolean ourFlagSeen, 
    		AgentLocation ourFlagLocation)
    {
    	MessageBoard board = Simulator.teamBoards.get(teamID);
    	board.setCurrentHitPoints(msgID, health);
    	board.setCurrentState(msgID, agent_state);
    	board.setIsAlive(msgID, isAlive);
    	board.setMyId(msgID, objectID);
    	board.setMyLocation(msgID, location);
    	board.setNeedHelp(msgID, needHelp);
    	if(opponentFlagSeen)
    	{
    		board.setOpponentFlagLocation(opponentFlagLocation);
    	}
		board.setOpponentFlagSeen(opponentFlagSeen);
		if(ourFlagSeen)
		{
			board.setOurFlagLocation(location);
		}
		board.setOurFlagSeen(msgID, ourFlagSeen);
		if(hasFlag)
		{
			board.setWhoOwnsFlag(objectID);
			board.setOpponentFlagLocation(location);
		}
		board.setAgentsSeen(agentsSeen);
		board.setAgentsHeard(agentsHeard);
    }
    
    public double aimID(int ID)
    {
    	AgentLocation badGuy = Simulator.worldObjects.get(ID).getLocation();
    	return moveToLocation(badGuy);
    	
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
    	if (isAlive)
    	{
    		g2.setColor(this.color);
    		g2.fill(new Ellipse2D.Float((float)location.getX() - (float)boundingRadius,
				(float)location.getY() - (float)boundingRadius,
				2f * (float)boundingRadius,
				2f * (float)boundingRadius));
    		if (sight)
    		{
    			/*Check how much of each color should be drawn*/
    			int RED, GREEN, BLUE;
    			RED = (int)(255-(255 * (double)((double)health / (double)maxHealth)));
    			GREEN = (int)(255 * ((double)health / (double)maxHealth));
    			BLUE = (int)(255 * ((double)health / (double)maxHealth));
    			sightColor = new Color(RED,GREEN,BLUE, sightColor.getAlpha());
    			/*Now draw it*/
    			g2.setColor(this.sightColor);
    			g2.fillArc((int)location.getX() - (int)(sensorSight.getlength()), 
    					(int)location.getY() - (int)(sensorSight.getlength()), 
    					2 *(int)sensorSight.getlength(), 2* (int)sensorSight.getlength(),
    					(int)location.getTheta()- (int)(sensorSight.getArcAngle() / 2),
    					(int)sensorSight.getArcAngle());
    		}
		
    		if (hearing)
    		{
    			/*Check how much of each color should be drawn*/
    			int RED, GREEN, BLUE;
    			RED = (int)(255-(255 * (double)((double)health / (double)maxHealth)));
    			GREEN = (int)(255 * ((double)health / (double)maxHealth));
    			BLUE = (int)(255 * ((double)health / (double)maxHealth));
    			sightColor = new Color(RED,GREEN,BLUE, sightColor.getAlpha());
    			hearColor = new Color(RED,GREEN,BLUE, hearColor.getAlpha());
    			/*Now draw it*/
    			g2.setColor(this.hearColor);
    			g2.fill(new Ellipse2D.Float((float)location.getX() - (float)sensorHearing.getHearingRadius(),
    					(float)location.getY() - (float)sensorHearing.getHearingRadius(),
    					2f * (float)sensorHearing.getHearingRadius(),
    					2f * (float)sensorHearing.getHearingRadius()));
    		}
    	}
    	else
    	{
    		/*Our Agent is dead and we need to fade if cleanup is called*/
    		if(fadeIn)
    		{
    			int step = 3;
    			if(currentFadeInRadius + step <= fadeInRadius)
    			{
        			System.out.println("Fading In");
    				int newTransparency = fadeInTransparency + 255/ (fadeInRadius / step);
    				g2.setColor(new Color(255,255,255,newTransparency));
    				g2.fill(new Ellipse2D.Float((float)initialLocation.getX(), (float)initialLocation.getY(), fadeInRadius, fadeInRadius));
    				currentFadeInRadius += step;
    			}
    		}
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
            update();
        	stop();
        }
        else
        {
            while( agentThread != null )
            {
            	update();

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
    
    public void fadeIn()
    {
    	fadeIn = true;
    }
}
