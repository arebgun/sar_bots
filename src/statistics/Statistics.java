package statistics;

import sim.Simulator;
import java.util.ArrayList;

public class Statistics 
{
	private static int numberOfTeams = 0;
	private static int totalWorldObjects = 0;
	
	//individual statistics
	private static ArrayList<Integer> shotsTaken = null;
	private static ArrayList<Integer> enemiesHit = null;
	private static ArrayList<Integer> damageDone = null;
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
	
	
	
	public Statistics()
	{
		numberOfTeams = Simulator.numberOfTeams;
		totalWorldObjects = Simulator.numberWorldObjects;
		
		initialize();
	}
	
	private void initialize()
	{
		//individual statistics
		for(int i = 0; i < totalWorldObjects; i++)
		{
			shotsTaken.add(0);
			enemiesHit.add(0);
			damageDone.add(0);
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
	public void incShotsTaken(int id)
	{
		shotsTaken.set(id, (shotsTaken.get(id) + 1));
	}
	public void incEnemiesHit(int id)
	{
		enemiesHit.set(id, (enemiesHit.get(id) + 1));
	}
	public void incDamageDone(int id, int d)
	{
		damageDone.set(id, (damageDone.get(id)+d));
	}
	public void incFlagsRecovered(int id)
	{
		flagsRecovered.set(id, (flagsRecovered.get(id)+1));
	}
	public void incFlagsScored(int id)
	{
		flagsScored.set(id, (flagsScored.get(id)+1));
	}
	public void incStateSearch(int id)
	{
		stateSearch.set(id, stateSearch.get(id)+1);
	}
	public void incStateGuard(int id)
	{
		stateGuard.set(id, (stateGuard.get(id) + 1));
	}
	public void incStateFlee(int id)
	{
		stateFlee.set(id, (stateFlee.get(id)+1));
	}
	public void incStateFlagCarrier(int id)
	{
		stateFlagCarrier.set(id, (stateFlagCarrier.get(id)+1));
	}
	public void incStateDead(int id)
	{
		stateDead.set(id, (stateDead.get(id)+1));
	}
	public void incStateHide(int id)
	{
		stateHide.set(id, (stateHide.get(id)+1));
	}
	public void incStateRecoverFlag(int id)
	{
		stateRecoverFlag.set(id, (stateRecoverFlag.get(id)+1));
	}
	public void incFlagsCaptured(int teamID)
	{
		flagsCaptured.set(teamID, (flagsCaptured.get(teamID)+1));
	}
	
	//gets
	public int getShotsTaken(int id)
	{
		return shotsTaken.get(id);
	}
	public int getEnemiesHit(int id)
	{
		return enemiesHit.get(id);
	}
	public int getDamageDone(int id)
	{
		return damageDone.get(id);
	}
	public int getFlagsRecovered(int id)
	{
		return flagsRecovered.get(id);
	}
	public int getFlagsScored(int id)
	{
		return flagsScored.get(id);
	}
	public int getStateSearch(int id)
	{
		return stateSearch.get(id);
	}
	public int getStateDead(int id)
	{
		return stateDead.get(id);
	}
	public int getStateFlagCarrier(int id)
	{
		return stateFlagCarrier.get(id);
	}
	public int getStateGuard(int id)
	{
		return stateGuard.get(id);
	}
	public int getFlee(int id)
	{
		return stateFlee.get(id);
	}
	public int getRecoverFlag(int id)
	{
		return stateRecoverFlag.get(id);
	}
	public int getStateHide(int id)
	{
		return stateHide.get(id);
	}
	public int getFlagsCaptured(int id)
	{
		return flagsCaptured.get(id);
	}
}
