package agent;/**
 * @(#) Agent.java
 */

import sim.DeploymentStrategy;
import sim.Simulator;

import java.awt.*;
import java.util.ArrayList;

public abstract class Agent
{
    private static int id = 0;

    // final modifier does not work
    private static Simulator sim;
    private static DeploymentStrategy deployStrategy;

    private AgentLocation loc;

    private double speed;

    private double damage;

    private PropulsionModule propulsion;

    private SensorModule sensor;

    private CommunicationModule communication;

    private PlanningModule plan;


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

    public static void setProperties( Simulator simulator, DeploymentStrategy strategy )
    {
        sim = simulator;
        deployStrategy = strategy;
    }

    /**
     * Updates a position on the blacboard.
     */
    public void move()
    {
        ArrayList<Shape> sensorView = sensor.getView( loc );
        AgentLocation goal = plan.getGoalLocation( sensorView );
        loc = propulsion.moveToward( goal );
        sim.blackBoard.agentMoved( this );
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
