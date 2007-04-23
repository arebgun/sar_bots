package agent;

import baseobject.*;
import config.ConfigBobject;
import statistics.Statistics;

public class Defender extends Agent
{
	public Defender(ConfigBobject config) throws Exception
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
			f.setOwner(0);
			f.reset();
			Statistics.incFlagsRecovered(objectID);
		}
	}
	public void dropFlag()
	{
		//do nothing atm, but if defenders can pick up the opposing teams flag
		//then we need to implement this and change pickUpFlag to reflect it
	}
	
	public void update()
	{
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
		else if (agent_state == Agent.state.GUARD)
			plan.Guard(this);
		else if (agent_state == Agent.state.PATROL)
			plan.Patrol(this);
		else if (agent_state == Agent.state.CLEANUP)
			plan.CleanUp(this);
		else if (agent_state == Agent.state.FADE)
			plan.Fade(this);
		else if (agent_state == Agent.state.WAIT)
			plan.Wait(this);
			
		
		beingShot = false;
	}
	
	public void cleanup()
	{
		agent_state = Agent.state.CLEANUP;		
	}
}
