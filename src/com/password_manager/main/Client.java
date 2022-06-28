package com.password_manager.main;

import java.security.Security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.password_maanger.team.TeamOperationHandler;
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

	
	
private static void addPassword()
{
	PasswordOperationHandler poh=new PasswordOperationHandler();
	System.out.println("Enter the required credentials to add the password");
	Password new_password=MethodKeeper.receivePasswordDetails(user);
	boolean added=poh.addPassword(new_password,user);
	    if(added)
	    {
	    	System.out.println("Password added successfully "+MethodKeeper.getLikeSymbol());
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
			String user_ids=sc.nextLine();					
			uoh.removeUser(lst_usr,user_ids);
		}
		else
		{
			System.out.println("Oops! no users found in your organisation. Try adding users to your organisation.");
		}
	}
	else if(data.equals("change master password"))
	{
		uoh=new UserOperationHandler();
		uoh.changeMasterPassword();
	}
	else if(data.equals("edit user role"))
	{
		uoh=new UserOperationHandler();
		uoh.editUserRole();
	}
	else if(data.equals("create team"))
	{
		TeamOperationHandler toh=new TeamOperationHandler();
		toh.createTeam();
	}
	else if(data.equals("show team"))
	{
		TeamOperationHandler toh=new TeamOperationHandler();
		toh.showTeam();
	}
	else if(data.equals("transfer ownership"))
	{
		UserOperationHandler uoh=new UserOperationHandler();
		uoh.transferSuperAdminOwnership();
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
		System.out.println("		Main Menu \n");
		System.out.println("Select any of these options: ");
			int option;
				while(true&&user.getRole()==1)
				{
					
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"change master password",5,"add user",6,"remove user",7,"edit user role",8,"create team",9,"show team"));
					opt_map.put(10, "transfer ownership");
					opt_map.put(11,"logout");
					option=MethodKeeper.receiveIntegerInput("\n1.Add password \n2.Show password  \n3.Show Trash \n4.Change Master Password \n5.Add user \n6.Remove user \n7.Edit user role \n8.Create Team \n9.Show team\n10.Transfer Super Admin Ownership \n11.Logout");
					
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
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"change master password",5,"add user",6,"remove user",7,"edit user role",8,"create team",9,"show team",10,"logout"));	
					option=MethodKeeper.receiveIntegerInput("\n1.Add password \n2.Show password  \n3.Show Trash \n4.Change Master Password \n5.Add user \n6.Remove user \n7.Edit user role \n8.Create Team \n9.Show team \n10.Logout");
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
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"change master password",5,"logout"));
					option=MethodKeeper.receiveIntegerInput("\n1.Add password \n2.Show Password \n3.Show Trash \n4.Change master password \n5.Logout");
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
					Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"change master password",5,"logout"));
					option=MethodKeeper.receiveIntegerInput("\n1.Add password \n2.Show Password \n3.Show Trash \n4.Change master password \n5.Logout");
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
				Map<Integer,String>opt_map=new HashMap<>(Map.of(1,"add password",2,"show password",3,"show trash",4,"change master password",5,"logout"));
				option=MethodKeeper.receiveIntegerInput("\n1.Add password \n2.Show Password \n3.Show Trash \n4.Change master password \n5.Logout");
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
			option=MethodKeeper.receiveIntegerInput("1.Sign Up \n2.Login ");
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
					System.out.println("\n Welcome back "+user.getUser_name()+" 😊 \n");
					break;
				}
			}
		}
		
		if(user!=null)
		{
			mainMenu();
		}
	}
	
	
	public static void main(String args[]) throws Exception
	{
		showHomePage();
		mainMenu();
	}
}
