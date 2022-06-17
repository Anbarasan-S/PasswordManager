package com.password_manager.main;

import java.security.Security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.password_manager.Password.Password;
import com.password_manager.Password.PasswordOperationHandler;
import com.password_manager.dao.UserDAO;
import com.password_manager.user.User;
import com.password_manager.user.UserOperationHandler;
public class Client 
{
	private static boolean login=false;
	private static User user=null;
	private static Scanner sc=new Scanner(System.in);
	private static Password password=null;
	private static PasswordOperationHandler poh=null;
	private static UserOperationHandler uoh=null;
	public static User getUser()
	{
		return user;
	}
	
	static
	{
		password=new Password();
	}
	
	private static void editPassword()
	{
//		try
//		{
//		password.editPassword(user);
//		}
//		catch(Exception ex)
//		{
//			System.out.println("Exception in server.java "+ex.getMessage());
//		}
	}
	
	
private static void addPassword()
{
	PasswordOperationHandler poh=new PasswordOperationHandler();
	System.out.println("Enter the required credentials to add the password");
	Password new_password=MethodKeeper.receivePasswordDetails(user);
	boolean added=poh.addPassword(new_password,user);
	    if(added)
	    {
	    	System.out.println("Password added successfully");
	    }
	    else 
	    {
	    	System.out.println("Password not added!!");
	    }	
}

public  void sharePasswordAsOrgAdmin()
{
//	System.out.println("Share ")
}


private static void viewStrength()
{
	
}


private static void logout()
{
	user=null;
	showHomePage();
}

private static void commonMenuHandler(String data)
{
	if(data.equals("add password"))
	{
	   addPassword();
	}
	else if(data.equals("show password"))
	{
		showPassword();
	}
	else if(data.equals("logout"))
	{
		logout();
	}
	else if(data.equals("show trash"))
	{
		showTrash();
	}
	else if(data.equals("add user"))
	{
		uoh=new UserOperationHandler();
		System.out.println("Enter the invitee's email id");
		String user_email_id=sc.next();
		try
		{
			uoh.addEmployee(user,user_email_id);					
		}
		catch(Exception ex)
		{
			System.out.println("Sorry we couldn't add the employee");
		}
	}
	else if(data.equals("remove user"))
	{
		uoh=new UserOperationHandler();
		List<User>lst_usr=user.viewUsers();
		if(lst_usr.size()!=0)
		{
			System.out.println("Enter the row numbers of the user to remove, To remove multiple users seperate the row number with spaces:(1 10 20)");
			sc.nextLine();
			String user_ids=sc.nextLine();					
			uoh.removeUser(lst_usr,user_ids);
		}
		else
		{
			System.out.println("Oops! no users found in your organisation. Try adding users to your organisation.");
		}
	}
}

private static void showPassword()
{
	poh=new PasswordOperationHandler();
	poh.showPassword(user);
}
	
	public static void mainMenu()
	{
		//For super-admin
	
		System.out.println("Select any of these options: ");
			int option;
				while(true&&user.getRole()==1)
				{
					System.out.println("\n1.Add password \n2.Show password  \n3.Show Trash \n4.Add user \n5.Remove user \n6.Logout");
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"add user",5,"remove user",6,"logout"));	
					option=sc.nextInt();
					if(!opt_map.containsKey(option))
					{
						System.out.println("Invalid input!!");
						continue;
					}
					
					String entered_value=opt_map.get(option);
					commonMenuHandler(entered_value);
				}
				
				//For admin
				while(true&&user.getRole()==2)
				{
					System.out.println("\n1.Add password \n2.Show password  \n3.Show Trash \n4.Add user \n5.Remove user \n6.Logout");
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"add user",5,"remove user",6,"logout"));	
					option=sc.nextInt();
					if(!opt_map.containsKey(option))
					{
						System.out.println("Invalid input!!");
						continue;
					}
					String entered_value=opt_map.get(option);
					commonMenuHandler(entered_value);
				}
				
				//For team-admin
				while(true&&user.getRole()==3)
				{
					System.out.println("\n1.Add password \n2.Show Password \n3.Show Trash \n4.Logout");
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"logout"));
					option=sc.nextInt();
					if(!opt_map.containsKey(option))
					{
						System.out.println("Invalid input!!");
						continue;
					}
					String entered_value=opt_map.get(option);
					commonMenuHandler(entered_value);
				}
				//For an employee
				while(true&&user.getRole()==4)
				{
					System.out.println("\n1.Add password \n2.Show Password \n3.Show Trash \n4.Logout");
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"logout"));
					option=sc.nextInt();
					if(!opt_map.containsKey(option))
					{
						System.out.println("Invalid input!!");
						continue;
					}
					String entered_value=opt_map.get(option);
					commonMenuHandler(entered_value);
				}
			//For individual users
			while(true&&user.getRole()==5)
			{
				System.out.println("\n1.Add password \n2.Show Password \n3.Show Trash \n4.Logout");
				Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"logout"));
				option=sc.nextInt();
				if(!opt_map.containsKey(option))
				{
					System.out.println("Invalid input!!");
					continue;
				}
				String entered_value=opt_map.get(option);
				commonMenuHandler(entered_value);
			}	
	}
	
	
private static void showTrash()
{
	poh=new PasswordOperationHandler();
	poh.showTrash();
}
	
	private static void showHomePage()
	{
		System.out.println("Hi!! What Do You want to do\n");
		Scanner sc=new Scanner(System.in);
		int option;
		while(user==null)
		{
			System.out.println("1.Sign Up \n2.Login ");
			option=sc.nextInt();
			if(option==1)
			{
				Signup new_signup=new Signup();
				user=new_signup.initialiser();
				if(user!=null)
				{
					System.out.println("Cheers!! account created successfully");
				}
			}
			else if(option==2)
			{
				user=new Login().verifyCredentials();
				if(user!=null)
				{
					System.out.println("\n Welcome back "+user.getUser_name()+" (: \n");
}
			}
		}
	}
	
	
	public static void main(String args[]) throws Exception
	{
		showHomePage();
		mainMenu();
	}
}
