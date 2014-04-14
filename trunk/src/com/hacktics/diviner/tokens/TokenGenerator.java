package com.hacktics.diviner.tokens;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 
 * @author Eran Tamari
 *
 */

public class TokenGenerator {

	public static final int STRING_TOKEN_TYPE = 0;
	public static final int INT_TOKEN_TYPE = 1;

	public static String GenerateTokens(int type)
	{
		Token token;
		if (type == INT_TOKEN_TYPE)
		{
		token = new Token(generateRandomNumeric());
		
		}
		else
		{
			token = new Token(generateRandomAlphanumeric());
		}
				
		return token.toString();
	}
	
	private static String generateRandomAlphanumeric()
	{
		return RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.randomNumeric(3);
	}
	
	private static String generateRandomNumeric()
	{
		return RandomStringUtils.randomNumeric(4);
	}
	
}
