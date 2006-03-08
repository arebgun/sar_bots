package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.sensor.SensorModule;
import sim.Simulator;

import java.awt.*;
import java.awt.geom.*;

public class Scout extends Agent
{
    public Scout() throws Exception
    {
        location = deployStrategy.getNextLocation( id );
	
        Class loader = Class.forName( Simulator.config.getScoutSensor(), true, this.getClass().getClassLoader() );
        sensor       = (SensorModule) loader.getConstructor( Class.forName( "Double" ) ).newInstance( Simulator.config.getScoutSensorRange() );


	loader        = Class.forName( Simulator.config.getScoutComm(), true, this.getClass().getClassLoader() );
	communication = (CommModule) loader.getConstructor( Class.forName( "Double" ) ).newInstance( Simulator.config.getScoutCommRange() );


	loader = Class.forName( Simulator.config.getScoutPlan(), true, this.getClass().getClassLoader() );
	plan   = (PlanModule) loader.newInstance();

	loader     = Class.forName( Simulator.config.getScoutPropulsion(), true, this.getClass().getClassLoader() );
	propulsion = (PropulsionModule) loader.newInstance();
    }


    public Area getBodyArea()
    {
	double wingSpan  = Simulator.config.getScoutWingSpan(), dimUnit = wingSpan / 3;
	double wingWidth = dimUnit, bodyLength = 5*dimUnit, bodyWidth = dimUnit;

	Area wings = new Area( new Ellipse2D.Double( location.getX() - wingWidth/2, location.getY() - wingSpan/2, wingWidth, wingSpan ) );
	Area body  = new Area( new Ellipse2D.Double( location.getX() - 3*dimUnit, location.getY() - dimUnit/2, bodyLength, bodyWidth ) );

        return wings.add( body );
    }



    private static void setProperties() throws Exception
    {
        Class loader   = Class.forName( Simulator.config.getScoutDeploymentStrategy(), true, this.getClass().getClassLoader() );
        deployStrategy = (DeploymentStrategy) loader.newInstance();
        init           = true;
    }
}
