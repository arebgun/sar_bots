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
    private static int id = 0;

    private static DeploymentStrategy deployStrategy;

    private AgentLocation loc;

    private double speed;

    private double damage;

    private PropulsionModule propulsion;

    private SensorModule sensor;

    private CommunicationModule communication;

    private PlanModule plan;

    private boolean init = false;

    public Agent()
    {
        if ( !init )
        {
            setProperties();
        }

        loc = deployStrategy.getNextLocation( id );
        id++;
    }

    public int getId()
    {
        return id;
    }

    public AgentLocation getLocation()
    {
        return loc;
    }

    /**
     * Updates a position on the blacboard.
     */
    public void move()
    {
        ArrayList<Shape> sensorView = sensor.getView( loc );
        AgentLocation goal = plan.getGoalLocation( sensorView );
        loc = propulsion.moveToward( goal );
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
