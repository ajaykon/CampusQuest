package world;

import java.util.*;

import users.User;

public class Location {
	public int row, column;
	public ArrayList<User> present;
	public User currentOwner;
	
	public Location(int row, int column){
		this.row = row;
		this.column = column;
		this.present = new ArrayList<User>();
	}
	
	public void addUser(User user){
		if (findUserInList(this.present, user.getUsername())== null){
			present.add(user);
		}
	}
	
	public static User findUserInList (ArrayList<User> list, String username){
		for (int i = 0; i < list.size(); i++){
			User check = list.get(i);
			if (check.getUsername().equalsIgnoreCase(username)){
				return check;
			}
		}

		return null;
	}
}
