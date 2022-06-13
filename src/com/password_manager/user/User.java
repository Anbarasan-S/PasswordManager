package com.password_manager.user;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.password_manager.Password.Password;
import com.password_manager.dao.EmployeeDAO;
import com.password_manager.main.MethodKeeper;
import com.password_manager.main.Warner;

public class User 
{
	 private String user_name,master_password,private_key,public_key,team_name;
	 private PBEKeySpec pbe_key_spec=null;
	 private SecretKeyFactory secret_key_factor=null;
     private int pass_id[]=new int[1000],org_id,role,user_id,team_id;
	  public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	private EmployeeDAO emp_dao=null;
	  
	  public User(){emp_dao=new EmployeeDAO();}
	   
	   public User(String user_name,int role,String master_password)
	    {
	        this.user_name=user_name;
	        this.role=role;
			this.master_password=MethodKeeper.hashPassword(master_password,MethodKeeper.generateSalt());
	        emp_dao=new EmployeeDAO();
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
	   
	   public void removePassword(List<Password>lst_password,String row_nums)
	   {
		   String split_row_num[]=row_nums.split(" ");
		   List<String> temp_password=new ArrayList<>();
		  for(String row_num:split_row_num)
		  {
			  int ind=Integer.parseInt(row_num);
			  Password pass=lst_password.get(ind-1);
			  if(ind<=0||lst_password.size()<ind)
			  {
				  System.out.println("Invalid row number "+ind+" That row number was ignored");
			  }
			  
			  if(pass.getIs_own()==1)
			  {
				  boolean removed=emp_dao.removePassword(pass.getPass_id(),this.user_id);
				  if(removed)
				  {
					 temp_password.add(pass.getSite_name());
				  }
			  }
			  else
			  {
				  System.out.println("Users can only delete the password which are owned by them!");
			  }
		  }
		  if(temp_password.size()!=0)
		  {
			 System.out.println("The following passwords are removed successfully");
			 int index=1;
			 for(String removed_pass:temp_password)
			 {
				 System.out.println(index+".)"+" "+removed_pass);
				 index++;
			 }
		  }
	   }
	   
	  public void showPassword()
	  {
		  try
		  {
			 List<Password>lst_password=emp_dao.showPassword(this.user_id);
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
			 }
		  
		  }
		  catch(Exception ex)
		  {
			  System.out.println("Trouble getting the password "+ex.getMessage());
		  }
	  }
	  
	  public void editPassword(List<Password>lst_password,int row_num)
	  {
		  boolean is_changed_password=false;
		  if(row_num<=0||row_num>lst_password.size())
			{
				System.out.println("Invalid row number. Please enter a valid row number ");
				return ;
			}
		  Password curr_password=lst_password.get(row_num-1);
		  System.out.println("Name: "+curr_password.getSite_name()+" Press 1 to edit the password name or press any other number to leave as it is: ");
		  Scanner sc=new Scanner(System.in);
		  int opt=sc.nextInt();
		  String site_name,site_url,site_user_name,site_password = null;
		  
		  //Enter the name for the password
		  if(opt==1)
		  {
		  do
			{
				System.out.println("Enter the name for the password: ");
				sc.nextLine();
				site_name=sc.nextLine();			
				if(site_name.isEmpty())
				{
					System.out.println("The name for the password can't be left empty");
					continue;
				}
				if(site_name.equals(curr_password.getSite_name()))
				{
					break;
				}
			}while(emp_dao.isOccupiedName(site_name,user_id));
		  curr_password.setSite_name(site_name);
		  }
		
		  
		  //Enter the site url
		  System.out.println("Url: "+curr_password.getSite_url()+" Press 1 to edit the site url or press any other number to leave as it is: ");
			 opt=sc.nextInt();
			 if(opt==1)
			 {
			  do
				{
					System.out.println("Enter the site url ");
					sc.nextLine();
					site_url=sc.nextLine();			
				}while(site_url!=null&&!MethodKeeper.isValidUrl(site_url));
			  curr_password.setSite_url(site_url);
			 }
			
			 
		  //Edit the site username
		  System.out.println("Username: "+curr_password.getSite_user_name()+" Press 1 to edit the site username or press any other number to leave as it is: ");
		  
		  opt=sc.nextInt();
		  if(opt==1)
		  {
		  do
			{
				System.out.println("Enter the username for the site: ");
				sc.nextLine();
				site_user_name=sc.nextLine();
				if(site_user_name.length()==0)
				{
					Warner.requiredWarning("User Name");
				}
			}while(site_user_name.length()==0);
		  curr_password.setSite_user_name(site_user_name);
		  }
		  
		  
		  //Edit the site password
		  System.out.println("Password: "+curr_password.getSite_password()+" Press 1 to edit the site password or press any other number to leave as it is: ");
		  opt=sc.nextInt();
		  if(opt==1)
		  {
			  do
				{
					System.out.println("1. Enter the password for the site manually \n2. Automatically generate a strong password");
					int option=sc.nextInt();
					if(option==1)
					{
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
					is_changed_password=true;
					
				}while(site_password.length()<1);
			  curr_password.setSite_password(MethodKeeper.encrypt(site_password,"secret@123#245"));
			  curr_password.setLast_changed(new Timestamp(System.currentTimeMillis()));
		  }
		  emp_dao.editPassword(curr_password);
	  }
	  
	   public boolean addPassword()
	   {
		  Password password_data=MethodKeeper.receivePasswordDetails(this.user_id);
		  return emp_dao.addPassword(password_data,this);
	   }

	   
	    
	    protected void setOrgId(int org_id)
	    {
	    	this.org_id=org_id;
	    }
	    
	    public void addEmployee(User emp,String user_email_id) throws Exception
	    {
	        emp_dao.addInviteUser(user_email_id, emp);
	       
	    }
	    
		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getMaster_password() {
			return master_password;
		}

		public void setMaster_password(String master_password) {
			this.master_password = master_password;
		}

		public int getRole() {
			return role;
		}

		public void setRole(int role) {
			this.role = role;
		}

		public String getPrivate_key() {
			return private_key;
		}

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public void setPrivate_key(String private_key) {
			this.private_key = private_key;
		}

		public String getPublic_key() {
			return public_key;
		}

		public void setPublic_key(String public_key) {
			this.public_key = public_key;
		}

		public int[] getPass_id() {
			return pass_id;
		}

		public void setPass_id(int[] pass_id) {
			this.pass_id = pass_id;
		}

		public int getOrg_id() {
			return org_id;
		}

		public void setOrg_id(int org_id) {
			this.org_id = org_id;
		}
		
		public List<User> viewUsers() 
		{
			try
			{
			if(this.getRole()==5)
			{
				System.out.println("You are not allowed to access this menu as you are not a part of any organisation");
				return null;
			}
			ResultSet rs=emp_dao.viewUsers(this);
			List<User> usr_lst=new ArrayList<>();
			int ind=1;
			
			while(rs.next())
			{
				String user_name=rs.getString("email");
				int user_id=rs.getInt("user_id");
				int org_id=rs.getInt("org_id");
				String user_role=rs.getString("user_role");
				int team_id=rs.getInt("team_id");
				Date date=rs.getDate("created_at");
				User temp_user=new User();
				temp_user.setUser_name(user_name);
				temp_user.setOrg_id(org_id);
				temp_user.setUser_id(user_id);
				temp_user.setRole(MethodKeeper.getRole(user_role));
				temp_user.setTeam_id(team_id);
				System.out.print(ind+".) ");
				MethodKeeper.showUserDetails(temp_user);
				usr_lst.add(temp_user);
			}

			return usr_lst;
			}
			catch(Exception ex)
			{
				System.out.println("Exception in view users method of User class "+ex.toString());
				return null;
			}
		}
	


		public int getTeam_id() 
		{
			return team_id;
		}

		public void setTeam_id(int team_id) {
			this.team_id = team_id;
		}

		public String toString()
	    {
			String role[]= {"","Super-Admin","Admin","Team Admin","Employee","User"};
	        return "User Details:\n User name: "+this.user_name+"\n Role: "+role[this.role];
	    }

}
