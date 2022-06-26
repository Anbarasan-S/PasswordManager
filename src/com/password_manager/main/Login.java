package com.password_manager.main;

import java.util.Scanner;

import com.password_manager.dao.UserDAO;
import com.password_manager.user.User;

public class Login
{
	public  User verifyCredentials()
    {
        Scanner sc=new Scanner(System.in);
        User user;
        String user_name,master_password;
    
        	System.out.println("Enter your username: ");
        	user_name=sc.nextLine();
        	UserDAO userDao=new UserDAO();
        	while(!userDao.userExists(user_name))
        	{
        		
        		System.out.println("Sorry we couldn't find the user with the user name specified. ");
        		
        		System.out.println("Please sign up or use a different username to sign in. Press enter to go back: ");
            	user_name=sc.nextLine();	
            	if(user_name.isEmpty())
            	{
            		return null;
            	}
        	}
        	System.out.println("Enter your master password: ");
        	master_password=sc.nextLine();
        	String msg[]= {""};
        	user=userDao.verifyHashedPassword(master_password,user_name,msg);
        	while(user==null)
        	{
        		if(msg[0].equals("inactive"))
            	{
            		return null;
            	}
        		System.out.println("Invalid master password!! ");
        		System.out.println("Re-enter your master password or press enter to go to main menu: ");
        		master_password=sc.nextLine();
        		if(master_password.isEmpty())
        		{
        			return null;
        		}
        		msg[0]="";
        		user=userDao.verifyHashedPassword(master_password, user_name,msg);
        	}
           return user;
        }
   }
