package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *         Date:   Feb 24, 2006
 *         Time:   8:52:15 PM
 */

public class AgentLocation
{
    private double x;
    private double y;
    private double theta;

    public AgentLocation( double x, double y, double theta )
    {
	setX(x);
	setY(y);
	setTheta(theta);
    }

    public double getX() 
    {
	return x;
    }

    public double getY() 
    {
	return y;
    }

    public double getTheta()
    {
	return theta;
    }


    public void setX( double newX ) {
	if ( newX<0 || newX>Simulator.config.getWorldWidth())
	    {
		throw new IndexOutOfBoundsException("new agent horizontal postion " + newX + " is out of world bounds");
	    }
	x = newX;
    }

    public void setY( double newY ) {
	if ( newY<0 || newY>Simulator.config.getWorldHeight())
	    {
		throw new IndexOutOfBoundsException("new agent vertical postion " + newY + " is out of world bounds");
	    }
	y = newY;
    }

    public void setTheta( double newTheta ) {
	theta = newTheta;
    }
}
