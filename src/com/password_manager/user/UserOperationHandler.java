package com.password_manager.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.password_maanger.cryptographer.Cryptographer;
import com.password_manager.dao.PasswordDAO;
import com.password_manager.dao.UserDAO;
import com.password_manager.main.Client;
import com.password_manager.main.MethodKeeper;
import com.password_manager.main.Warner;

public class UserOperationHandler 
{
	
	private UserDAO emp_dao=new UserDAO();
	
	public void addEmployee(User emp,String user_email_id) throws Exception
    {
        emp_dao.addInviteUser(user_email_id, emp);
    }
	
	public boolean changeMasterPassword()
	{
		System.out.println("Enter your current master password or press enter to cancel: ");
		Scanner sc=new Scanner(System.in);
		String master_password=sc.nextLine();
		if(master_password.isEmpty())
		{
			return false;
		}
		User hasUser=new UserDAO().verifyHashedPassword(master_password,Client.getUser().getUser_name());
		if(hasUser==null)
		{
			System.out.println("Master password do not match");
			
			return changeMasterPassword();
		}
		else
		{
			UserDAO user_dao=new UserDAO();
			System.out.println("Enter your new master password or press enter to cancel: ");
			String new_master_password=sc.nextLine();
	
			while(Warner.warnPassword(new_master_password))
			{
				System.out.println("Enter your new master password or press enter to cancel: ");
				new_master_password=sc.nextLine();
				if(new_master_password.isEmpty())
				{
					return false;
				}				
			}
			String re_entered_password;
			do
			{
				System.out.println("Enter your new master password again(Note:If you loose your master password the master password can't be retreived)s or press enter to cancel:");
				re_entered_password=sc.nextLine();
				if(re_entered_password.isEmpty())
				{
					return false;
				}
				if(!re_entered_password.equals(new_master_password))
				{
					System.out.println("New master password not match with the re-entered password ");
				}
			}while(!new_master_password.equals(re_entered_password));
			
			boolean is_changed=user_dao.changeMasterPassword(master_password,new_master_password,Client.getUser().getUser_id());
			
			if(is_changed)
			{
				new_master_password=new Cryptographer().encryptMasterPassword(new_master_password);
				Client.getUser().setMaster_password(new_master_password);
				System.out.println("Your master password has changed successfully "+"\uD83D\uDC4D");
			}
			else
			{
				System.out.println("Your master password has not changed!!");
			}
			return true;
		}
	}
	
	public void removeUser(List<User>lst_usr,String remove_users_data)
	   {
		   String data[]=remove_users_data.split(" ");
		   List<User> removed_users=new ArrayList<>();
		   for(String row:data)
		   {
			   int row_num=Integer.parseInt(row),size=lst_usr.size();
			   if(row_num<=0||row_num>size)
			   {
				   System.out.println("Invalid row number "+row_num+" and that row number is ignored");
			   }
			   else
			   {
				   User temp_user=lst_usr.get(row_num-1);
				   boolean is_removed=emp_dao.removeUser(temp_user.getUser_id());
				   if(is_removed)
				   {
					   removed_users.add(temp_user);
				   }
				   else
				   {
					   System.out.println("Cannot remove the  user with the username "+temp_user.getUser_name());					   
				   }
			   }
		   }
		   if(removed_users.size()!=0) 
		{
		   System.out.println("The following users are removed successfully from the org and their account will permanently deleted after 5 days: ");
		   for(int ind=0;ind<removed_users.size();ind++)
		   {
			  System.out.println(ind+1+".) "+removed_users.get(ind).getUser_name()); 
		   }
     }
	   }
}
