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
    protected static int id = 0;

    /**
     * Agent deployment strategy. Determines initial position.
     */
    protected static DeploymentStrategy deployStrategy;


    protected Area body;

    protected AgentLocation location;

    protected double velocity;

    protected double health;

    protected PropulsionModule propulsion;

    protected SensorModule sensor;

    protected CommunicationModule communication;

    protected PlanModule plan;

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

        location = deployStrategy.getNextLocation( id );
        BlackBoard.agentMoved( this );
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
        Area sensorView = sensor.getView( location );
        AgentLocation goal = plan.getGoalLocation( sensorView );
        location = propulsion.move( location, goal );
    }


    public Area getBodyArea()
    {
        return body;
    }


    private void setProperties() throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        Class ds = Class.forName( Simulator.config.agentDeploymentStrategy(), true, this.getClass().getClassLoader() );
        deployStrategy = (DeploymentStrategy) ds.newInstance();
        init = true;
    }
}
