package com.password_manager.main;

import java.util.Scanner;

import com.password_manager.Password.*;
import com.password_manager.dao.EmployeeDAO;
import com.password_manager.user.User;
public class Server 
{
	static boolean login=false;
	private static User user=null;
	public static void main(String args[])
	{
		System.out.println("Hi!! What Do You want to do\n");
		Scanner sc=new Scanner(System.in);
		int option;
		while(user==null)
		{
			System.out.println("1.Sign Up 2.Login ");
			option=sc.nextInt();
			if(option==1)
			{
				Signup new_signup=new Signup();
				user=new_signup.initialiser();
				if(user!=null)
				{
					System.out.println("Cheers!! account created successfully");
					System.out.println(user);
				}
			}
			else if(option==2)
			{
				user=new Login().verifyCredentials();
				System.out.println(user);
			}
		}
		
		while(true&&user.getRole()==1)
		{
			System.out.println("1.Add user 2.Add multiple users 3.Remove User 4.Exit");
			option=sc.nextInt();
			if(option==1)
			{
				System.out.println("Enter the invitee's email id");
				String user_email_id=sc.next();
				try
				{
					user.addEmployee(user,user_email_id);					
				}
				catch(Exception ex)
				{
					System.out.println("Sorry we couldn't add the employee");
				}
			}
			else if(option==2)
			{
				
			}
			else if(option==3)
			{
				System.out.println("Enter the user name of the employee to remove");
				String user_name=sc.next();
				user.removeUser(user_name);	
			}
			else if(option==4)
			{
				System.exit(0);
			}
		}
	}
}
