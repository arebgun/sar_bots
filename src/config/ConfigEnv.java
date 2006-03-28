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

    public int getWorldWidth()
    {
        return Integer.parseInt( pTable.get( "worldWidth" ) );
    }

    public int getWorldHeight()
    {
        return Integer.parseInt( pTable.get( "worldHeight" ) );
    }

    public int getGridSize()
    {
        return Integer.parseInt( pTable.get( "gridSize" ) );
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
