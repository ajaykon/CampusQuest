package users;

import java.util.*;

public class Rolodex {
	ArrayList<User> users;
	
	public Rolodex(){
		this.users = new ArrayList<User>();
	}
	
	public boolean addUser(String username, String password){
		// Check if existing user
		User usernameCheck = this.findUser(username);
		if (usernameCheck != null) return false;
		
		User newUser = new User(username, password, 1000, 5000);
		this.users.add(newUser);
		return true;
	}
	
	public User findUser(String username){
		for (int i = 0; i < users.size(); i++){
			User check = users.get(i);
			if (check.getUsername().equalsIgnoreCase(username)){
				return check;
			}
		}
		System.out.println("Romeny serach faled");

		return null;
	}
	
	public boolean attemptLogin(String username, String password){
		User find = this.findUser(username);
		
		if (find == null){
			User newUser = new User(username, password, 1000, 5000);
			this.users.add(newUser);
			return true;
		} else {
			if (find.password.equalsIgnoreCase(password)){
				return true;
			} else return false;
		}
	}
}
