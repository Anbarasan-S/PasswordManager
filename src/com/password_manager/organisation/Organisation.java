package com.password_manager.organisation;

public class Organisation 
{
	private int org_id,status;
	private String org_name;
	public void setOrgId(int org_id)
	{
		this.org_id=org_id;
	}
	public void setStatusEnum(int status)
	{
		this.status=status;
	}
	public void setOrgName(String org_name)
	{
		this.org_name=org_name;
	}
	
	public int getOrgId()
	{
		return this.org_id;
	}
	public int getStatusEnum()
	{
		return this.status;
	}
	public String getOrgName()
	{
		return this.org_name;
	}
	
}
