package statistics;

import sim.Simulator;
import java.util.ArrayList;

public class Statistics 
{
	private static Statistics statisticsInstance = null;
	private static int numberOfTeams = 0;
	private static int totalWorldObjects = 0;
	
	//individual statistics
	private static ArrayList<Integer> shotsTaken = null;
	private static ArrayList<Integer> enemiesHit = null;
	private static ArrayList<Integer> damageDone = null;
	private static ArrayList<Integer> flagsPickedUp = null;
	private static ArrayList<Integer> flagsRecovered = null;
	private static ArrayList<Integer> flagsScored = null;
	private static ArrayList<Integer> stateSearch = null;
	private static ArrayList<Integer> stateDead = null;
	private static ArrayList<Integer> stateFlagCarrier = null;
	private static ArrayList<Integer> stateGuard = null;
	private static ArrayList<Integer> stateFlee = null;
	private static ArrayList<Integer> stateRecoverFlag = null;
	private static ArrayList<Integer> stateHide = null;
	
	//team statistics
	private static ArrayList<Integer> flagsCaptured = null;
	
	
	
	private Statistics()
	{
		numberOfTeams = Simulator.numberOfTeams;
		totalWorldObjects = Simulator.numberWorldObjects;
		
		initialize();
	}
	
	public static Statistics getStatisticsInstance()
	{
		if (statisticsInstance == null)
		{
			statisticsInstance = new Statistics();
		}
		return statisticsInstance;
		
	}
	private void initialize()
	{
		shotsTaken = new ArrayList<Integer>();
		enemiesHit = new ArrayList<Integer>();
		damageDone = new ArrayList<Integer>();
		flagsPickedUp = new ArrayList<Integer>();
		flagsRecovered = new ArrayList<Integer>();
		flagsScored = new ArrayList<Integer>();
		stateSearch = new ArrayList<Integer>();
		stateDead = new ArrayList<Integer>();
		stateFlagCarrier = new ArrayList<Integer>();
		stateGuard = new ArrayList<Integer>();
		stateFlee = new ArrayList<Integer>();
		stateRecoverFlag = new ArrayList<Integer>();
		stateHide = new ArrayList<Integer>();
		flagsCaptured = new ArrayList<Integer>();
		
		//individual statistics
		for(int i = 0; i < totalWorldObjects; i++)
		{
			shotsTaken.add(0);
			enemiesHit.add(0);
			damageDone.add(0);
			flagsPickedUp.add(0);
			flagsRecovered.add(0);
			flagsScored.add(0);
			stateSearch.add(0);
			stateDead.add(0);
			stateFlagCarrier.add(0);
			stateGuard.add(0);
			stateFlee.add(0);
			stateRecoverFlag.add(0);
			stateHide.add(0);
		}
		
		//team statistics
		for(int t = 0; t <= numberOfTeams; t++)
		{
			flagsCaptured.add(0);
		}
	}
	
	//set functions
	public static void incShotsTaken(int id)
	{
		shotsTaken.set(id, (shotsTaken.get(id) + 1));
	}
	public static void incEnemiesHit(int id)
	{
		enemiesHit.set(id, (enemiesHit.get(id) + 1));
	}
	public static void incDamageDone(int id, int d)
	{
		damageDone.set(id, (damageDone.get(id)+d));
	}
	public static void incFlagsPickedUp(int id)
	{
		flagsPickedUp.set(id, (flagsPickedUp.get(id)+1));
	}
	public static void incFlagsRecovered(int id)
	{
		flagsRecovered.set(id, (flagsRecovered.get(id)+1));
	}
	public static void incFlagsScored(int id)
	{
		flagsScored.set(id, (flagsScored.get(id)+1));
	}
	public static void incStateSearch(int id)
	{
		stateSearch.set(id, stateSearch.get(id)+1);
	}
	public static void incStateGuard(int id)
	{
		stateGuard.set(id, (stateGuard.get(id) + 1));
	}
	public static void incStateFlee(int id)
	{
		stateFlee.set(id, (stateFlee.get(id)+1));
	}
	public static void incStateFlagCarrier(int id)
	{
		stateFlagCarrier.set(id, (stateFlagCarrier.get(id)+1));
	}
	public static void incStateDead(int id)
	{
		stateDead.set(id, (stateDead.get(id)+1));
	}
	public static void incStateHide(int id)
	{
		stateHide.set(id, (stateHide.get(id)+1));
	}
	public static void incStateRecoverFlag(int id)
	{
		stateRecoverFlag.set(id, (stateRecoverFlag.get(id)+1));
	}
	public static void incFlagsCaptured(int teamID)
	{
		flagsCaptured.set(teamID, (flagsCaptured.get(teamID)+1));
	}
	
	//gets
	public static int getShotsTaken(int id)
	{
		return shotsTaken.get(id);
	}
	public static int getEnemiesHit(int id)
	{
		return enemiesHit.get(id);
	}
	public static int getDamageDone(int id)
	{
		return damageDone.get(id);
	}
	public static int getFlagsPickedUp(int id)
	{
		return flagsPickedUp.get(id);
	}
	public static int getFlagsRecovered(int id)
	{
		return flagsRecovered.get(id);
	}
	public static int getFlagsScored(int id)
	{
		return flagsScored.get(id);
	}
	public static int getStateSearch(int id)
	{
		return stateSearch.get(id);
	}
	public static int getStateDead(int id)
	{
		return stateDead.get(id);
	}
	public static int getStateFlagCarrier(int id)
	{
		return stateFlagCarrier.get(id);
	}
	public static int getStateGuard(int id)
	{
		return stateGuard.get(id);
	}
	public static int getFlee(int id)
	{
		return stateFlee.get(id);
	}
	public static int getRecoverFlag(int id)
	{
		return stateRecoverFlag.get(id);
	}
	public static int getStateHide(int id)
	{
		return stateHide.get(id);
	}
	public static int getFlagsCaptured(int id)
	{
		return flagsCaptured.get(id);
	}
}
