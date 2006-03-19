package config;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import java.io.*;
import java.util.*;

public abstract class Config
{   
    protected final Hashtable<String, String> pTable;

    protected Config( String configFileName ) throws IOException
    {
	pTable = new Hashtable<String, String>();

	StreamTokenizer st = new StreamTokenizer( new BufferedReader( new FileReader( configFileName ) ) );
	st.ordinaryChars('+', '9');
	st.wordChars(' ', '~');
	st.commentChar('#');
	st.whitespaceChars('=', '=');

	while ( st.nextToken() != StreamTokenizer.TT_EOF ) 
	    {
		String property = st.sval;
		st.nextToken();
		pTable.put(property, st.sval);
	    }
    }
}
