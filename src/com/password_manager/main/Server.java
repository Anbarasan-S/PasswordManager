package com.password_manager.main;

import java.util.List;
import java.util.Scanner;

import com.password_manager.Password.Password;
import com.password_manager.dao.EmployeeDAO;
import com.password_manager.user.User;
public class Server 
{
	static boolean login=false;
	private static User user=null;
	private static Scanner sc=new Scanner(System.in);
	public static void mainMenu()
	{
		//For super-admin
			int option;
				while(true&&user.getRole()==1)
				{
					System.out.println("1.Add user 2.Remove User 3.Add password 4.Show passwords 5.Remove password 6.Edit password 7.Exit");
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
						try
						{
							List<Password> lst_password=new EmployeeDAO().showPassword(user.getUser_id());
							int ind=1;   
							 for(Password pass:lst_password)
							 {
								 if(pass.getSite_url()==null)
								 {
									 pass.setSite_url("-");
								 }
								 pass.setSite_password(MethodKeeper.decrypt(pass.getSite_password(),"secret@123#245"));
								 System.out.println(ind+".)\n Name: "+pass.getSite_name()+"\n Url: "+pass.getSite_url()+"\n Username: "+pass.getSite_user_name()+ " \n Password: "+pass.getSite_password());
					
								 System.out.println(" Last Changed: "+pass.getLast_changed()+" ");
								 ind++;
							 }
							 if(lst_password.size()>0)
							 System.out.println("\n\nEnter the row numbers of the passwords from the above list of passwords, To remove multiple password seperate the row number with spaces:(1 10 20)");
							 System.out.println("Or enter -1 to go to main menu");
							 sc.nextLine();
							 String user_ids=sc.nextLine();					
							 if(user_ids.equals("-1"))
								 mainMenu();
							 System.out.println("Are you sure to remove the password with row numbers: "+user_ids+" Press 1 to continue or press -1 to abort and go to main menu");
							 String confirm=sc.nextLine();
							 if(confirm.equals("1"))
							 {
								 user.removePassword(lst_password,user_ids);										 
							 }
							 else
							 {
								 mainMenu();
							 }
							 
						}
						catch(Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
					else if(option==6)
					{
						try
						{
						List<Password> lst_password=new EmployeeDAO().showPassword(user.getUser_id());
						int ind=1;   
						 for(Password pass:lst_password)
						 {
							 if(pass.getSite_url()==null)
							 {
								 pass.setSite_url("-");
							 }
							 pass.setSite_password(MethodKeeper.decrypt(pass.getSite_password(),"secret@123#245"));
							 System.out.println(ind+".)\n Name: "+pass.getSite_name()+"\n Url: "+pass.getSite_url()+"\n Username: "+pass.getSite_user_name()+ " \n Password: "+pass.getSite_password());
							 System.out.println(" Last Changed: "+pass.getLast_changed()+" ");
							 ind++;
						 }
						 if(lst_password.size()==0)
						 {
							 System.out.println("Oops! it looks like you don't have any passwords. Try adding some password");
							 continue;
						 }
						int row_num;
						System.out.println("Enter the row number of the password to edit the password: ");
						row_num=sc.nextInt();
						user.editPassword(lst_password,row_num);
						}
						catch(Exception ex)
						{
							System.out.println("Exception in server.java "+ex.getMessage());
						}
					}
					else if(option==7)
					{
						System.exit(0);
					}
					else 
					{
						mainMenu();
					}
				}
				
				//For admin
				while(true&&user.getRole()==2)
				{
					System.out.println("1.Add user 2.Remove User 3.Add password 4.Show passwords 5.Remove password 6.Edit password 7.Exit");
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
						try
						{
							List<Password> lst_password=new EmployeeDAO().showPassword(user.getUser_id());
							int ind=1;   
							 for(Password pass:lst_password)
							 {
								 if(pass.getSite_url()==null)
								 {
									 pass.setSite_url("-");
								 }
								 pass.setSite_password(MethodKeeper.decrypt(pass.getSite_password(),"secret@123#245"));
								 System.out.println(ind+".)\n Name: "+pass.getSite_name()+"\n Url: "+pass.getSite_url()+"\n Username: "+pass.getSite_user_name()+ " \n Password: "+pass.getSite_password());
								 System.out.println(" Last Changed: "+pass.getLast_changed()+" ");
								 ind++;
							 }
							 if(lst_password.size()>0)
							 System.out.println("\n\nEnter the row numbers of the passwords from the above list of passwords, To remove multiple password seperate the row number with spaces:(1 10 20)");
							 System.out.println("Or enter -1 to go to main menu ");
							 sc.nextLine();
							 String user_ids=sc.nextLine();					
							 if(user_ids.equals("-1"))
								 mainMenu();
							 System.out.println("Are you sure to remove the password with row numbers: "+user_ids+" Press 1 to continue or press -1 to abort and go to main menu");
							 String confirm=sc.nextLine();
							 if(confirm.equals("1"))
							 {
								 user.removePassword(lst_password,user_ids);										 
							 }
							 else
							 {
								 mainMenu();
							 }
							 
						}
						catch(Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
					else if(option==6)
					{
						System.exit(0);
					}
					else
					{
						mainMenu();
					}
					
				}
				
				//For team-admin
				while(true&&user.getRole()==3)
				{
					System.out.println("1.Add password 2.Show Password 3.Remove Passwords 4.Edit password");
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
					else if(option==3)
					{
						try
						{
							List<Password> lst_password=new EmployeeDAO().showPassword(user.getUser_id());
							int ind=1;   
							 for(Password pass:lst_password)
							 {
								 if(pass.getSite_url()==null)
								 {
									 pass.setSite_url("-");
								 }
								 pass.setSite_password(MethodKeeper.decrypt(pass.getSite_password(),"secret@123#245"));
								 System.out.println(ind+".)\n Name: "+pass.getSite_name()+"\n Url: "+pass.getSite_url()+"\n Username: "+pass.getSite_user_name()+ " \n Password: "+pass.getSite_password());
								 System.out.println(" Last Changed: "+pass.getLast_changed()+" ");
								 ind++;
							 }
							 if(lst_password.size()>0)
							 System.out.println("\n\nEnter the row numbers of the passwords from the above list of passwords, To remove multiple password seperate the row number with spaces:(1 10 20)");
							 System.out.println("Or enter -1 to go to main menu ");
							 sc.nextLine();
							 String user_ids=sc.nextLine();					
							 if(user_ids.equals("-1"))
								 mainMenu();
							 System.out.println("Are you sure to remove the password with row numbers: "+user_ids+" Press 1 to continue or press -1 to abort and go to main menu");
							 String confirm=sc.nextLine();
							 if(confirm.equals("1"))
							 {
								 user.removePassword(lst_password,user_ids);										 
							 }
							 else
							 {
								 mainMenu();
							 }
							 
						}
						catch(Exception ex)
						{
							System.out.println(ex.getMessage());
						}
				}
				else
				{
					mainMenu();
				}
				}
				//For an employee
				while(true&&user.getRole()==4)
				{
					System.out.println("1.Add password 2.Show Password 3.Remove Passwords ");
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
					else if(option==3)
					{
						try
						{
							List<Password> lst_password=new EmployeeDAO().showPassword(user.getUser_id());
							int ind=1;   
							 for(Password pass:lst_password)
							 {
								 if(pass.getSite_url()==null)
								 {
									 pass.setSite_url("-");
								 }
								 pass.setSite_password(MethodKeeper.decrypt(pass.getSite_password(),"secret@123#245"));
								 System.out.println(ind+".)\n Name: "+pass.getSite_name()+"\n Url: "+pass.getSite_url()+"\n Username: "+pass.getSite_user_name()+ " \n Password: "+pass.getSite_password());
								 System.out.println(" Last Changed: "+pass.getLast_changed()+" ");
								 ind++;
							 }
							 if(lst_password.size()>0)
							 System.out.println("\n\nEnter the row numbers of the passwords from the above list of passwords, To remove multiple password seperate the row number with spaces:(1 10 20)");
							 System.out.println("Or enter -1 to go to main menu ");
							 sc.nextLine();
							 String user_ids=sc.nextLine();					
							 if(user_ids.equals("-1"))
								 mainMenu();
							 System.out.println("Are you sure to remove the password with row numbers: "+user_ids+" Press 1 to continue or press -1 to abort and go to main menu");
							 String confirm=sc.nextLine();
							 if(confirm.equals("1"))
							 {
								 user.removePassword(lst_password,user_ids);										 
							 }
							 else
							 {
								 mainMenu();
							 }
							 
						}
						catch(Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
					else
					{
						mainMenu();
					}
				}
				
			//For individual users
			while(true&&user.getRole()==5)
			{
				System.out.println("1.Add password 2.Show Password 3.Remove Passwords 4.Edit password");
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
				else if(option==3)
				{
					try
					{
						List<Password> lst_password=new EmployeeDAO().showPassword(user.getUser_id());
						int ind=1;   
						 for(Password pass:lst_password)
						 {
							 if(pass.getSite_url()==null)
							 {
								 pass.setSite_url("-");
							 }
							 pass.setSite_password(MethodKeeper.decrypt(pass.getSite_password(),"secret@123#245"));
							 System.out.println(ind+".)\n Name: "+pass.getSite_name()+"\n Url: "+pass.getSite_url()+"\n Username: "+pass.getSite_user_name()+ " \n Password: "+pass.getSite_password());
							 System.out.println(" Last Changed: "+pass.getLast_changed()+" ");
							 ind++;
						 }
						 if(lst_password.size()>0)
						 System.out.println("\n\nEnter the row numbers of the passwords from the above list of passwords, To remove multiple password seperate the row number with spaces:(1 10 20)");
						 System.out.println("Or enter -1 to go to main menu ");
						 sc.nextLine();
						 String user_ids=sc.nextLine();					
						 if(user_ids.equals("-1"))
							 mainMenu();
						 System.out.println("Are you sure to remove the password with row numbers: "+user_ids+" Press 1 to continue or press -1 to abort and go to main menu");
						 String confirm=sc.nextLine();
						 if(confirm.equals("1"))
						 {
							 user.removePassword(lst_password,user_ids);										 
						 }
						 else
						 {
							 mainMenu();
						 }
						 
					}
					catch(Exception ex)
					{
						System.out.println(ex.getMessage());
					}
				}
				else
				{
					mainMenu();
				}
			}
	}
	
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
		
		mainMenu();
		
}
}
