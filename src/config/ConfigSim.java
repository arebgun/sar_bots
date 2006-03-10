package config;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import java.util.ArrayList;

public class ConfigSim
{
    public ConfigSim( String configFilePath )
    {

    }

    public String getEnvConfigFileName()
    {
        return "config/default.ConfigEnv";
    }

    public ArrayList<String> getAgentConfigFileNames()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add( "config/scout.ConfigAgent" );
        list.add( "config/worker.ConfigAgent" );

        return list;
    }

}
