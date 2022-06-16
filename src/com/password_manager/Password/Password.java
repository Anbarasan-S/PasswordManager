package com.password_manager.Password;

import java.security.spec.KeySpec;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.password_maanger.encryptor.Cryptographer;
import com.password_manager.dao.EmployeeDAO;
import com.password_manager.main.MethodKeeper;
import com.password_manager.main.Client;
import com.password_manager.main.Warner;
import com.password_manager.user.User;

public class Password 
{	
	private int pass_id,user_id,changed_by_id,is_own,owner_pass_id;
	private String site_name,site_url,site_password,site_user_name;
	private Timestamp created_at,last_changed;
	private EmployeeDAO emp_dao=null;
	private Scanner sc;
	
	public Password()
	{
		emp_dao=new EmployeeDAO();
		sc=new Scanner(System.in);
	}
	
	public void setPass_id(int pass_id) {
		this.pass_id = pass_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public void setChanged_by_id(int changed_by_id) {
		this.changed_by_id = changed_by_id;
	}
	public void setIs_own(int is_own) {
		this.is_own = is_own;
	}
	public void setOwner_pass_id(int owner_pass_id) {
		this.owner_pass_id = owner_pass_id;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public void setSite_url(String site_url) {
		this.site_url = site_url;
	}

	
	
	public void setSite_password(String site_password,int mode)
	{
		try
		{
			if(mode==0)
			{
				this.site_password=site_password;
				return;
			}
			else if(mode==1)
			this.site_password=new Cryptographer().ecEncryptWithPublicKey(Client.getUser().getPublic_key(),site_password);			
		}
		catch(Exception ex)
		{
			System.out.println("Exception in set site password "+ex.getMessage());
		}
	}

	
	public void setSite_user_name(String site_user_name) {
		this.site_user_name = site_user_name;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public void setLast_changed(Timestamp last_changed) {
		this.last_changed = last_changed;
	}
	

	public int getPass_id() {
		return pass_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public int getChanged_by_id() {
		return changed_by_id;
	}
	public int getIs_own() {
		return is_own;
	}
	public int getOwner_pass_id() {
		return owner_pass_id;
	}
	public String getSite_name() {
		return site_name;
	}
	public String getSite_url() {
		return site_url;
	}
	public String getSite_password(int mode) 
	{
		if(mode==0)
			return this.site_password;
		return  new Cryptographer().decryptWithPrivateKey(this.site_password);
	}
	public String getSite_user_name() {
		return site_user_name;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public Timestamp getLast_changed() {
		return last_changed;
	}
	
	public boolean addPassword(User user)
			   {
				  return emp_dao.addPassword(this,user);
			   }
			
			
			public HashMap<String,Password> showPasswordOverview(int user_id,int status)
			   {
				   try
					  {
						 List<Password>lst_password=emp_dao.showPassword(status);
						 HashMap<String,Password> details=new HashMap<>();
						 int ind=1;   
						 if(lst_password.size()>0)
						 {
							 System.out.println("The list of Passwords available: ");
						 }
						 for(Password pass:lst_password)
						 {
							 if(pass.getSite_url()==null)
							 {
								 pass.setSite_url("");
							 }
							 details.put(pass.getSite_name().toLowerCase(), pass);
							 //Set to decrypt mode
							 System.out.println("	"+ind+".)	Name: "+pass.getSite_name());
							 ind++;
						 }
						 if(lst_password.size()==0)
						 {
							 
							 return null;
						 }
						 else
						 {
							 return details;
						 }
					  }
					  catch(Exception ex)
					  {
						  System.out.println("Trouble getting the password "+ex.getMessage());
						  return null;
					  }  
			   }
			   
				   
			public void showPassword(User user)
			{
				int user_id=user.getUser_id();
				HashMap<String,Password>details=showPasswordOverview(user_id,1);
				if(details==null)
				{
					System.out.println("Oops! it looks like you don't have any passwords. Try adding some passwords");				
				}
				else
				{
				 while(true)
					{
					 System.out.println("\n Enter the name of the password for detailed description of the password (Press enter if you wish to go to main menu): ");
					 Scanner sc=new Scanner(System.in);
					 
					 String str_inp=sc.nextLine().toLowerCase();
					 if(str_inp.isEmpty())
					 {
						 Client.mainMenu();
					 }
					 str_inp=str_inp.trim();
					 if(details.containsKey(str_inp))
					 {
						 Password temp_pass=details.get(str_inp);
						 System.out.println(" Name: "+temp_pass.getSite_name()+"\n Url: "+temp_pass.getSite_url()+"\n Username: "+temp_pass.getSite_user_name()+ " \n Password: "+temp_pass.getSite_password(1));
						 System.out.println(" Last Changed: "+temp_pass.getLast_changed()+" ");
						 System.out.println("Press 1. To edit this password 2. To move this password to trash(Or press any other number to continue viewing password):");
						 int choice=sc.nextInt();
						 if(choice==1)
						 {
							 editPassword(temp_pass);
						 }
						 else if(choice==2)
						 {
							 moveToTrash(temp_pass);
						 }
					 }
					 else
					 {
						 System.out.println("   Sorry we can't find any password with the entered password name");
					 }
					}
				}
				}
			
			public void moveToTrash(Password pass)
			{
					System.out.println("Are you sure you want to move this password to trash(Press y to move to trash or press any other character to cancel): ");
					char ch=sc.nextLine().charAt(0);
					if(ch=='y')
					{
						emp_dao.changeTrashState(pass.getPass_id(),Client.getUser().getUser_id(),0);	
						System.out.println("Password moved to trash successfully ");
					}
				else
				{
					System.out.println("Enter a valid password name to delete ");
				}
				}
			
			public void showTrash()
			{
				HashMap<String,Password>details=showPasswordOverview(Client.getUser().getUser_id(),0);
				
				if(details.isEmpty())
				{
					System.out.println("Oops! it looks like you don't have any passwords. Try adding some passwords");				
				}
				else
				{
				while(true)
				{
				System.out.println("Enter the password name to restore or delete from trash or press enter to empty trash(Or enter double space to go to main-menu): ");
				String str_inp=sc.nextLine().toLowerCase();
				if(str_inp.isEmpty())
				{
					System.out.println("Are you sure you want to empty trash(Press y to continue or any other character to cancel):");
					if(sc.nextLine().charAt(0)=='y')
					{
						emp_dao.clearTrash(Client.getUser().getUser_id());
					}
					
				}
				if(str_inp.equals("  "))
				{
					Client.mainMenu();
				}
				if(details.containsKey(str_inp))
				{
					Password temp_pass=details.get(str_inp);
					System.out.println("Enter 1. to restore the password 2.To delete the password permanently ");
					int opt=sc.nextInt();
					if(opt==1)
					{
						emp_dao.changeTrashState(temp_pass.getPass_id(), Client.getUser().getUser_id(),1);
					}
					else if(opt==2)
					{
						System.out.println("Are you sure you want to delete the password permanently (Press y to continue or any other key to cancel):");
						emp_dao.deletePassword(temp_pass);
					}
				}
				}
			}
			}
			
			
			public void editPassword(Password temp_pass)
			{
			boolean edit=false;
						 
						 System.out.println("\n\nPress 1. To edit the site name\n 2. To edit the site url\n 3. To edit the site username\n 4. To edit the site password\n (Or press any other number to go back) (To edit multiple fields enter the above described option separated by spaces Example:(1 2 3 4))");
						 String edit_option=sc.nextLine();
						 List<String>opt_list=new ArrayList<String>(Arrays.asList(edit_option.split(" ")));
						 //Edit site name
						 
						 if(opt_list.indexOf("1")!=-1)
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
						 }
						 //Edit site username
						 if(opt_list.indexOf("3")!=-1)
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
						 }
						 //Edit site password
						 if(opt_list.indexOf("4")!=-1)
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
						 }
						 if(edit)
						 {
							 emp_dao.editPassword(temp_pass);
							 System.out.println("Password updated successfully: ");
						 }

					 }		
}
