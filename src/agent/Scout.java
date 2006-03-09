package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import sim.Simulator;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Scout extends Agent
{
    public Scout() throws Exception
    {
        String deployClass = Simulator.config.getScoutDeploymentStrategy();
        String sensorClass = Simulator.config.getScoutSensor();
        String commClass = Simulator.config.getScoutComm();
        String planClass = Simulator.config.getScoutPlan();
        String propulsionClass = Simulator.config.getScoutPropulsion();

        double sensorRange = Simulator.config.getScoutCommRange();
        double commRange = Simulator.config.getScoutSensorRange();

        initialize( deployClass, sensorClass, sensorRange, planClass, commClass, commRange, propulsionClass );
    }

    public Area getBodyArea()
    {
        double wingSpan = Simulator.config.getWingSpan(), dimUnit = wingSpan / 3;
        double wingWidth = dimUnit, bodyLength = 5 * dimUnit, bodyWidth = dimUnit;

        Area wings = new Area( new Ellipse2D.Double( location.getX() - wingWidth / 2, location.getY() - wingSpan / 2, wingWidth, wingSpan ) );
        Area body = new Area( new Ellipse2D.Double( location.getX() - 3 * dimUnit, location.getY() - dimUnit / 2, bodyLength, bodyWidth ) );
        wings.add( body );

        return wings;
    }
}
