package com.password_manager.user;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.password_manager.Password.Password;
import com.password_manager.dao.EmployeeDAO;
import com.password_manager.main.MethodKeeper;

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
			   if(row_num<0||row_num>size)
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
					   System.out.println("Cannot remvoe the  user with the username "+temp_user.getUser_name());					   
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
	   
	  public void showPassword()
	  {
		  try
		  {
			 List<Password>lst_password=emp_dao.showPassword(this.user_id);
			 int ind=1;   
			 for(Password pass:lst_password)
			 {
				 if(pass.getSite_url().length()==0)
				 {
					 pass.setSite_url("-");
				 }
				 System.out.println(ind+".) Name: "+pass.getSite_name()+" Password: "+pass.getSite_password()+" Url: "+pass.getSite_url())
			 }
		  }
		  catch(Exception ex)
		  {
			  System.out.println("Trouble getting the password "+ex);
		  }
	  }
	  
	   public boolean addPassword()
	   {
		  Password password_data=MethodKeeper.receivePasswordDetails();
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
