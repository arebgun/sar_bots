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

    protected AgentLocation location;

    protected double velocity;

    protected double health;

    protected DeploymentStrategy deployStrategy;

    protected SensorModule sensor;

    protected PlanModule plan;

    protected CommunicationModule communication;

    protected PropulsionModule propulsion;

    /**
     * Agent constructor. Creates a new agent and sets the initial location
     * according to the deployment strategy.
     */
    public Agent()
    {
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
        AgentLocation goal = plan.getGoalLocation( location, sensorView );
        location = propulsion.move( location, goal );
    }

    /**
     * Initializes agent deployment strategy and all subsystems:
     * sesnor module, planning module, communication module,
     * propulsion module and initial (deployment location).
     *
     * @param deployClass class to use for deployment strategy
     * @param sensorClass class to use for sensor module (class must implement SensorModule interface)
     * @param sensorRange used to create and initialize the range of sensor module
     * @param planClass class to use for planning module (class must implement PlanModule interface)
     * @param commClass class to use for communication module (class must implement CommunicationModule interface)
     * @param commRange used to create and initialize the range of communication module
     * @param propulsionClass class to use for propulsion module (class must implement PropulsionModule interface)
     * @throws Exception
     */
    public void initialize( String deployClass, String sensorClass, double sensorRange, String planClass, String commClass, double commRange, String propulsionClass ) throws Exception
    {
        if ( init ) { return; }

        Class loader = Class.forName( deployClass, true, this.getClass().getClassLoader() );
        deployStrategy = (DeploymentStrategy) loader.newInstance();

        loader = Class.forName( sensorClass, true, this.getClass().getClassLoader() );
        sensor = (SensorModule) loader.getConstructor( Class.forName( "Double" ) ).newInstance( sensorRange );

        loader = Class.forName( planClass, true, this.getClass().getClassLoader() );
        plan = (PlanModule) loader.newInstance();

        loader = Class.forName( commClass, true, this.getClass().getClassLoader() );
        communication = (CommunicationModule) loader.getConstructor( Class.forName( "Double" ) ).newInstance( commRange );

        loader = Class.forName( propulsionClass, true, this.getClass().getClassLoader() );
        propulsion = (PropulsionModule) loader.newInstance();

        location = deployStrategy.getNextLocation( id );

        init = true;
    }

    public abstract Area getBodyArea();
}
