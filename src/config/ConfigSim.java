package config;

/*
 * Class Name:    config.ConfigSim
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

}
