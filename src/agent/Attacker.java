package agent;
import baseobject.*;
import config.ConfigBobject;
import sim.Simulator;

public class Attacker extends Agent
{
	private int flagID = -1;
	public Attacker(ConfigBobject config) throws Exception
	{
		super (config);
		isAlive = true;
	}
	
	public void pickUpFlag(Flag f)
	{
		if (f.getOwned() == false)
		{
			f.setOwned(true);
			f.setOwner(this.objectID);
			f.setLocation(this.location);
			flagID = f.getObjectID();
			System.out.println("Picked up the flag <Agent> " + flagID);
			hasFlag = true;
		}
		else
			System.out.println("Agent "+this.objectID+" is trying to pick up already owned flag!");
	}

	public void dropFlag()
	{
		hasFlag = false;
		Flag f = (Flag)Simulator.worldObjects.get(flagID);
		f.flagDropped();
		flagID = -1;
	}
	public void update()
	{
//		skip most everything if you are dead
    	if (!isAlive)
    	{
    	    if (hasFlag)
    	       	dropFlag();
    	    return;
    	}
    	if (shotCounter > 0)
    		shotCounter--;
    	if (shotCounter > 0)
    		soundRadius = (int)sensorSight.getlength();
    	else
    		soundRadius = initialSoundRadius;
    	
		move();
		/*
		if (agent_state == agent_state.DEAD)
			plan.Dead(this);
		if (agent_state == agent_state.FLAG_CARRIER)
			plan.FlagCarrier(this);
		if (agent_state == agent_state.ATTACKING)
			plan.Attacking(this);
		if (agent_state == agent_state.FLEE)
			plan.Flee(this);
		if (agent_state == agent_state.HIDE)
			plan.Hide(this);
		if (agent_state == agent_state.SEARCH)
			plan.Search(this);
		if (agent_state == agent_state.RECOVER_FLAG)
			plan.RecoverFlag(this);
			*/
	}
}
