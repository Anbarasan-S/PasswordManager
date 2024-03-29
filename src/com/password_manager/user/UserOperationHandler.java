package com.password_manager.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.password_maanger.cryptographer.Cryptographer;
import com.password_maanger.team.Team;
import com.password_manager.dao.OrganisationDAO;
import com.password_manager.dao.PasswordDAO;
import com.password_manager.dao.TeamDAO;
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
		User hasUser=new UserDAO().verifyHashedPassword(master_password,Client.getUser().getUser_name(),new String[] {""});
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
				System.out.println("Enter your new master password again(Note:If you loose your master password the master password can't be retreived) or press enter to cancel:");
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
	
	public void editUserRole()
	{
		int role=Client.getUser().getRole();
		Scanner sc=new Scanner(System.in);
		
			do
			{
			ArrayList<User>users=printOrgMembers();
			if(users==null||users.size()==0)
			{
				int choice=MethodKeeper.receiveIntegerInput("	1.)Go back");
				if(choice==1)
				{
					break;						
				}
				else
				{
					System.out.println("Invlalid input!!");
				}
			}
			if(users!=null)
			{
				int val=MethodKeeper.receiveIntegerInput("Select the user you want to assign the role: ");
				if(val==users.size()+1)
				{
				break;
				}
				else if(val>0&&val<=users.size())
				{
					User user=users.get(val-1);
					System.out.println("The selected user is "+user.getUser_name());
					int inp=MethodKeeper.receiveIntegerInput("\nSelect a role to assign to "+user.getUser_name()+":\n  1.)Admin \n  2.)User");
					if(inp==1)
					{
						if(user.getRole()==2)
						{
							System.out.println("No changes were made as the user is already an admin!!");
							continue;
						}
						UserDAO user_dao=new UserDAO();
						user_dao.setUserRole(2,-1,user.getUser_id());
						System.out.println("Role of the user with the username "+user.getUser_name()+" has changed successfully "+MethodKeeper.getLikeSymbol()+"\n");
					}
					else if(inp==2)
					{
						if(user.getRole()==4)
						{
							System.out.println("No changes were made as the user was already an user!!");
							continue;
						}
						UserDAO user_dao=new UserDAO();
						user_dao.setUserRole(4,-1,user.getUser_id());
						System.out.println("Role of the user with the username "+user.getUser_name()+" has changed successfully "+MethodKeeper.getLikeSymbol()+"\n");
					}
					else
					{
						System.out.println("Invalid input!!");
					}
				}
			}
		  }while(true);
		}

	
	public ArrayList<User> printOrgMembers()
	{
		UserDAO user_dao=new UserDAO();
		ArrayList<User>users=user_dao.getOrgMembers(Client.getUser().getOrg_id(),Client.getUser().getUser_id());
		if(users==null)
		{
			System.out.println("Oops!! No members found in your organisation. Try adding members to your organisation ");
			return null;
		}
		
		System.out.println("The list of users available in your organisation: \n");
		
		int ind=1;
		for(User user:users)
		{
			System.out.println("  "+ind+".) User Name: "+user.getUser_name()+"  Role: "+MethodKeeper.getRoleAsString(user.getRole()));
			ind=ind+1;
		}
		
		System.out.println("  "+(users.size()+1)+".) "+MethodKeeper.printBlock("Go back")+"\n");
		return users;
	}
	
	public ArrayList<User> printOrgMembers(int overload)
	{
		UserDAO user_dao=new UserDAO();
		ArrayList<User>users=user_dao.getOrgMembers(Client.getUser().getOrg_id(),Client.getUser().getUser_id());
		if(users==null)
		{
			System.out.println("Oops!! No members found in your organisation. Try adding members to your organisation ");
			return null;
		}
		int checked_in=0,ind=1;
		ArrayList<User>final_users=new ArrayList<>();
		for(User user:users)
		{
			if(user.getTeam_id()==0)
			{
				if(checked_in==0)
				{
					System.out.println("The list of users available for adding to the team: ");
					checked_in=1;
				}
				System.out.println("  "+ind+".) User Name: "+user.getUser_name()+"  Role: "+MethodKeeper.getRoleAsString(user.getRole()));				
				final_users.add(user);
				ind++;
			}
		}
		if(final_users.size()==0)
		{
			System.out.println("No user found to add to the team. The users are part of another team!!");
			return null;
		}
		
		return final_users;
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
	
	
	public void transferSuperAdminOwnership()
	{
		
	while(true)
	{
		
		List<User>org_members=printOrgMembers();
		if(org_members==null||org_members.size()==0)
		{
			while(true)
			{
				
				int choice=MethodKeeper.receiveIntegerInput("1.)Go back");
				if(choice==1)
				{
					return;
				}
				else 
				{
					System.out.println("Invalid input!!");
					continue;
				}
			}
		}
		
		int choice=MethodKeeper.receiveIntegerInput("Select any of the user from the above available users from your organisation: ");
		if(org_members.size()+1==choice)
		{
			break;
		}
		if(choice>org_members.size()||choice<=0)
		{
			System.out.println("Invalid input!!");
			continue;
		}
		
		User selected_user=org_members.get(choice-1);
		choice=MethodKeeper.receiveIntegerInput("Sure you want to transfer the super-admin ownership with "+selected_user.getUser_name()+" Note:(Transfering ownership will remove you as super-admin and you will be assigned as an employee)"+"\n1.) Yes\n2.) No");
		
		if(choice==1)
		{
			System.out.println("The selected user "+selected_user.getUser_name()+" is now a super-admin (You are now an user) "+MethodKeeper.getLikeSymbol()+"\n");
			UserDAO user_dao=new UserDAO();
			boolean changed=user_dao.transferSuperAdminOwnership(Client.getUser().getUser_id(),selected_user.getUser_id(),Client.getUser().getOrg_id());
			if(changed)
			{
				Client.getUser().setRole(4);
				Client.mainMenu();
				break;
			}
		}
		else if(choice==2)
		{
			break;
		}
		else
		{
			System.out.println("Invalid input!!");
		}
		
	}
	
	}
}
