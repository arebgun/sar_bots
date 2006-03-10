package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import config.ConfigAgent;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Scout extends Agent
{
    public Scout( ConfigAgent config ) throws Exception
    {
        super( config );
    }

    public Area getBodyArea()
    {
        double wingSpan = config.getWingSpan(), dimUnit = wingSpan / 3;
        double wingWidth = dimUnit, bodyLength = 5 * dimUnit, bodyWidth = dimUnit;

        Area wings = new Area( new Ellipse2D.Double( location.getX() - wingWidth / 2, location.getY() - wingSpan / 2, wingWidth, wingSpan ) );
        Area body = new Area( new Ellipse2D.Double( location.getX() - 3 * dimUnit, location.getY() - dimUnit / 2, bodyLength, bodyWidth ) );
        wings.add( body );

        return wings;
    }
}
