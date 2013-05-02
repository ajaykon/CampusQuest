package users;

import java.util.*;

import world.Location;

public class User {
	public String username;
	public String password;
	public ArrayList<Location> ownedGrids;

	public int troops;
	public int gold;

	public boolean online;
	public Location position;
	
	public boolean attacked;
	
	public User(String username, String password, int troops, int gold){
		// New user
		this.username = new String(username);
		this.password = new String(password);
		this.troops = troops;
		this.gold = gold;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String newName){
		this.username = new String(newName);
	}
	
	public void setPosition(Location position) {
		this.position = position;
		position.addUser(this);
	}
	
	public Location getPosition(){
		return this.position;
	}
	
	public void setUserOnline(Location position){
		this.online = true;
		this.position = position;
	}
	
	public void setUserOffline(){
		this.online = true;
		this.position = null;
	}
	
	
}
