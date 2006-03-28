package config;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import java.awt.*;
import java.io.IOException;

public class ConfigAgent extends Config
{
    public ConfigAgent( String configFileName ) throws IOException
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

    public double getWingSpan()
    {
        return Double.parseDouble( pTable.get( "wingSpan" ) );
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

    public String getSensorName()
    {
        return pTable.get( "sensorName" );
    }

    public double getSensorRange()
    {
        return Double.parseDouble( pTable.get( "sensorRange" ) );
    }

    public Color getSensorColor()
    {
        String clrValues[] = pTable.get( "sensorColor" ).split( "\\," );

        return new Color( Integer.parseInt( clrValues[0] ),
                Integer.parseInt( clrValues[1] ),
                Integer.parseInt( clrValues[2] ),
                Integer.parseInt( clrValues[3] ) );
    }
}
