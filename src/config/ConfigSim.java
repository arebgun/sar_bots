package config;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
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
