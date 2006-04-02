package config;

/*
 * Class Name:    config.ConfigEnv
 * Last Modified: 4/2/2006 2:45
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
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

    public long getFireSeed()
    {
        return Long.parseLong( pTable.get( "fireSeed" ) );
    }
}
