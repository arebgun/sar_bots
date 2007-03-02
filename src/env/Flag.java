package env;

/*
 * Class Name:    env.Flag
 * Last Modified: 3/2/2007 10:47
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 * @author Joshua Sanderlin
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

//import sim.Simulator;
import agent.AgentLocation;

public class Flag {
	private static AgentLocation initialPosition;
	private static AgentLocation currentPosition;
	private int owner;
	private int radius;
	
	public Flag()
	{
		radius = 5;
	}
	
	public static void reset()
	{
		currentPosition.copy( initialPosition );
	}
}
