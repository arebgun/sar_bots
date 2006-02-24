/**
 * @(#) Environment.java
 */

package edu.uwyo.cs.artificialintelligence.agent;

public class Environment
{
	private static Environment singletonInstance;
	
	protected Environment( )
	{
		
	}
	
	public static Environment getInstance( )
	{
		return singletonInstance;
	}
	
	
}
