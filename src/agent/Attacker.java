package agent;
import baseobject.*;
import config.ConfigBobject;
import statistics.Statistics;

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
			myFlag = f;
			moveRadius = moveRadius / 2;
			Statistics.incFlagsPickedUp(objectID);
		}
		else
			System.out.println("Agent "+this.objectID+" is trying to pick up already owned flag!");
	}

	public void dropFlag()
	{
		myFlag.flagDropped();
		myFlag = null;
		flagID = -1;
		hasFlag = false;
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
    	
		
    	if (agent_state == Agent.state.DEAD)
			plan.Dead(this);
		else if (agent_state == Agent.state.FLAG_CARRIER)
			plan.FlagCarrier(this);
		else if (agent_state == Agent.state.ATTACKING)
			plan.Attacking(this);
		else if (agent_state == Agent.state.FLEE)
			plan.Flee(this);
		else if (agent_state == Agent.state.HIDE)
			plan.Hide(this);
		else if (agent_state == Agent.state.SEARCH)
			plan.Search(this);
		else if (agent_state == Agent.state.RECOVER_FLAG)
			plan.RecoverFlag(this);
		else if (agent_state == Agent.state.PATROL)
			plan.Patrol(this);
		else if (agent_state == Agent.state.CLEANUP)
			plan.CleanUp(this);
		else if (agent_state == Agent.state.FADE)
			plan.Fade(this);
		else if (agent_state == Agent.state.WAIT)
			plan.Wait(this);

		
		//new turn starts from this point on, so reset beingShot to false
		beingShot = false;
	}
		
	public void cleanup()
	{
		agent_state = Agent.state.CLEANUP;
	}
}