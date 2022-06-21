package com.password_maanger.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.password_manager.dao.TeamDAO;
import com.password_manager.dao.UserDAO;
import com.password_manager.main.Client;
import com.password_manager.main.MethodKeeper;
import com.password_manager.user.User;
import com.password_manager.user.UserOperationHandler;

public class TeamOperationHandler 
{
	public void createTeam()
	{
		TeamDAO team_dao=new TeamDAO();	
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the team name: ");
		String team_name=sc.nextLine().trim();
		
		while(team_dao.existsName(Client.getUser().getOrg_id(),team_name))
		{
			System.out.println("The team name already exists please enter a valid team name: ");
			team_name=sc.nextLine();
		}
		Team new_team=new Team();
		new_team.setTeam_name(team_name);
		new_team.setOrg_id(Client.getUser().getOrg_id());
		int team_id=team_dao.createTeam(new_team);
		if(team_id==-1)
		{
			System.out.println("Team is not created!!");
		}
		else
		{
			System.out.println("Team created successfully "+MethodKeeper.getLikeSymbol());
			while(true)
			{
				int opt=MethodKeeper.receiveIntegerInput("1.) Add team admin to the team " +MethodKeeper.printBlock(new_team.getTeam_name())+"\n2.) Add user to the team "+MethodKeeper.printBlock(new_team.getTeam_name())+"\n3.) Go back");
				if(opt==1)
				{
					addAsTeamAdmin(team_id,new_team.getTeam_name());
				}
				else if(opt==2)
				{
					addAsTeamMember(team_id,new_team.getTeam_name());
				}
				else if(opt==3)
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
	public void addAsTeamAdmin(int team_id,String team_name)
	{
		while(true)
		{
		TeamDAO team_dao;
		ArrayList<User>users=new UserOperationHandler().printOrgMembers(1);
		if(users==null)
		{
			break;
		}
		int choice=MethodKeeper.receiveIntegerInput("Select a user to add as team admin "+(users.size()+1)+".) Go back");
		if(choice==users.size()+1)
		{
			break;
		}
		if(choice<=0||choice>users.size())
		{
			System.out.println("Invalid input!!");
			continue;
		}
		else
		{
			team_dao=new TeamDAO();
			User user=users.get(choice-1);
		team_dao=new TeamDAO();
		boolean added=team_dao.addToTeam(1,user.getUser_id(),team_id);
		
		if(added)
		{
			System.out.println(user.getUser_name()+" has added to the team " +MethodKeeper.printBlock(team_name)+" as team admin "+MethodKeeper.getLikeSymbol());
		}
		else
		{
			System.out.println(user.getUser_name()+" can't be added to the team as team admin!! ");
		}
	}
	}
	}
	
	public void addAsTeamMember(int team_id,String team_name)
	{
		TeamDAO team_dao=new TeamDAO();
		while(true)
		{			
			ArrayList<User>users=new UserOperationHandler().printOrgMembers(1);
			if(users==null)
			{
				break;
			}
			int choice=MethodKeeper.receiveIntegerInput("Select a user to add as a team member: ");
			if(choice<=0||choice>users.size())
			{
				System.out.println("Invalid option try again!!");
				continue;
			}
			else
			{
				User user=users.get(choice-1);
				boolean added=team_dao.addToTeam(2,user.getUser_id(),team_id);
				if(added)
				{
					System.out.println(user.getUser_name()+" has added to the team "+MethodKeeper.printBlock(team_name)+"as team member "+MethodKeeper.getLikeSymbol());
				}
				else
				{
					System.out.println(user.getUser_name()+" can't be added to the team  as team member!! ");
				}
			}
		}
	}
	
	
	public void showTeam()
	{
		while(true)
		{
		List<Team>teams=showTeamOverview();
		if(teams==null)
			return;
		if(teams!=null)
		{
			int choice=MethodKeeper.receiveIntegerInput("Select any of the option: ");
			if(choice==teams.size()+1)
			{
				break;
			}
			if(choice>0&&choice<=teams.size())
			{
				Team team=teams.get(choice-1);
				System.out.println("What do you want do with the selected team "+MethodKeeper.printBlock(team.getTeam_name()));
				choice=MethodKeeper.receiveIntegerInput(" 1.) Add team admin to the team "+MethodKeeper.printBlock(team.getTeam_name())+
						"\n 2.) Add team member to the team "+MethodKeeper.printBlock(team.getTeam_name())+"\n 3.) View members in the team \n 4.) Go back");
		
				if(choice==1)
				{
					addAsTeamAdmin(team.getTeam_id(),team.getTeam_name());
				}
				else if(choice==2)
				{
					addAsTeamMember(team.getTeam_id(),team.getTeam_name());
				}
				else if(choice==3)
				{
					
				}
				else if(choice==4)
				{
					break;
				}
	     	}
			else
			{
				System.out.println("Invalid input!!");
			}
		}
		}
	}
	
	public ArrayList<Team>showTeamOverview()
	{
	TeamDAO team_dao=new TeamDAO();
	ArrayList<Team>teams=team_dao.showTeam(Client.getUser().getOrg_id());
	if(teams==null||teams.size()==0)
	{
		int choice;
		do
		{
			System.out.println("\nOops!! It looks like there are no teams in your organisation. Try adding teams to your organisation ");
			choice=MethodKeeper.receiveIntegerInput("1.)Go back");			
		}while(choice!=1);
		return null;
	}
	System.out.println("\nThe list of teams available in your organisation: ");
	int ind=1;
	for(Team team:teams)
	{
		System.out.println("  "+ind+".) Team name: "+team.getTeam_name());
		ind++;
	}
	System.out.println(ind+".) "+MethodKeeper.printBlock("Go back"));
	return teams;
	}
	
	public HashMap<String,String>showTeamMember()
	{
		UserDAO user_dao=new UserDAO();
		user_dao.getTeamMembers();
	}
}
