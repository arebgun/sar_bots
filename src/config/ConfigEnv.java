package config;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import java.io.IOException;

public class ConfigEnv extends Config
{
    public ConfigEnv( String configFileName ) throws IOException
    {
        super( configFileName );
    }

    public double getWorldWidth()
    {
        return Double.parseDouble( pTable.get( "worldWidth" ) );
    }

    public double getWorldHeight()
    {
        return Double.parseDouble( pTable.get( "worldHeight" ) );
    }

    public String getBuildingsFileName()
    {
        return pTable.get( "buildingsFileName" );
    }

    public String getFiresFileName()
    {
        return pTable.get( "firesFileName" );
    }
}
