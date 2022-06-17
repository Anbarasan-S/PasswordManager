package com.password_manager.main;

import java.util.Scanner;

import com.password_manager.dao.UserDAO;
import com.password_manager.user.User;

public class Login
{
	public  User verifyCredentials()
    {
        Scanner sc=new Scanner(System.in);
        User emp;
        String user_name,master_password;
            System.out.println("Enter your username: ");
            user_name=sc.nextLine();
            System.out.println("Enter your master password: ");
            master_password=sc.nextLine();
            UserDAO empDao=new UserDAO();
            if(!empDao.userExists(user_name))
            {
            	System.out.println("Sorry we couldn't find the user with the user name specified. Please sign up");
            	return null;
            }
            	emp=empDao.verifyHashedPassword(master_password,user_name);
            	if(emp==null)
            	{
            		System.out.println("Master password does not match");
            	}
            	
           return emp;
        }
   }
