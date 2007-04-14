package agent;
import baseobject.*;

import config.ConfigBobject;

public class Defender extends Agent
{
	public Defender(ConfigBobject config) throws Exception
	{
		super (config);
	}

	public void pickUpFlag(Flag f)
	{
		if (f.getOwned() == false)
		{
			f.setOwner(0);
			f.reset();
		}
	}
	public void dropFlag()
	{
		//do nothing atm, but if defenders can pick up the opposing teams flag
		//then we need to implement this and change pickUpFlag to reflect it
	}
	
	public void update()
	{
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
