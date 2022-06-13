package com.password_manager.main;

import java.util.Base64;
import java.util.Scanner;

import com.password_manager.Password.Password;
import com.password_manager.dao.EmployeeDAO;
import com.password_manager.email.EmailServer;
import com.password_manager.organisation.Organisation;
import com.password_manager.user.User;


public class Signup 
{

    private EmployeeDAO empDao=null;
    private User new_user=null;
    
    public Signup()
    {
        empDao=new EmployeeDAO();
    }

   public User initialiser()
   {
        receiveCredentials();
        return new_user;
    }
public void sendOtp(String user_name)
{
	int checker=2;
	 int otp=1000+(int)(Math.random()*((9999-1000)+1)),verify_otp;
     try
     {
     EmailServer.sendMail(user_name, "Password Manager Verification", "OTP: "+otp+"");
     }
     catch(Exception ex)
     {
     	System.out.println(ex.getMessage());
     }
     Scanner sc=new Scanner(System.in);
     while(checker-->0) 
     {
		 System.out.println("Enter the otp sent to your email address: ");
		 verify_otp=sc.nextInt();
		 if(verify_otp!=otp)
		 {
			continue;
		 }
		 break;
     }
    
    if(checker==0)
    {
    	System.out.println("Too many unsuccessful attempts");
    	receiveCredentials();
    }
}

   public  void receiveCredentials()
    {
	   System.out.println("1.Sign up for Organisation\n2.Sign up as an employee of an organisation\n3.Sign up as an individual\n4.Go to main-menu");
        Scanner sc=new Scanner(System.in);
        String user_name,master_password,verify_password;
        int inp=sc.nextInt();

        if(inp==1)
        {
            System.out.println("Enter the username: ");
            user_name=sc.next();
           if(!MethodKeeper.isValidEmail(user_name))
            {
            	receiveCredentials();
            }
            System.out.println("Enter your master password (Note - The master password must atleast contains 10 characters \"\n"
            		+ "	 and should contain one special character one lower case character one upper case character and one digit :) ");
            master_password=sc.next();
            while(Warner.warnPassword(master_password))
            {
            	System.out.println("Choose an option: 1. Enter your master password again 2. Return to main menu");
            	int opt=sc.nextInt();
            	if(opt==1)
            	{
            		System.out.println("Enter your master password: ");
            		master_password=sc.next();
                    System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();
            	}
            	else if(opt==2)
            	{
            		//Show main menu
            		return;
            	}
            }
            System.out.println("Re-Enter your master password: ");
            verify_password=sc.next();
            while(!verify_password.equals(master_password))
            {
            	Warner.notMatch();
            	int opt=sc.nextInt();
            	if(opt==1)
            	{
            		System.out.println("Enter your master password: ");
            		master_password=sc.next();
                    System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();
            	}
            	else if(opt==2)
            	{
            		System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();	
            	}
            	else if(opt==3)
            	{
            		//Show main menu
            		return;
            	}
            }
            int checker=2;
            if(empDao.userExists(user_name))
            {
           	 System.out.println("User already exists");
           	 return;
            }
           
            sendOtp(user_name);

            System.out.println("Enter your organisation name: ");
            String org_name=sc.next();
            createOrgWithUser(user_name,master_password,org_name);
        }
        else if(inp==2)
        {
            System.out.println("Enter your username: ");
            sc.nextLine();
            user_name=sc.nextLine();
            System.out.println("Paste the Invite token sent to your email address");
            int org_id;
            try
            {
            	do 
            	{
            		String token=sc.next();
            		String decode_token=new String(Base64.getDecoder().decode(token.getBytes()));
            		String split_arr[]=decode_token.split(":");
            
            	org_id=Integer.parseInt(split_arr[1]);
               if(!empDao.verifySecretToken(user_name, token,org_id))
                {	
                    System.out.println("Oops! It's a Invalid invite token");
                    System.out.println("1.Paste the token again 2.Go to main-menu");
                    int opt=sc.nextInt();
                    
                    if(opt==1)
                    {
                    	continue;
                    }
                    else if(opt==2)
                    {
                    	return;
                    }
                }	
               else 
               {
            	   break;            	   
               }
            	}while(true);
            	System.out.println("Verification successful");
            }
            catch(Exception ex)
            {
            	System.out.println("Oops! It's a Invalid invite token");
            	return;
            }
            
            System.out.println("Enter your master password (Note - The master password must atleast contains 10 characters \"\n"
            		+ "	 and should contain one special character one lower case character one upper case character and one digit :) ");
            master_password=sc.next();
            while(Warner.warnPassword(master_password))
            {
            	System.out.println("Choose an option: 1. Enter your master password again 2. Return to main menu");
            	int opt=sc.nextInt();
            	if(opt==1)
            	{
            		System.out.println("Enter your master password: ");
            		master_password=sc.next();
                    System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();
            	}
            	else if(opt==2)
            	{
            		//Show main menu
            		return;
            	}
            }
            System.out.println("Re-Enter your master password: ");
            verify_password=sc.next();
            while(!verify_password.equals(master_password))
            {
            	Warner.notMatch();
            	int opt=sc.nextInt();
            	if(opt==1)
            	{
            		System.out.println("Enter your master password: ");
            		master_password=sc.next();
                    System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();
            	}
            	else if(opt==2)
            	{
            		System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();	
            	}
            	else if(opt==3)
            	{
            		//Show main menu
            		return;
            	}
            }
            new_user=new User(user_name,4,master_password);
            new_user.setOrg_id(org_id);
           
            boolean created=empDao.createEmployee(new_user);
            if(!created)
            {
            	new_user=null;
            }
        }
        else if(inp==3)
        {
        	//Create individual user
        	
        	System.out.println("Enter the username: ");
            user_name=sc.next();
           if(!MethodKeeper.isValidEmail(user_name))
            {
            	receiveCredentials();
            }
            System.out.println("Enter your master password: ");
            master_password=sc.next();
            System.out.println("Re-Enter your master password: ");
            verify_password=sc.next();
            while(!verify_password.equals(master_password))
            {
            	Warner.notMatch();
            	int opt=sc.nextInt();
            	if(opt==1)
            	{
            		System.out.println("Enter your master password: ");
            		master_password=sc.next();
                    System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();
            	}
            	else if(opt==2)
            	{
            		System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();	
            	}
            	else if(opt==3)
            	{
            		//Show main menu
            		return;
            	}
        }
            
            while(Warner.warnPassword(master_password))
            {
            	int opt=sc.nextInt();
            	if(opt==1)
            	{
            		System.out.println("Enter your master password: ");
            		master_password=sc.next();
                    System.out.println("Re-Enter your master password: ");
                    verify_password=sc.next();
            	}
            	else if(opt==2)
            	{
            		//Show main menu
            		return;
            	}
            }	
            sendOtp(user_name);
            new_user=new User(user_name,5,master_password);  
            empDao.createUser(new_user);
            
        }    
        else if(inp==4)
        {
        	return;
        }
   }

   
   private void createOrgWithUser(String user_name,String master_password,String org_name)
   {
	   Organisation org=new Organisation();
   	   org.setOrgName(org_name);
       new_user=new User(user_name,1,master_password);
       EmployeeDAO.createOrgWithUser(org,new_user);
   }
}
