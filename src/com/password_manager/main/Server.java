package com.password_manager.main;

import java.util.List;
import java.util.Scanner;

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
			System.out.println("1.Add user 2.Remove User 3.Add password 4.Show passwords 5.Exit");
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
				List<User>lst_usr=user.viewUsers();
				if(lst_usr.size()!=0)
				{
					System.out.println("Enter the row numbers of the user to remove, To remove multiple users seperate the row number with spaces:(1 10 20)");
					sc.nextLine();
					String user_ids=sc.nextLine();					
					user.removeUser(lst_usr,user_ids);
				}
				else
				{
					System.out.println("Oops! no users found in your organisation. Try adding users to your organisation.");
				}
			}
			else if(option==3)
			{
			    boolean added=user.addPassword();
			    if(added)
			    {
			    	System.out.println("Password added successfully");
			    }
			    else 
			    {
			    	System.out.println("Sorry we couldn't add the password");
			    }
			}
			else if(option==4)
			{
				user.showPassword();
			}
			else if(option==5)
			{
				System.exit(0);
			}
			
		}
		
		
		while(true&&user.getRole()==4)
		{
			System.out.println("1.Add password 2.Show Password ");
			option=sc.nextInt();
			if(option==1)
			{
				boolean added=user.addPassword();
				if(added)
				{
					System.out.println("Password added successfully");
				}
				else
				{
					System.out.println("Sorry we couldn't add the password");
				}
			}
			else if(option==2)
			{
				user.showPassword();
			}
		}
	}
}
