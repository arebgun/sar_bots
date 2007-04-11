package agent.deployment;

import config.ConfigBobject;

import agent.*;
import baseobject.Bobject;
import java.util.Iterator;
import java.util.Random;
import sim.Simulator;

public class LineDeploy extends DeploymentStrategy
{
	private static Random rand = null;
	protected double startX = 0;
	protected double startY = 0;
	protected double endX = 0;
	protected double endY = 0;
	protected int number = 0;
	protected double initialHeading = -1;
	
	public LineDeploy (ConfigBobject config)
	{
		super(config);
		if ( rand == null ) { rand = new Random( objectConfig.getDeploymentSeed() );}
        startX = config.getStartX();
		startY = config.getStartY();
		endX = config.getEndX();
		endY = config.getEndY();
		number = config.getSwarmSize();
		initialHeading = config.getInitialHeading();
		if (initialHeading < 0)
			initialHeading = rand.nextDouble() * 360;
	}
	
	public AgentLocation getNextLocation( Agent a )
	{
		double stepX = (endX - startX) / number;
		double stepY = (endY - startY) / number;
		double newX = startX;
		double newY = startY;
		
		int num = 0; //number of times through loop so far
		boolean found = false; //have we found a location yet;
		
		while (!found && (num++ < number))
		{
			boolean good = true;
			newX = startX + num * stepX;
			newY = startY + num * stepY;
			Iterator<Bobject> iter = Simulator.objectIterator();
		    while ( iter.hasNext())
		    {
		      	Bobject b = iter.next();
		      	if (b.isPlaced() && a.getObjectID() != b.getObjectID())
		      	{
		      		double dist = Math.hypot((newX - b.getLocation().getX()), (newY - b.getLocation().getY()));
		      		if (a.getBoundingRadius() + b.getBoundingRadius() >= dist)
		      			good = false;
		      	}
			   found = good;
		    }
		}
		if ( !found ) { throw new IllegalStateException( "unable to deploy agent #" + a.getObjectID() ); }
		a.setPlaced(true); 
		
		return new AgentLocation(newX, newY, initialHeading);
	}
}
