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
			f.setOwner(-1);
			f.reset();
		}
	}
}
