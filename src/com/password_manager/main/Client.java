package com.password_manager.main;

import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.password_manager.Password.Password;
import com.password_manager.dao.EmployeeDAO;
import com.password_manager.user.User;
public class Client 
{
	static boolean login=false;
	private static User user=null;
	private static Scanner sc=new Scanner(System.in);
	private static Password password=null;
	
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
	System.out.println("Enter the required credentials to add the password");
	Password new_password=MethodKeeper.receivePasswordDetails(user);
	 boolean added=new_password.addPassword(user);
	    if(added)
	    {
	    	System.out.println("Password added successfully");
	    }
	    else 
	    {
	    	System.out.println("Sorry we couldn't add the password");
	    }
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
	else if(data.equals("remove user"))
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
}

private static void showPassword()
{
	password.showPassword(user);
}
	
	public static void mainMenu()
	{
		//For super-admin
	
		
			int option;
				while(true&&user.getRole()==1)
				{
					System.out.println("1.Add password 2.Show password  3.Show Trash 4.Add user 5.Remove user 6.Logout");
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
					System.out.println("1.Add password 2.Show password  3.Show Trash 4.Add user 5.Remove user 6.Logout");
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
					System.out.println("1.Add password 2.Show Password 3.Show Trash 4.Logout");
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
					System.out.println("1.Add password 2.Show Password 3.Show Trash 4.Logout");
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
				System.out.println("1.Add password 2.Show Password 3.Show Trash 4.Logout");
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
	password.showTrash();
}
	
	private static void showHomePage()
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
				if(user!=null)
				{
					System.out.println(user);					
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
