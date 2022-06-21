package com.password_manager.Password;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.password_manager.dao.PasswordDAO;
import com.password_manager.dao.UserDAO;
import com.password_manager.main.Client;
import com.password_manager.main.MethodKeeper;
import com.password_manager.main.Warner;
import com.password_manager.user.User;

public class PasswordOperationHandler 
{
	UserDAO emp_dao=null;
	PasswordDAO pass_dao=null;
	private static Scanner sc=null;
	private Map<String,String>edit_data=new HashMap<>();
	
	public PasswordOperationHandler()
	{
		 emp_dao=new UserDAO();
		 pass_dao=new PasswordDAO();
		 sc=new Scanner(System.in);
	}
	
	//Add password 
	public boolean addPassword(Password password,User user)
	   {
		pass_dao=new PasswordDAO();
		do 
	{
		System.out.println("Take a look at the password before adding the password:\n");
		printPassword(password);
		int choice=MethodKeeper.receiveIntegerInput("Select any of the options to add password: \n 1.)Confirm adding password\n 2.)Edit password\n 3.)Cancel");
		if(choice==2)
		{
			edit_data.putIfAbsent("edit", "yes");
			editPassword(password);
			if(edit_data.containsKey("site_name"))
			{
				password.setSite_name(edit_data.get("site_name"));
			}
			if(edit_data.containsKey("site_username"))
			{
				password.setSite_user_name(edit_data.get("site_username"));
			}
			if(edit_data.containsKey("site_password"))
			{
				password.setSite_password(edit_data.get("site_password"),1);
			}
			if(edit_data.containsKey("site_url"))
			{
				password.setSite_url(edit_data.get("site_url"));
			}
			edit_data.clear();
		}
		else if(choice==1)
		{
			return pass_dao.addPassword(password,user);			
		}
		else if(choice==3)
		{
			return false;
		}
	}while(true);
		}
	
	
	//Show password overview
	public ArrayList<Password> showPasswordOverview(int user_id,int status)
	   {
		   try
			  {
			   pass_dao=new PasswordDAO();
				 ArrayList<Password>lst_password=pass_dao.showPassword(status);
				 int ind=1;   
				 if(lst_password.size()>0&&status==1)
				 {
					 System.out.println("The list of Passwords available: ");
				 }
				 else if(lst_password.size()>0&&status==0)
				 {
					 System.out.println("Passwords in the trash: ");
				 }
				 
				 for(Password pass:lst_password)
				 {
					 if(pass.getSite_url()==null)
					 {
						 pass.setSite_url("");
					 }
					 
					 System.out.println("	"+ind+".)Password Name: "+pass.getSite_name());
					 ind++;
				 }
				 if(lst_password.size()==0)
				 {
					 return null;
				 }
				 else
				 {
					 return lst_password;
				 }
			  }
			  catch(Exception ex)
			  {
				  System.out.println("Trouble getting the password "+ex.getMessage());
				  return null;
			  }  
	   }
	   
		   
	//Show password
	public void showPassword(User user)
	{
		pass_dao=new PasswordDAO();
		int user_id=user.getUser_id();
		while(true)
		{
		ArrayList<Password>details=showPasswordOverview(user_id,1);
		if(details==null)
		{
			System.out.println("Oops! it looks like you don't have any passwords. Try adding some passwords");	
			int choice=MethodKeeper.receiveIntegerInput("1.)Go back");
			if(choice==1)
			{
				break;				
			}
			else
			{
				System.out.println("Invalid input!!");
				continue;
			}
		}
		else
		{
			
			System.out.println((details.size()+1)+".) "+MethodKeeper.printBlock("Go back"));
			int numb=MethodKeeper.receiveIntegerInput("\n Select any of the option for performing more operations ");
			if(numb==details.size()+1)
				break;
			if(details.size()>=numb&&numb>0)
			 {
				 Password temp_pass=details.get(numb-1);
				 while(pass_dao.isActivePassword(temp_pass.getPass_id()))
				 {
				 int opt=MethodKeeper.receiveIntegerInput("\nWhat do you wanna do with the selected password "+MethodKeeper.printBlock(temp_pass.getSite_name())+" :\n \n1.)View Password \n2.)Edit Password  \n3.)Delete Password \n4.)Go back");
				 if(opt==1)
				 {
					 printPassword(temp_pass);
					 int choice=MethodKeeper.receiveIntegerInput("\n1.)Edit password\n2.)Delete password\n3.)Go back\n");
					 if(choice==1)
					 {
						 editPassword(temp_pass);
					 }
					 else if(choice==2)
					 {
						 deletePassword(temp_pass);
					 }
					 else if(choice==3)
					 {
						 
					 }
					 else
					 {
						 System.out.println("Invalid input!!");
					 }
				 }
				 else if(opt==2)
				 {
					 editPassword(temp_pass);
				 }
				 else if(opt==3)
				 {
					 deletePassword(temp_pass);					 					 
				 }
				 else if(opt==4)
				 {
					break; 
				 }
				 else
				 {
					 System.out.println("Invalid input!!");
				 }
				 }
				 }
			 else
			 {
				 System.out.println("Invalid input!!");
			 }
		}
		}
		}
	
	public void printPassword(Password temp_pass)
	{
		 System.out.print(" Name: "+temp_pass.getSite_name()+"\n Url: "+temp_pass.getSite_url()+"\n Username: "+temp_pass.getSite_user_name()+ " \n Password: "+temp_pass.getSite_password(1));
		 
		 if(temp_pass.getLast_changed()!=null)
		 System.out.println(" Last Changed: "+temp_pass.getLast_changed()+"\n"+" Password Strength: "+strengthCalculator(temp_pass.getSite_password(1)));
		 else
			 System.out.println("Password Strength: "+strengthCalculator(temp_pass.getSite_password(1)));
	}
	
	
	//Move To Trash
	public void deletePassword(Password pass)
	{
		    String message="What delete action do you want to perform on the selected password "+MethodKeeper.printBlock(pass.getSite_name())+"\n1.)Move password to trash"+"\n2.)Delete password permanently("+MethodKeeper.printBlock("Note:This can't be undone. The deleted password will be permanently deleted")+")\n3.)Cancel\n";
		    int choice=MethodKeeper.receiveIntegerInput(message);
		    if(choice==2)
		    {
			    pass_dao=new PasswordDAO();
			    pass_dao.deletePassword(new int[]{pass.getPass_id()});
			    System.out.println("Password deleted successfully "+"\uD83D\uDC4D");
		    }
		    else if(choice==1)
			{
			    pass_dao=new PasswordDAO();
				pass_dao.changeTrashState(new int[] {pass.getPass_id()},Client.getUser().getUser_id(),0);	
				System.out.println("Password moved to trash successfully "+"\uD83D\uDC4D");
			}
			else
			{
				System.out.println("Password not moved to trash ");
			}
	}
	
	//Show Trash
	public void showTrash()
	{
		pass_dao=new PasswordDAO();
		while(true)
		{
		ArrayList<Password>details=showPasswordOverview(Client.getUser().getUser_id(),0);
		if(details==null)
		{
			String message="No passwords found in the trash"+"\n1.) Go back";
			int opt=MethodKeeper.receiveIntegerInput(message);
			if(opt==1)
			{
				break;
			}
			else
			{
				System.out.println("Invalid option!!");
			}
		}
		else
		{
			System.out.println("\n	"+(details.size()+1)+".)"+MethodKeeper.printBlock("Empty trash"));
			System.out.println("	"+(details.size()+2)+".)"+MethodKeeper.printBlock("Go back"));
			 int val=details.size()+1;
			 
			 int inp=MethodKeeper.receiveIntegerInput("\n Select any of the option for performing more operations: ");
			 if(inp==details.size()+1)
			 {
				 
				 int pass_ids[]=new int[details.size()],size=details.size();
				 for(int ind=0;ind<size;ind++)
				 {
					 pass_ids[ind]=details.get(ind).getPass_id();
				 }
				 
				 String message="Are you sure you want to empty the trash (Note:Emptying the trash will delete all the passwords in the trash permanently): "+"\n1.)Empty trash\n2.)Cancel";
				 int opt=MethodKeeper.receiveIntegerInput(message);
				 if(opt==1)
				 {
					 boolean deleted=pass_dao.deletePassword(pass_ids);
					 if(deleted)
					 {
						 System.out.println("All the passwords in the trash have been deleted permanently "+"\uD83D\uDC4D"+"\n");
						 continue;
					 }
				 }
				 else
				 {
					 continue;
				 }
			 }
			 else if(inp==details.size()+2)
			 {
				 break;
			 }
			 if(inp<=0||inp>details.size())
			 {
				 System.out.println("Invalid option Enter a valid option");
				 continue;
			 }
			 
			 
			 System.out.println("What you wanna do with the selected password "+ MethodKeeper.printBlock(details.get(inp-1).getSite_name()));
			 
					
				 Password pass=details.get(inp-1);				 
				 
					int choice=MethodKeeper.receiveIntegerInput("1.) Restore password\n2.) Delete password\n3.) Go back");
					if(choice==1)
					{
						String message="Are you sure you want to restore the password "+pass.getSite_name()+"\n1.)Restore password 2.)Cancel";
						choice=MethodKeeper.receiveIntegerInput(message);
						if(choice==1)
						{
							boolean state=pass_dao.changeTrashState(new int[] {pass.getPass_id()},Client.getUser().getUser_id(),1);
							if(state)
							{
								System.out.println("The selected password have been restored successfully "+"\uD83D\uDC4D"+"\n");
							}							
						}
						
					}
					else if(choice==2)
					{
					
						String message="Are you sure you want to delete the password "+pass.getSite_name()+"\n1.)Delete password 2.)Cancel";
						choice=MethodKeeper.receiveIntegerInput(message);
						if(choice==1)
						{
							boolean state=pass_dao.deletePassword(new int[] {pass.getPass_id()});
							if(state)
							{ 
								System.out.println("The selected password have been deleted successfully "+"\uD83D\uDC4D"+"\n");
							}							
						}
					}
					else if(choice==3)
					{
						continue;
					}
					else
					{
						System.out.println("Select a valid option!!");
					}
				 }
			 }
		}
	
	
	
	//Edit Password
	public void editPassword(Password temp_pass)
	{
	String site_password="";
	int user_id=Client.getUser().getUser_id();
	boolean edit=false;
				 System.out.println("Select any of the options to edit the password "+MethodKeeper.printBlock(temp_pass.getSite_name())+": ");
				 System.out.println("\n1. Edit the name\n2. Edit the site url\n3. Edit the site username\n4. Edit the site password\n (Or press any other number to go back) (To edit multiple fields enter the above described option separated by spaces Example:(1 2 3 4))");
				 int choice;
				 String edit_option=sc.nextLine();
				 if(edit_option.equals("\n")||edit_option.isEmpty())
				 {
					 edit_option=sc.nextLine();
				 }
				 List<String>opt_list=new ArrayList<String>(Arrays.asList(edit_option.split(" ")));
				 //Edit site name
				 if(!(opt_list.indexOf("1")!=-1||opt_list.indexOf("2")!=-1||opt_list.indexOf("3")!=-1||opt_list.indexOf("4")!=-1))
				 {
					 return;
				 }
				 
				 if(opt_list.indexOf("1")!=-1)
				 {
					 choice=MethodKeeper.receiveIntegerInput("\n 1.) Add name \n2.) Continue to next field");
					 if(choice==1)
					 {
					 String site_name="";
					 do
						{
							System.out.print("	Enter the name for the password: ");		
							site_name=sc.nextLine();	
							if(site_name.equals(temp_pass.getSite_name()))
							{
								break;
							}
						}while(emp_dao.isOccupiedName(site_name,user_id));
					 temp_pass.setSite_name(site_name);
					 edit=true;
					 if(edit_data.containsKey("edit"))
					 {
						 edit_data.put("site_name", site_name);
					 }
					}
				 }
				 //Edit site url
				 if(opt_list.indexOf("2")!=-1)
				 {					 
						 String site_url="";
						 do
						 {
							 System.out.print("	Enter the site url (Press enter if you wish not to add the site url): ");
							 site_url=sc.nextLine();			
						 }while(!site_url.isEmpty()&&!MethodKeeper.isValidUrl(site_url));
						 temp_pass.setSite_url(site_url);
						 edit=true;
						 if(edit_data.containsKey("edit"))
						 {
							 edit_data.put("site_url", site_url);
						 }
				 }
				 //Edit site username
				 if(opt_list.indexOf("3")!=-1)
				 {
					 choice=MethodKeeper.receiveIntegerInput("\n1.) Add site username \n2.) Continue to next field");
					 if(choice==1)
					 {
					 String site_user_name;
					 do
						{
							System.out.print("	Enter the username for the site: ");
							site_user_name=sc.nextLine();
							if(site_user_name.length()==0)
							{
								Warner.requiredWarning("User Name");
							}
						}while(site_user_name.length()==0);
					 temp_pass.setSite_user_name(site_user_name);
					 edit=true;
					 if(edit_data.containsKey("edit"))
					 {
						 edit_data.put("site_username", site_user_name);
					 }
					 }
				 }
				 //Edit site password
				 if(opt_list.indexOf("4")!=-1)
				 {
					 choice=MethodKeeper.receiveIntegerInput("\n1.) Add site password \n2.) Continue to next field");
					 if(choice==1)
					 {
					 do
						{
							int option=MethodKeeper.receiveIntegerInput("	1. Enter the password for the site manually \n	2. Automatically generate a strong password");
							if(option==1)
							{
								System.out.print("	Enter your password: ");
								sc.nextLine();
								
							site_password=sc.nextLine();
							if(site_password.length()<1)
							{
								Warner.requiredWarning("Password");
							}
							}
							else if(option==2)
							{
								site_password=MethodKeeper.autoGeneratePassword();
							}
						}while(site_password.length()<1);
					 temp_pass.setSite_password(site_password,1);
					 temp_pass.setLast_changed(new Timestamp(System.currentTimeMillis()));
					 edit=true;
					 if(edit_data.containsKey("edit"))
					 {
						 edit_data.put("site_password", site_password);
					 }
				 }
				 }
				 if(edit_data.containsKey("edit"))
				 {
					 return;
				 }
				 if(edit)
				 {
					 System.out.println("Take a look at the password before updating: \n\n");
					 printPassword(temp_pass);
					 choice=MethodKeeper.receiveIntegerInput("\n1.)Edit password \n2.)Cancel  ");
					 if(choice==1)
					 {
						 pass_dao.editPassword(temp_pass);
						 System.out.println("Password updated successfully "+"\uD83D\uDC4D");						 
					 }
				 }
				 else
				 {
					 System.out.println("No field has changed!!");
				 }

			 }
	
	private String strengthCalculator(String password)
	{
		boolean upper=false,lower=false,special=false,digit=false;
		for(char let:password.toCharArray())
		{
			if(Character.isUpperCase(let))
			{
				upper=true;
			}
			else if(Character.isLowerCase(let))
			{
				lower=true;
			}
			else if(Character.isDigit(let))
			{
				digit=true;
			}
			else
			{
				special=true;
			}
		}
		if(password.length()>=8&&upper&&lower&&digit)
		{
			return "Strong";
		}
		else if(password.length()>=6&&(lower||upper||special))
		{
			return "Moderate";
		}
		else if(password.length()>=4)
		{
			return "Weak";
		}
		else 
		{
			return "Very weak";
		}
	}
	
}
