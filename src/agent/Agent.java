package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.comm.CommunicationModule;
import agent.plan.PlanModule;
import agent.propulsion.PropulsionModule;
import agent.sensor.SensorModule;
import sim.BlackBoard;
import sim.Simulator;

import java.awt.*;
import java.util.ArrayList;

public abstract class Agent
{
    /**
     * Tells if the agent properties need to be set.
     */
    private static boolean init = false;

    /**
     * Agent unique Identification Number.
     */
    private static int id = 0;

    /**
     * Agent deployment strategy. Determines initial position.
     */
    private static DeploymentStrategy deployStrategy;

    private AgentLocation location;

    private double velocity;

    private double health;

    private PropulsionModule propulsion;

    private SensorModule sensor;

    private CommunicationModule communication;

    private PlanModule plan;

    /**
     * Agent constructor. Creates a new agent and sets the initial location
     * according to the deployment strategy.
     */
    public Agent()
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
        ArrayList<Shape> sensorView = sensor.getView( location );
        AgentLocation goal = plan.getGoalLocation( sensorView );
        location = propulsion.moveToward( goal );
        BlackBoard.agentMoved( this );
    }

    private void setProperties()
    {
        try
        {
            Class ds = Class.forName( Simulator.config.getAgentDeploymentStrategy(), true, this.getClass().getClassLoader() );
            deployStrategy = (DeploymentStrategy) ds.newInstance();
            init = true;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
