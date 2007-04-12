package agent;
import baseobject.*;

import config.ConfigBobject;

public class Attacker extends Agent
{
	public Attacker(ConfigBobject config) throws Exception
	{
		super (config);
	}
	
	public void pickUpFlag(Flag f)
	{
		if (f.getOwned() == false)
		{
			f.setOwned(true);
			f.setOwner(this.objectID);
			f.setLocation(this.location);
			hasFlag = true;
		}
		else
			System.out.println("Agent "+this.objectID+" is trying to pick up already owned flag!");
	}

}
