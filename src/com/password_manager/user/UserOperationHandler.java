package com.password_manager.user;

import java.util.ArrayList;
import java.util.List;

import com.password_manager.dao.UserDAO;

public class UserOperationHandler 
{
	
	private UserDAO emp_dao=new UserDAO();
	
	public void addEmployee(User emp,String user_email_id) throws Exception
    {
        emp_dao.addInviteUser(user_email_id, emp);
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
