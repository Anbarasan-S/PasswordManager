package com.password_manager.main;

import java.util.Scanner;

import com.password_manager.employee.Employee;
import com.password_manager.dao.EmployeeDAO;


public class Signup 
{

    private EmployeeDAO empDao=null;
    private int caught_org_id;
    private String added_by;
    private Employee new_emp=null;
    public Signup()
    {
        empDao=new EmployeeDAO();
    }

   public Employee initialiser()
    {
        System.out.println("1.Sign up for Organisation\n2.Sign up as an employee of an organisation\n");
        receiveCredentials();
        return new_emp;
    }


   public  void receiveCredentials()
    {
        Scanner sc=new Scanner(System.in);
        String user_name,master_password;
        int inp=sc.nextInt();
        boolean success=false;
        if(inp==1)
        {
            System.out.println("Enter the username: ");
            sc.nextLine();
            user_name=sc.nextLine();
            System.out.println("Enter your master password: ");
            master_password=sc.next();
            if(master_password.length()<10)
            {
                System.out.println("Sorry we can't sign you up, the master_password length must be greater than or equal to 10");
                return;
            }
            setData(user_name, master_password,"Super-admin");
        }
        else if(inp==2)
        {
            System.out.println("Enter your username: ");
            sc.nextLine();
            user_name=sc.nextLine();
            System.out.println("Enter the secret key provided by our team: ");
            String secret_token=sc.nextLine();
            System.out.println("Enter your organisation id");
            int org_id=sc.nextInt();
            String catch_data[]=new String[2];
            if(!empDao.verifySecretToken(user_name, secret_token,catch_data,org_id))
            {
                System.out.println("Oops! the secret key doesn't match");
                return;
            }
            System.out.println("Enter your master password: ");
            master_password=sc.next();
            if(master_password.length()<10)
            {
                System.out.println("Sorry we can't sign you up, the master_password length must be greater than or equal to 10");
            } 
            caught_org_id=Integer.parseInt(catch_data[0]);
            added_by=catch_data[1];
           setData(user_name, master_password,"Employee");
        }
    }    

  private void setData(String user_name,String master_password,String role)
    {
             new_emp=null;

             if(empDao.userExists(user_name))
             {
            	 System.out.println("User already exists");
            	 return;
             }
            if(role=="Super-admin")
            {
                 new_emp=new Employee(user_name,role,master_password);
            }        
            else if(role=="Employee")
            {
                new_emp=new Employee(user_name, role, master_password);
                new_emp.setOrg_id(caught_org_id);
            }

            EmployeeDAO empDao=new EmployeeDAO();
            
            boolean created=empDao.createEmployee(new_emp,added_by);
            if(!created)
            {
                System.out.println("Sorry we couldn't sign you up");
                return;
            }
                System.out.println("Your account created successfully");
                System.out.println(new_emp);
    }
}
