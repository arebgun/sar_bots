package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.sensor.SensorModule;
import sim.Simulator;

public class Worker extends Agent
{
    public Worker() throws Exception
    {
        Class ds = Class.forName( Simulator.config.workerSensor(), true, this.getClass().getClassLoader() );
        sensor = (SensorModule) ds.getConstructor( Class.forName( "Double" ) ).newInstance( Simulator.config.workerSensorRange() );
    }
}
