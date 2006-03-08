package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.comm.CommunicationModule;
import agent.deployment.DeploymentStrategy;
import agent.plan.PlanModule;
import agent.propulsion.PropulsionModule;
import agent.sensor.SensorModule;
import sim.BlackBoard;
import sim.Simulator;

import java.awt.geom.Area;

public abstract class Agent
{
    /**
     * Tells if the agent properties need to be set.
     */
    protected static boolean init = false;

    /**
     * Agent unique Identification Number.
     */
    protected static int id = -1;

    /**
     * Agent deployment strategy. Determines initial position.
     */
    protected static DeploymentStrategy deployStrategy;

    protected AgentLocation location;

    protected double velocity;

    protected double health;

    protected SensorModule sensor;

    protected PlanModule plan;
    
    protected CommunicationModule communication;

    protected PropulsionModule propulsion;


    /**
     * Agent constructor. Creates a new agent and sets the initial location
     * according to the deployment strategy.
     */
    public Agent() throws Exception
    {
        if ( !init )
        {
            setProperties();
        }
        id++;
    }

    /**
     * Gets the Identification Number of the agent.
     *
     * @return unique agent ID
     */
    public int getId()
    {
        return id;
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
     * Updates agent position on the blacboard.
     */
    public void move()
    {
        Area sensorView    = sensor.getView( location );
        AgentLocation goal = plan.getGoalLocation( location, sensorView );
        location           = propulsion.move( location, goal );
    }

    public abstract Area getBodyArea();

    private static abstract void setProperties() throws Exception;
}
