package agent.plan;
import java.util.Random;
import agent.Agent;
import baseobject.Flag;
import agent.AgentLocation;
import config.ConfigBobject;
import messageBoard.MessageBoard;
import sim.Simulator;
import java.util.Iterator;
import statistics.Statistics;

public class Aggressive extends PlanModule
{
	private static Random rand = null;
    private Agent.state agentState = Agent.state.SEARCH;
	public Aggressive(ConfigBobject config)
	{
		super(config);
		agentState = Agent.state.SEARCH;
		initialState = agentState;
		guardDistance = config.getGuardDistance();
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
    	AgentLocation holder = null;
    	
    	if(a.getHasFlag())
    		a.dropFlag();
    	a.setIsAlive(false);
    	
    	a.sendMessage(false, false, holder, false, holder);
    	agentState = Agent.state.HIDE;
    	a.setAgentState(agentState);
    	   		
    }
    public void FlagCarrier(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	AgentLocation oppFlagLoc = a.getLocation();
    	boolean ourFlagSeen = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	
    	a.checkSensors();
    	AgentLocation base = mb.getBaseLocation();
    	double newHeading = a.arctangentToGoal(base);
    	if (atBase(a))
    	{
    		Simulator.weWon(a.getColor(), "FLAG CAPTURED!!");
    		Flag f = (Flag)Simulator.worldObjects.get(a.getFlagID());
    		f.setCaptured(true);
    		Statistics.incFlagsCaptured(a.getTeamID());
    		Statistics.incFlagsScored(a.getObjectID());
    	}
    	else
    		moveAgent(a,heading,newHeading);
    	
		a.checkSensors();
		Statistics.incStateFlagCarrier(a.getObjectID());
		a.sendMessage(false, oppFlagSeen, oppFlagLoc, ourFlagSeen, ourFlagLoc);	
    }
    
    public void Guard(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	int moveChance = 100;
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	boolean ourFlagSeen = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	int flagOwner = mb.getWhoOwnsFlag();
    	a.checkSensors();
    	
    	if (flagOwner < 1)
    	{
    		agentState = Agent.state.SEARCH;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
   
    	AgentLocation oppFlagLoc = mb.getOpponentFlagLocation();
    	
    	double x = a.getLocation().getX();
    	double y = a.getLocation().getY();
    	double fx = oppFlagLoc.getX();
    	double fy = oppFlagLoc.getY();
    	double dist = Math.hypot((x-fx),(y-fy));
    	if (dist <= guardDistance)
    		moveChance = 50;
    	double newHeading = a.arctangentToGoal(oppFlagLoc);
    	//if we see an opponent decide if we shoot or move
    	if (seeOpponent(a) && moveChance == 50)
    		a.shootAll();
    	//so we did not see an opponent, so we move
    	else
    	{
    		int chance = (int)(rand.nextDouble() * 100);
    		if (chance <= moveChance)
    			moveAgent(a,heading,newHeading);
    	}
    	
    	a.checkSensors();
    	Statistics.incStateGuard(a.getObjectID());
    	a.sendMessage(false, oppFlagSeen, oppFlagLoc, ourFlagSeen, ourFlagLoc);
    	
    }
    public void Attacking(Agent a)
    {
    	
    }
    public void Flee(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	AgentLocation oppFlagLoc = null;
    	boolean ourFlagSeen = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	a.checkSensors();
    	
    	//if i am not getting shot anymore, switch to search
    	if (!a.getIsBeingShot() || oppFlagSeen)
    	{
    		agentState = Agent.state.SEARCH;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	
    	if (mb.getWhoOwnsFlag() > 0)
    	{
    		agentState = Agent.state.GUARD;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	
    	double newHeading = 0;
		int numEnemies = 0;
		Iterator <Agent> enemy = a.getAgentsHeard();
    	while (enemy.hasNext())
    	{
    		Agent b = (Agent)enemy.next();
    		if (a.getTeamID() != b.getTeamID())
    		{
    			numEnemies++;
    			newHeading += a.arctangentToGoal(b.getLocation());
    		}
    	}
    	newHeading = newHeading/numEnemies;
    	
    	moveAgent(a,heading,newHeading);
    	
		a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	ourFlagSeen = ourFlagSeen(a);
    	if (ourFlagSeen)
    		ourFlagLoc = ourFlagLoc(a);
    	Statistics.incStateFlee(a.getObjectID());
		a.sendMessage(false, oppFlagSeen, oppFlagLoc, ourFlagSeen, ourFlagLoc);
   }
    
    public void Hide(Agent a)
    {
    	
    }
    public void Search(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	double newHeading = heading;
    	int moveChance = 100;
    	int shootChance = 0;
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	AgentLocation oppFlagLoc = null;
    	boolean ourFlagSeen = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	a.checkSensors();
    	
    	if (ourFlagSeen)
    	{
    		ourFlagLoc = ourFlagLoc(a);
    		if (!mb.getFlagAtHome())
    		{
    			agentState = Agent.state.RECOVER_FLAG;
    			a.setAgentState(agentState);
        		a.update();
    			return;
    		}
    	}
    	
    	if (mb.getWhoOwnsFlag() > 0)
    	{
			agentState = Agent.state.GUARD;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	//if we see the flag, head to it, do nothing else
     	//if we collide with the flag, pick it up and change state to Flag Carrier
    	if (oppFlagSeen)
    	{
    		Flag f = getFlagSeen(a);
    		if (f == null)
    			return;
			double dist = Math.hypot((a.getLocation().getX() - f.getLocation().getX()), 
    				(a.getLocation().getY() - f.getLocation().getY()));
    		if ((a.getBoundingRadius() + f.getBoundingRadius()) >= dist)
    		{
    			a.pickUpFlag(f);
    			agentState = Agent.state.FLAG_CARRIER;
    			a.setAgentState(agentState);
    			a.update();
    			return;
    		}
    		newHeading = a.moveToLocation(f.getLocation());
    		moveChance = 100;
    	}
    	else if (a.getHealth() <= a.getThreshold() && a.getIsBeingShot())
    	{	
    		agentState = Agent.state.FLEE;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	//if we know where the flag is, plot a heading to it
    	else if (mb.getOpponentFlagSeen())
    	{
    		AgentLocation temp = mb.getOpponentFlagLocation();
    		newHeading = a.moveToLocation(temp);
    		moveChance = 75;
    	}
    	//we do not know where the flag is, so pick a random heading
    	//within our viewing area.
    	else
    	{
    		newHeading = heading;
    		if ((rand.nextDouble()*100) > 75 )
    			newHeading = rand.nextDouble()*360;
        	moveChance = 25;
       }
    	//if we see an opponent decide if we shoot or move
    	if (seeOpponent(a))
    	{
    		shootChance = 100 - moveChance;
    		int chance = (int)(rand.nextDouble() * 100);
    		if (chance <= shootChance)
    			a.shootAll();
    		else
    			moveAgent(a,heading,newHeading);
    	}
    	//so we did not see an opponent, so we move
    	else
    		moveAgent(a,heading,newHeading);
    	
    	//we have now moved. check sensors again so we can send current info to the mb
    	a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	ourFlagSeen = ourFlagSeen(a);
    	if (ourFlagSeen)
    		ourFlagLoc = ourFlagLoc(a);
    	
    	Statistics.incStateSearch(a.getObjectID());
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
    	AgentLocation oppFlagLoc = null;
    	boolean ourFlagSeen = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	a.checkSensors();
    	
    	//if our flag is returned, get out of recoverFlag and do something else
    	if (mb.getFlagAtHome())
    	{
    		agentState = Agent.state.SEARCH;
    		a.setAgentState(agentState);
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
    			a.setAgentState(agentState);
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
    		else
    		{
    			a.setTheta(heading);
    			a.checkSensors();
    			boolean found = move(a,heading);
    			if (!found)
    			{
    				double newTurn = heading + a.sensorSight.getHalfAngle();//rand.nextDouble()*a.sensorSight.getArcAngle() + (heading - a.sensorSight.getHalfAngle());
    				a.turnMove(newTurn);
    			}
    		}
    	}
    	//so we did not see an opponent, so we move
    	else
    	{
    		a.setTheta(heading);
			a.checkSensors();
			boolean found = move(a,heading);
			if (!found)
			{
				double newTurn = heading + a.sensorSight.getHalfAngle();//rand.nextDouble()*a.sensorSight.getArcAngle() + (heading - a.sensorSight.getHalfAngle());
				a.turnMove(newTurn);
			}
    	}
    	//we have now moved. check sensors again so we can send current info to the mb
    	a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	ourFlagSeen = ourFlagSeen(a);
    	if (ourFlagSeen)
    		ourFlagLoc = ourFlagLoc(a);
    	
    	Statistics.incStateRecoverFlag(a.getObjectID());
    	//now send all your info to the messageboard
    	a.sendMessage(false, oppFlagSeen, oppFlagLoc, ourFlagSeen, ourFlagLoc);
    		
    }

    public void CleanUp(Agent a)
    {
    	/*There are two conditions, if the agent is dead or alive*/
		if(!a.getIsAlive())
		{
			a.reset();
			a.setFadeIn(true);
			agentState = Agent.state.FADE;
			a.setAgentState(agentState);
		}
		else
		{
			if(!a.closeToGoal(a.getMoveRadius(), a.getInitialLocation()))
			{
				double initHeading = a.arctangentToGoal(a.getInitialLocation());
				moveAgent(a, a.getLocation().getTheta(), initHeading);
			}
			else
			{
				a.setLocation(a.getInitialLocation());
				agentState = Agent.state.WAIT;
				a.setAgentState(agentState);
			}
		}
    }
    public void Patrol(Agent a)
    {
    	
    } 
    public void Fade(Agent a)
    {
    	if(!a.getFadeIn())
    	{
    		agentState = Agent.state.WAIT;
    		a.setAgentState(agentState);
    	}
    }
    public void Wait(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	mb.setIAmFaded(a.getMsgID());
    	agentState = Agent.state.HIDE;
    	a.setAgentState(agentState);
    	if (mb.getAllFadeIn())
    		Simulator.addFaded(a.getTeamID());
    	
    }
}
