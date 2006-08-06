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

public abstract class Config
{
    protected final Hashtable<String, String> pTable;

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
