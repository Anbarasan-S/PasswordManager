package com.password_manager.main;

import java.util.Scanner;

import com.password_manager.employee.Employee;

public class Server 
{
	static boolean login=false;
	private static Employee emp=null;
	public static void main(String args[])
	{
		System.out.printf("Hi!! What Do You want to do\n");
		Scanner sc=new Scanner(System.in);
		int option;
		while(emp==null)
		{
			System.out.println("1.Sign Up 2.Login ");
			option=sc.nextInt();
			if(option==1)
			{
				Signup new_signup=new Signup();
				emp=new_signup.initialiser();
			}
			else if(option==2)
			{
				emp=new Login().verifyCredentials();
				System.out.println(emp);
			}
		}
		
		while(true&&emp.getRole()=="Super-admin")
		{
			System.out.println("1.Add user 2.Remove user 3.Exit");
			option=sc.nextInt();
			if(option==1)
			{
				emp.addEmployee(emp);
			}
			else if(option==2)
			{
				
			}
			else if(option==3)
			{
				System.exit(0);
			}
		}
		
		
		while(true&&emp.getRole()=="Employee")
		{
			
		}
	}
}
