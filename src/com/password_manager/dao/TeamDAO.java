package com.password_manager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.password_maanger.team.Team;
import com.password_manager.user.User;

public class TeamDAO 
{
	private Connection con=null;
	private String query=null;
	private PreparedStatement ps=null;
	public TeamDAO()
	{
		con=ConnectionProvider.getInstance().getConnection();
	}
	
	
	public boolean existsName(int org_id,String name)
	{
		query="SELECT* FROM Team where org_id=? and team_name=?";
		try
		{
			ps=con.prepareStatement(query);
			ps.setInt(1, org_id);
			ps.setString(2, name);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				return true;
			}
			return false;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in existsname team dao "+ex.getMessage());
			return true;
		}
	}
	
	public int createTeam(Team new_team)
	{
		try
		{
			int team_id;
			ResultSet rs;
			con.setAutoCommit(false);
			query="INSERT INTO Team(team_name,org_id) VALUES(?,?)";
			ps=con.prepareStatement(query);
			ps.setString(1, new_team.getTeam_name());
			ps.setInt(2, new_team.getOrg_id());
			ps.executeUpdate();
			query="SELECT last_insert_id()";
			ps=con.prepareStatement(query);
			rs=ps.executeQuery();
			rs.next();
			team_id=rs.getInt("last_insert_id()");
			con.commit();
			con.setAutoCommit(true);
			return team_id;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in create team method of team dao "+ex.getMessage());
			return -1;
		}
		
	}
	
	public boolean addToTeam(int role,int user_id,int team_id)
	{
		try
		{
			con.setAutoCommit(false);

			if(role==1)
			{
				query="UPDATE User set is_team_admin=1,team_id=? where user_id=?";
				ps=con.prepareStatement(query);
				ps.setInt(1, team_id);
				ps.setInt(2, user_id);
				ps.executeUpdate();
			}	
			else if(role==2)
			{
				query="UPDATE User set team_id=? where user_id=?";
				ps=con.prepareStatement(query);
				ps.setInt(1, team_id);
				ps.setInt(2, user_id);
				ps.executeUpdate();
			}
			con.commit();
			con.setAutoCommit(true);
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in add to team method of team dao "+ex.getMessage());
			return false;
		}
	}
	
	public ArrayList<Team> showTeam(int org_id)
	{
		query="SELECT* FROM Team where org_id=?";
		try
		{
			ps=con.prepareStatement(query);			
		    ps.setInt(1, org_id);
		    ResultSet rs=ps.executeQuery();
		    ArrayList<Team>teams=new ArrayList<>();
		    while(rs.next())
		    {
		    	Team new_team=new Team();
		    	new_team.setOrg_id(org_id);
		    	new_team.setTeam_name(rs.getString("team_name"));
		    	new_team.setTeam_id(rs.getInt("team_id"));
		    	teams.add(new_team);
		    }
		    return teams;
		}
		catch(Exception ex)
		{
			System.out.println("The exception in show team method "+ex.getMessage());
			return null;
		}
	}
	
	
	public boolean removeUserFromTeam(int user_id)
	{
		query="UPDATE User set team_id=? where user_id=?";
		try
		{
			ps=con.prepareStatement(query);
			ps.setNull(1,Types.NULL);
			ps.setInt(2, user_id);
			ps.executeUpdate();
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in remove user from team method "+ex.getMessage());
			return false;
		}
	}
	
	

}
