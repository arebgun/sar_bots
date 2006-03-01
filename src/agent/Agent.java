package agent;/**
 * @(#) Agent.java
 */

import agent.sensor.SensorModule;
import agent.plan.PlanModule;
import agent.propulsion.PropulsionModule;
import agent.comm.CommunicationModule;
import sim.Simulator;
import sim.BlackBoard;

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

    public static void setProperties() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
	deployStrategy = ( DeploymentStrategy ) Class.forName( Simulator.config.getAgentDeploymentStrategy() ).newInstance();
    }

    public Agent()
    {
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
}

/*
public void setLocation( final Point2D location )
{
    // do bounds checking on location
    loc = location;
}


public void setOrientation( final double orientation )
{
    // normalize orient 0 <= orient < 360
    orient = orientation;
}
*/
