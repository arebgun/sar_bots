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
        return "usr/conf/default.ConfigEnv";
    }

    public ArrayList<String> getAgentConfigFileNames()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add( "usr/conf/scout.ConfigAgent" );
        list.add( "usr/conf/worker.ConfigAgent" );

        return list;
    }

}
