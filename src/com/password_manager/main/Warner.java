package com.password_manager.main;

public class Warner 
{
	public static void notMatch()
	{
		System.out.println("The password do not match! Please re-enter your password correctly. Press 1. Enter actual password and re-enter password, 2.Re-enter only the verification password, 3.Go to main menu");
	}
	
	public static boolean warnPassword(String password)
	{
		int special=0,lower_case=0,upper_case=0,digit=0;
		
		
		for(char ch:password.toCharArray())
		{
			if(Character.isUpperCase(ch))
			upper_case++;
			else if(Character.isLowerCase(ch))
			lower_case++;
			else if(Character.isDigit(ch))
			digit++;
			else
			special++;
		}
		if(special==0||lower_case==0||upper_case==0||digit==0||password.length()<10)
		{
			System.out.println("The master password must atleast contains 10 characters "
					+ "and should contain one special character one lower case character one upper case character and one digit\n\n");
			return true;
		}
		return false;
	}
}
