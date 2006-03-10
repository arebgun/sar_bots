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
import config.ConfigAgent;

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
    protected ConfigAgent config;
    protected DeploymentStrategy deployStrategy;
    protected SensorModule sensor;
    protected PlanModule plan;
    protected CommunicationModule communication;
    protected PropulsionModule propulsion;

    /**
     * Agent constructor. Creates a new agent.
     */
    public Agent( ConfigAgent config ) throws Exception
    {
        this.config = config;

        String deployClass = config.getAgentDeploymentStrategy();
        String sensorClass = config.getAgentSensor();
        String commClass = config.getAgentComm();
        String planClass = config.getAgentPlan();
        String propulsionClass = config.getAgentPropulsion();

        initialize( deployClass, sensorClass, planClass, commClass, propulsionClass );

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

    public ConfigAgent getConfig()
    {
        return config;
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
     * @param sensorClass class to use for sensor module (must be a subclass of SensorModule)
     * @param planClass class to use for planning module (must be a subclass of PlanModule)
     * @param commClass class to use for communication module (must be a subclass of CommunicationModule)
     * @param propulsionClass class to use for propulsion module (must be a subclass of PropulsionModule)
     * @throws Exception
     */
    private void initialize( String deployClass, String sensorClass, String planClass, String commClass, String propulsionClass ) throws Exception
    {
        Class aC = ConfigAgent.class;

        if ( !init )
        {
            Class loader = Class.forName( deployClass, true, this.getClass().getClassLoader() );
            deployStrategy = (DeploymentStrategy) loader.newInstance();

            loader = Class.forName( sensorClass, true, this.getClass().getClassLoader() );
            sensor = (SensorModule) loader.getConstructor( aC ).newInstance( config );

            loader = Class.forName( planClass, true, this.getClass().getClassLoader() );
            plan = (PlanModule) loader.getConstructor( aC ).newInstance( config );

            loader = Class.forName( commClass, true, this.getClass().getClassLoader() );
            communication = (CommunicationModule) loader.getConstructor( aC ).newInstance( config );

            loader = Class.forName( propulsionClass, true, this.getClass().getClassLoader() );
            propulsion = (PropulsionModule) loader.getConstructor( aC ).newInstance( config );

            location = deployStrategy.getNextLocation( id );

            init = true;
        }
    }

    public abstract Area getBodyArea();
}
