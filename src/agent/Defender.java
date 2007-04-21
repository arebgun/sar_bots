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
		if (agent_state == Agent.state.FLAG_CARRIER)
			plan.FlagCarrier(this);
		if (agent_state == Agent.state.ATTACKING)
			plan.Attacking(this);
		if (agent_state == Agent.state.FLEE)
			plan.Flee(this);
		if (agent_state == Agent.state.HIDE)
			plan.Hide(this);
		if (agent_state == Agent.state.SEARCH)
			plan.Search(this);
		if (agent_state == Agent.state.RECOVER_FLAG)
			plan.RecoverFlag(this);
		if (agent_state == Agent.state.PATROL)
			plan.Patrol(this);
			
		
		beingShot = false;
	}
}
