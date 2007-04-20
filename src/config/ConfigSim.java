package config;

/*
 * Class Name:    config.ConfigSim
 * Last Modified: 4/2/2006 3:7
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

/**
 * Extracts configuation information specific to the @see Simulator objects.
*/
public class ConfigSim extends Config
{
    public ConfigSim( String configFileName ) throws IOException
    {
        super( configFileName );
    }

    public String getEnvConfigFileName()
    {
        return pTable.get( "envConfigFileName" );
    }

    public String[] getAgentConfigFileNames()
    {
        return pTable.get( "agentConfigFileNames" ).split( "\\," );
    }
    
    public String[] getObjectConfigFileNames()
    {
    	return pTable.get( "objectConfigFileNames").split( "\\,");
    }
    
    public int getNumberOfTeams()
    {
    	return Integer.parseInt(pTable.get( "numberOfTeams" ));
    }
    
    public String getBuildingsFileName()
    {
        return pTable.get( "buildingsFileName" );
    }
}
