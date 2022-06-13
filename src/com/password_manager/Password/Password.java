package com.password_manager.Password;

import java.sql.Date;

public class Password 
{
	private int pass_id,user_id,changed_by_id,is_own,owner_pass_id;
	private String site_name,site_url,site_password,site_user_name;
	private Date created_at,last_changed;
	public void setPass_id(int pass_id) {
		this.pass_id = pass_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public void setChanged_by_id(int changed_by_id) {
		this.changed_by_id = changed_by_id;
	}
	public void setIs_own(int is_own) {
		this.is_own = is_own;
	}
	public void setOwner_pass_id(int owner_pass_id) {
		this.owner_pass_id = owner_pass_id;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public void setSite_url(String site_url) {
		this.site_url = site_url;
	}
	public void setSite_password(String site_password) {
		this.site_password = site_password;
	}
	public void setSite_user_name(String site_user_name) {
		this.site_user_name = site_user_name;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public void setLast_changed(Date last_changed) {
		this.last_changed = last_changed;
	}
	
	
	
	public int getPass_id() {
		return pass_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public int getChanged_by_id() {
		return changed_by_id;
	}
	public int getIs_own() {
		return is_own;
	}
	public int getOwner_pass_id() {
		return owner_pass_id;
	}
	public String getSite_name() {
		return site_name;
	}
	public String getSite_url() {
		return site_url;
	}
	public String getSite_password() {
		return this.site_password;
	}
	public String getSite_user_name() {
		return site_user_name;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public Date getLast_changed() {
		return last_changed;
	}
	
}
