package agent;

import agent.comm.CommModule;
import agent.deployment.DeploymentStrategy;
import agent.plan.PlanModule;
import agent.propulsion.PropulsionModule;
import agent.sensor.SensorModule;
import config.ConfigAgent;

import java.awt.*;
import java.awt.geom.*;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */
public abstract class Agent
{
    protected static int typeID = 0;
    protected int unitID;

    protected ConfigAgent config;
    protected Color sensorColor;

    /**
     * Agent deployment strategy. Determines initial position.
     */
    protected AgentLocation location;

    protected double velocity;
    protected double health;

    protected DeploymentStrategy deployStrategy;
    protected SensorModule sensor;
    protected PlanModule plan;
    protected CommModule communication;
    protected PropulsionModule propulsion;


    /**
     * Agent constructor. Creates a new agent.
     */
    public Agent( ConfigAgent config ) throws Exception
    {
        this.config            = config;
        String deployClass     = config.getDeploymentName();
        String sensorClass     = config.getSensorName();
        String commClass       = config.getCommName();
        String planClass       = config.getPlanName();
        String propulsionClass = config.getPropulsionName();

        unitID = typeID++;
        initialize( deployClass, sensorClass, planClass, commClass, propulsionClass );
    }

    /**
     * Gets the Identification Number of the agent.
     *
     * @return unique agent ID
     */
    public int getID()
    {
        return unitID;
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

    public Area getSensorView()
    {
        return sensor.getView( location );
    }

    public Color getSensorColor()
    {
	return sensorColor;
    }

    public Area getBodyArea()
    {
        double wingSpan = config.getWingSpan(), dimUnit = wingSpan / 3;
        double wingWidth = dimUnit, bodyLength = 5 * dimUnit, bodyWidth = dimUnit;

        Area wings = new Area( new Ellipse2D.Double( location.getX() - wingWidth / 2, location.getY() - wingSpan / 2, wingWidth, wingSpan ) );
        Area body = new Area( new Ellipse2D.Double( location.getX() - 3 * dimUnit, location.getY() - dimUnit / 2, bodyLength, bodyWidth ) );

        wings.add( body );
        //BUG-DIMZAR-20060320: is this the right point to rotate about?  Do we care?
        wings.transform( AffineTransform.getRotateInstance( location.getTheta(), location.getX(), location.getY() ) );
        return wings;
    }

    /**
     * Updates agent's location.
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

        Class loader = Class.forName( deployClass, true, this.getClass().getClassLoader() );
        deployStrategy = (DeploymentStrategy) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( sensorClass, true, this.getClass().getClassLoader() );
        sensor = (SensorModule) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( planClass, true, this.getClass().getClassLoader() );
        plan = (PlanModule) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( commClass, true, this.getClass().getClassLoader() );
        communication = (CommModule) loader.getConstructor( aC ).newInstance( config );

        loader = Class.forName( propulsionClass, true, this.getClass().getClassLoader() );
        propulsion = (PropulsionModule) loader.getConstructor( aC ).newInstance( config );

        location    = deployStrategy.getNextLocation( unitID );
	sensorColor = config.getSensorColor();
    }
}
