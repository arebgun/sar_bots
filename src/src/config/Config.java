package config;

/*
 * Class Name:    config.Config
 * Last Modified: 4/2/2006 3:6
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import java.io.*;
import java.util.Hashtable;

/**
 * Utility class for building application property hashtables based on configuration files.
 */
public abstract class Config
{
    protected final Hashtable<String, String> pTable;

    /**
     * Parses the configuration files, using the parameter name as the key into a Hashtable structure.
     * Key, Value pairs are separated by equal signs, and all values, including numbers are treated as strings.
     *
     * @param configFileName The fully qualified file path for the configuration file being parsed.
     * @throws IOException
     */
    protected Config( String configFileName ) throws IOException
    {
        pTable = new Hashtable<String, String>();

        StreamTokenizer st = new StreamTokenizer( new BufferedReader( new FileReader( configFileName ) ) );
        st.ordinaryChars( '+', '9' );
        st.wordChars( ' ', '~' );
        st.commentChar( '#' );
        st.whitespaceChars( '=', '=' );

        while ( st.nextToken() != StreamTokenizer.TT_EOF )
        {
            String property = st.sval;
            st.nextToken();
            pTable.put( property, st.sval );
        }
    }
}
