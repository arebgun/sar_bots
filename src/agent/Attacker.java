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
		threshold = config.getThreshold();
    	myType = agentType.AGENT;
	}
	
	public void pickUpFlag(Flag f)
	{
		if (f.getOwned() == false)
		{
			f.setOwned(true);
			f.setOwner(this.objectID);
			f.setLocation(this.location);
			flagID = f.getObjectID();
			System.out.println("Picked up the flag <Agent> " + objectID);
			hasFlag = true;
			moveRadius = moveRadius / 2;
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
		moveRadius = initialSoundRadius;
	}
	public void update()
	{
		if (shotCounter > 0)
    		shotCounter--;
    	if (shotCounter > 0)
    		soundRadius = (int)sensorSight.getlength();
    	else
    		soundRadius = initialSoundRadius;
    	
		
		if (agent_state == state.DEAD)
			plan.Dead(this);
		if (agent_state == state.FLAG_CARRIER)
			plan.FlagCarrier(this);
		if (agent_state == state.GUARD)
			plan.Guard(this);
		if (agent_state == state.ATTACKING)
			plan.Attacking(this);
		if (agent_state == state.FLEE)
			plan.Flee(this);
		if (agent_state == state.HIDE)
			plan.Hide(this);
		if (agent_state == state.SEARCH)
			plan.Search(this);
		if (agent_state == state.RECOVER_FLAG)
			plan.RecoverFlag(this);

		
		//new turn starts from this point on, so reset beingShot to false
		beingShot = false;
	}
}
