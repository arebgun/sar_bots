package agent.plan;
//TODO add threshold for hit points and fleeing
import java.util.Random;
import agent.Agent;
import agent.AgentLocation;
import config.ConfigBobject;
import messageBoard.MessageBoard;
import sim.Simulator;

public class Aggressive extends PlanModule
{
	private static Random rand = null;
    private agent.Agent.state agentState = agent.Agent.state.SEARCH;
	
	public Aggressive(ConfigBobject config)
	{
		super(config);
		if ( rand == null )
        {
            rand = new Random( objectConfig.getPlanSeed() );
        }
	}
	
	public agent.Agent.state getAgentState()
	{
		return agentState;
	}

    public AgentLocation getGoalLocation(Agent a )
    {
    	AgentLocation temp = null;
    	return temp;
    }
    
    public void Dead(Agent a)
    {
    	   		
    }
    public void FlagCarrier(Agent a)
    {
    	
    }
    public void Guard(Agent a)
    {
    	
    }
    public void Attacking(Agent a)
    {
    	
    }
    public void Flee(Agent a)
    {
    	
    }
    public void Hide(Agent a)
    {
    	
    }
    public void Search(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	int moveChance = 100;
    	int shootChance = 0;
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	int numOpponentsSeen = opponentsSeen(a);
    	AgentLocation oppFlagLoc = new AgentLocation(-1,-1,-1);
    	boolean ourFlagSeen = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = new AgentLocation(-1,-1,-1);
    	
    	if (ourFlagSeen)
    	{
    		ourFlagLoc = ourFlagLoc(a);
    		if (!mb.getFlagAtHome())
    		{
    			agentState = Agent.state.RECOVER_FLAG;
    			a.update();
    			return;
    		}
    	}
    	
    	if (a.getHealth() <= 50 && a.getIsBeingShot())
    	{	
    		agentState = Agent.state.FLEE;
    		a.update();
    		return;
    	}
    	
    	if (mb.getWhoOwnsFlag() >= 0)
    	{
    		agentState = Agent.state.GUARD;
    		a.update();
    		return;
    	}
    	//if we see the flag, head to it, do nothing else
    	if (oppFlagSeen)
    	{
    		AgentLocation temp = opponentsFlagLocation(a);
    		heading = a.moveToLocation(temp);
    		moveChance = 100;
    	}
    	//if we know where the flag is, plot a heading to it
    	else if (mb.getOpponentFlagSeen())
    	{
    		AgentLocation temp = mb.getOpponentFlagLocation();
    		heading = a.moveToLocation(temp);
    		moveChance = 75;
    	}
    	//we do not know where the flag is, so pick a random heading
    	//within our viewing area.
    	else
    	{
    		double arc = a.sensorSight.getArcAngle();
    		heading = rand.nextDouble() * arc + (heading - arc/2);
        	while (heading > 360)
        		heading -= 360;
        	while (heading < 0)
        		heading += 360;	
        	moveChance = 25;
       }
    	//if we see an opponent decide if we shoot or move
    	if (seeOpponent(a))
    	{
    		shootChance = 100 - moveChance;
    		int chance = (int)(rand.nextDouble() * 100);
    		if (chance <= shootChance)
    			a.shootAll();
    //		else
    	//		a.move(heading);
    	}
    	//so we did not see an opponent, so we move
    //	else
    //		a.move(heading);
    	
    	//we have now moved. check sensors again so we can send current info to the mb
    	a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	ourFlagSeen = ourFlagSeen(a);
    	if (ourFlagSeen)
    		ourFlagLoc = ourFlagLoc(a);
    	
    	//now send all your info to the messageboard
    	a.sendMessage(false, oppFlagSeen, oppFlagLoc, ourFlagSeen, ourFlagLoc);
    }
    public void RecoverFlag(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	int moveChance = 100;
    	int shootChance = 0;
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	int numOpponentsSeen = opponentsSeen(a);
    	AgentLocation oppFlagLoc = new AgentLocation(-1,-1,-1);
    	boolean ourFlagSeen = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = new AgentLocation(-1,-1,-1);
    	
    	//if our flag is returned, get out of recoverFlag and do something else
    	if (mb.getFlagAtHome())
    	{
    		agentState = Agent.state.SEARCH;
    		a.update();
    		return;
    	}
    	
    	if (ourFlagSeen)
    	{
    		ourFlagLoc = ourFlagLoc(a);
    		heading = a.moveToLocation(ourFlagLoc);
    		moveChance = 100;
    	}
    	//otherwise we haven't seen our flag, ask if someone else has
    	else
    	{
    		if(mb.getOurFlagSeen())
    		{
    			ourFlagLoc = mb.getOurFlagLocation();
    			heading = a.moveToLocation(ourFlagLoc);
    			moveChance = 75;
    		}
    		//nobody knows where our flag is, so go back to search
    		else
    		{
    			agentState = Agent.state.SEARCH;
    			a.update();
    			return;
    		}
    	}
    	if (numOpponentsSeen > 0)
    	{
    		shootChance = 100 - moveChance;
    		int chance = (int)(rand.nextDouble() * 100);
    		if (chance <= shootChance)
    			a.shootAll();
    	//	else
    	//		a.move(heading);
    	}
    	//so we did not see an opponent, so we move
    //	else
    //		a.move(heading);

    	//we have now moved. check sensors again so we can send current info to the mb
    	a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	ourFlagSeen = ourFlagSeen(a);
    	if (ourFlagSeen)
    		ourFlagLoc = ourFlagLoc(a);
    	
    	//now send all your info to the messageboard
    	a.sendMessage(false, oppFlagSeen, oppFlagLoc, ourFlagSeen, ourFlagLoc);
    		
    }

    

}
