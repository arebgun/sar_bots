package config;

import java.awt.Color;
import java.io.IOException;
import agent.AgentLocation;

public class ConfigBobject extends Config
{
	
	public ConfigBobject( String configFileName ) throws IOException
    {
        super( configFileName );
    }
	
	public String getClassName()
    {
        return pTable.get( "className" );
    }

    public int getSwarmSize()
    {
        return Double.valueOf( pTable.get( "swarmSize" ) ).intValue();
    }

    public String getCommName()
    {
        return pTable.get( "commName" );
    }

    public double getCommRange()
    {
        return Double.parseDouble( pTable.get( "commRange" ) );
    }

    public String getDeploymentName()
    {
        return pTable.get( "deploymentName" );
    }

    public long getDeploymentSeed()
    {
        return Long.parseLong( pTable.get( "deploymentSeed" ) );
    }

    public String getPlanName()
    {
        return pTable.get( "planName" );
    }

    public long getPlanSeed()
    {
        return Long.parseLong( pTable.get( "planSeed" ) );
    }

    public String getPropulsionName()
    {
        return pTable.get( "propulsionName" );
    }

    public double getPropulsionMaxSpeed()
    {
        return Double.parseDouble( pTable.get( "propulsionMaxSpeed" ) );
    }

    public double getPropulsionEnergyAmount()
    {
        return Double.parseDouble( pTable.get( "propulsionEnergyAmount" ) );
    }

    public String getSensorSightName()
    {
        return pTable.get( "sensorSightName" );
    }
    
    public String getSensorHearingName()
    {
    	return pTable.get( "sensorHearingName" );
    }

    public Color getSightColor()
    {
        String clrValues[] = pTable.get( "sightColor" ).split( "\\," );

        return new Color( Integer.parseInt( clrValues[0] ),
                          Integer.parseInt( clrValues[1] ),
                          Integer.parseInt( clrValues[2] ),
                          Integer.parseInt( clrValues[3] ) );
    }
    
    public Color getSoundColor()
    {
        String clrValues[] = pTable.get( "soundColor" ).split( "\\," );

        return new Color( Integer.parseInt( clrValues[0] ),
                          Integer.parseInt( clrValues[1] ),
                          Integer.parseInt( clrValues[2] ),
                          Integer.parseInt( clrValues[3] ) );
    }
    
    public int getHealth()
    {
    	return Integer.parseInt( pTable.get( "health" ) );
    }
    
    public int getBoundingRadius()
    {
    	return Integer.parseInt( pTable.get( "boundingRadius" ) );
    }
    
    public int getSoundRadius()
    {
    	return Integer.parseInt( pTable.get( "soundRadius" ) );
    }
    
    public double getSensorRadius()
    {
    	return Double.parseDouble( pTable.get ( "sensorRadius" ) );
    }
    
    public double getSensorArcAngle()
    {
    	return Double.parseDouble( pTable.get ( "sensorArcAngle" ) );
    }
    
    public double getSensorLength()
    {
    	return Double.parseDouble( pTable.get ( "sensorLength" ) );
    }
    
    
    public AgentLocation objectLocation()
    {
    	String locValues[] = pTable.get( "objectLocation").split("\\,");
    	return new AgentLocation(Double.parseDouble( locValues[0] ), 
    							 Double.parseDouble( locValues[1] ),
    							 Double.parseDouble( locValues[2] ) );
    }
    
	public Color getObjectColor()
	{
		 String clrValues[] = pTable.get( "objectColor" ).split( "\\," );

	     return new Color( Integer.parseInt( clrValues[0] ),
	                       Integer.parseInt( clrValues[1] ),
	                       Integer.parseInt( clrValues[2] ),
	                       Integer.parseInt( clrValues[3] ) );
	}
	
	public int getTeamID()
	{
		return Integer.parseInt( pTable.get("teamID"));
	}
//DeploymentStrategy variables
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	//circle deployment
	public double getXCircle()
	{
		return Double.parseDouble( pTable.get( "xCircle"));
	}
	
	public double getYCircle()
	{
		return Double.parseDouble( pTable.get( "yCircle"));
	}
	
	public double getCircleRadius()
	{
		return Double.parseDouble( pTable.get( "circleRadius"));
	}
	
	//line deployment
	public double getStartX()
	{
		return Double.parseDouble( pTable.get( "startX"));
	}
	
	public double getStartY()
	{
		return Double.parseDouble( pTable.get( "startY"));
	}
	
	public double getEndX()
	{
		return Double.parseDouble( pTable.get( "endX"));
	}
	
	public double getEndY()
	{
		return Double.parseDouble( pTable.get( "endY"));
	}
	
	public double getInitialHeading()
	{
		return Double.parseDouble( pTable.get( "initialHeading"));
	}
	
//===================================================================
//CONFIG FOR OBSTACLES
//===================================================================
	//deployment for obstacles
	public int getObsMinX()
	{
		return Integer.parseInt( pTable.get( "obsMinX"));
	}
	
	public int getObsMaxX()
	{
		return Integer.parseInt( pTable.get( "obsMaxX"));
	}
	
	public int getObsMinY()
	{
		return Integer.parseInt( pTable.get( "obsMinY"));
	}
	
	public int getObsMaxY()
	{
		return Integer.parseInt( pTable.get( "obsMaxY"));
	}
}
