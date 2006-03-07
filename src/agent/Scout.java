package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.sensor.SensorModule;
import sim.Simulator;

public class Scout extends Agent
{
    public Scout() throws Exception
    {
        //get body dimensions
        // body = yada yada ...
        Class ds = Class.forName( Simulator.config.scoutSensor(), true, this.getClass().getClassLoader() );
        sensor = (SensorModule) ds.getConstructor( Class.forName( "Double" ) ).newInstance( Simulator.config.scoutSensorRange() );
    }
}
