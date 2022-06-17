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
		System.out.println("Select any of the options to add password: ");
		System.out.println(" 1.)Edit password\n 2.)Confirm adding password\n 3.)Cancel");
		int choice=sc.nextInt();
		if(choice==1)
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
		else if(choice==2)
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
			break;
		}
		else
		{
			 int val=details.size()+1;
			 System.out.print("\n Enter the row number of the password for performing more operations "+"(For multiple select separate the row number with spaces (Example:1 2 3 4)) "+(details.size()+1)+".)Select all");
			 Scanner sc=new Scanner(System.in);
			 String str_inp=sc.nextLine();
			 String inp_arr[]=str_inp.split(" ");
			 if(inp_arr.length>1)
			 {
				 System.out.println("1.) Restore passwords\n2.)Delete passwords");
				 List<Integer>errors=new ArrayList<Integer>();
				 List<Password> temp_passes=new ArrayList<>();
				 for(String input:inp_arr)
				 {
					 try
					 {
						 int values=Integer.parseInt(input);
						 if(values>0&&values<=details.size())
						 {
							 temp_passes.add(details.get(values-1));							 
						 }
						 else
						 {
							 errors.add(values);
						 }
					 }
					 catch(Exception ex)
					 {
						 
					 }
				 }
				 if(temp_passes.size()>0)
				 {
					 pass_dao=new PasswordDAO();
					 System.out.println("The selected passwords are: ");
					 int ind=0;
					 int pass_ids[]=new int[temp_passes.size()];
					 for(Password pass:temp_passes)
					 {
						 pass_ids[ind]=pass.getPass_id();
						 System.out.println(ind+".) "+pass.getSite_name());
						 ind++;
					 }
					if(temp_passes.size()==1)
					System.out.println("1.)Restore the selected password \n2.)Delete the selected  password permanently\n3.)");
					else
					System.out.println("1.)Restore the selected passwords \n2.)Delete the selected passwords permanently");
					System.out.println("3.)Go to previous menu");
					int choice=sc.nextInt();
					if(choice==1)
					{
						boolean state=pass_dao.changeTrashState(pass_ids,Client.getUser().getUser_id(),1);
						if(state)
						{
							System.out.println("The selected passwords are restored successfully "+"\uD83D\uDC4D");
						}
					}
					else if(choice==2)
					{
						pass_dao.deletePassword(pass_ids);
					}
					else if(choice==3)
					{
						
					}
				 }
				 else
				 {
					 System.out.println("Select valid row numbers!!");
				 }
			 }
			 if(numb==val)
			 {
				 Client.mainMenu();
			 }

			 if(details.size()>=numb||numb>0)
			 {
				 Password temp_pass=details.get(numb-1);
				 while(pass_dao.isActivePassword(temp_pass.getPass_id()))
				 {
				 System.out.println("\nWhat do you wanna do with the selected password "+MethodKeeper.printBlock(temp_pass.getSite_name())+" :\n \n1.)View Password \n2.)Edit Password \n3.)View and Edit Password \n4.)Delete Password \n5.)View and Delete Password  \n6.)Go back");
				 int opt=sc.nextInt();
				 if(opt==1)
				 {
					 printPassword(temp_pass);
					 System.out.println("1.)Go to previous menu\n2.)Go to main menu");
					 int choice=sc.nextInt();
					 if(choice==2)
					 {
						 Client.mainMenu();
						 
					 }
				 }
				 else if(opt==2)
				 {
					 editPassword(temp_pass);
				 }
				 else if(opt==3)
				 {
					 printPassword(temp_pass);
					 editPassword(temp_pass);
				 }
				 else if(opt==4)
				 {
					 deletePassword(temp_pass);					 					 
				 }
				 else if(opt==5)
				 {
					 printPassword(temp_pass);
					 deletePassword(temp_pass);
				 } 
				 else if(opt==6)
				 {
					 break;
				 }
			 }
				 }
			 else
			 {
				 System.out.println("Invalid row number!!");
			 }
			}
		}
		}
	
	public void printPassword(Password temp_pass)
	{
		 System.out.println(" Name: "+temp_pass.getSite_name()+"\n Url: "+temp_pass.getSite_url()+"\n Username: "+temp_pass.getSite_user_name()+ " \n Password: "+temp_pass.getSite_password(1));
		 
		 if(temp_pass.getLast_changed()!=null)
		 System.out.println(" Last Changed: "+temp_pass.getLast_changed()+"\n");							
		 else
			 System.out.println("");
	}
	
	
	//Move To Trash
	public void deletePassword(Password pass)
	{
		    System.out.println("What action do you want to perform on the selected password "+MethodKeeper.printBlock(pass.getSite_name()));
		    System.out.println("1.)Delete password permanently("+MethodKeeper.printBlock("Note:This can't be undone. The deleted password will be permanently deleted")+")\n2.)Move to trash\n3.)Go to previous menu");
		    System.out.println("");
		    int choice=sc.nextInt();
		    if(choice==1)
		    {
			    pass_dao=new PasswordDAO();
			    pass_dao.deletePassword(pass);
			    System.out.println("Password deleted successfully "+"\uD83D\uDC4D");
		    }
		    else if(choice==2)
			{
			    pass_dao=new PasswordDAO();
				pass_dao.changeTrashState(pass.getPass_id(),Client.getUser().getUser_id(),0);	
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
		ArrayList<Password>details=showPasswordOverview(Client.getUser().getUser_id(),0);
		if(details==null)
		{
			System.out.println("Oops! it looks like you don't have any passwords. Try adding some passwords");				
		}
		else
		{
			int val=details.size()+1;
		while(true)
		{
			System.out.println("\n");
			System.out.println("\n"+(val+1)+".) Go to main menu");
		int numb=sc.nextInt();
		if(numb==val)
		{
			System.out.println("Are you sure you want to empty trash: ");
			System.out.println("1.Empty Trash \n 2.Cancel");
			if(sc.nextInt()==1)
			{
				pass_dao.clearTrash(Client.getUser().getUser_id());
			}
		}
		else if(numb==val+1)
		{
			Client.mainMenu();
		}
		else if(numb>0||numb<=details.size())
		{
			Password temp_pass=details.get(numb-1);
			System.out.println("Select an option:\n1.Restore the password \n2.Delete the password permanently \n3.Go back \n4.To go to main menu");
			int opt=sc.nextInt();
			sc.nextLine();
			if(opt==1)
			{
				pass_dao.changeTrashState(temp_pass.getPass_id(), Client.getUser().getUser_id(),1);
				System.out.println("	Password restored successfully "+"\uD83D\uDC4D");
			}
			else if(opt==2)
			{
				System.out.println("Are you sure you want to delete the password permanently (Press y to continue or any other key to cancel):");
				String inp=sc.nextLine();
				if(inp.charAt(0)=='y')
				{
					pass_dao.deletePassword(temp_pass);
					System.out.println("Password deleted permanently "+"\uD83D\uDC4D");
				}
				else
				{
					System.out.println("Password is not deleted ");
				}
				
			}
			else if(opt==4)
			{
				Client.mainMenu();
			}
		}
		else
		{
			System.out.println("\n\n	Invalid row number!!");
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
				 System.out.println("Select any of the options to edit the password: ");
				 System.out.println("\n1. Edit the site name\n2. Edit the site url\n3. Edit the site username\n4. Edit the site password\n (Or press any other number to go back) (To edit multiple fields enter the above described option separated by spaces Example:(1 2 3 4))");
				 int choice;
				 String edit_option=sc.nextLine();
				 if(edit_option.equals("\n")||edit_option.isEmpty())
				 {
					 edit_option=sc.nextLine();
				 }
				 List<String>opt_list=new ArrayList<String>(Arrays.asList(edit_option.split(" ")));
				 //Edit site name
				 
				 if(opt_list.indexOf("1")!=-1)
				 {
					 System.out.println("\n 1.) Add name \n2.) Continue to next field");
					 choice=sc.nextInt();
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
					 System.out.println("\n1.) Add site username \n2.) Continue to next field");
					 choice=sc.nextInt();
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
					 System.out.println("\n1.) Add site password \n2.) Continue to next field");
					 choice=sc.nextInt();
					 if(choice==1)
					 {
					 do
						{
							System.out.println("	1. Enter the password for the site manually \n	2. Automatically generate a strong password");
							int option=sc.nextInt();
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
					 System.out.println("\n1.)Edit password \n2.)Cancel and go to previous menu \n3.)Go to main-menu ");
					 choice=sc.nextInt();
					 if(choice==1)
					 {
						 pass_dao.editPassword(temp_pass);
						 System.out.println("Password updated successfully "+"\uD83D\uDC4D");						 
					 }
					 else if(choice==3)
					 {
						 Client.mainMenu();
					 }
				 }
				 else
				 {
					 System.out.println("No field has changed!!");
				 }

			 }	
}
