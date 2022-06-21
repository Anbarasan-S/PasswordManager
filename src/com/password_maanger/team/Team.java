package com.password_maanger.team;

public class Team 
{
	private int team_id,org_id;
	private String team_name;
	
	
	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}
	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
	
	
	public int getTeam_id() {
		return team_id;
	}
	public int getOrg_id() {
		return org_id;
	}
	public String getTeam_name() {
		return team_name;
	}
		
}
