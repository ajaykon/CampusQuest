package comms;

/*import java.util.*;

public class Message {
	public int requestType;
	
	/*
	 * request type list:
	 * 
	 * 1 - Username - return personal data.
	 * 2 - Given location and usernames, get ID's of players present and owner
	 * 3 - Given target username, get data of user (troops).
	 * 4 - Attack directive - given target username and number of troops, execute an attack.
	 * 5 - Status - Tell me how my troops are. (same as 2?)
	 */
	
/*	public String username;		// All request types
	public double latitude, longitude;	
	public ArrayList<String> targetUsernames;	// Types 3, 4
	public ArrayList<Integer> numAttackingTroops;	// type 4
	
}*/

import java.util.*;

public class Message {
	public int requestType;
	
	/*
	 * request type list:
	 * 
	 * 0 - "Status" - Given username, return personal gold, troops, and attacks occuring upon you
	 * 1 - "Location Data" - Give username , lat, lng, get back Grid coordinate
	 * 2 - "Surrounding Players" - Given a username, and Grid coordinates, get back a list of players and their number of troops. 
	 * 		Note: This should return an ArrayList (HashMap if you are so inclined) of Player objects. 
	 * 3 - "Claim" - Given a username, and grid coordinates which are being claimed, claim the spot.
	 * 4 - "Attack" - Given a username, grid coordinates, a number of troops to attack with, and an enemy player userName to attack,
	 * 		do battle with this foe! :-) -or... Maybe not?
	 * 
	 */
	
	public String username;		// All request types
	public double latitude, longitude;
	public String currentCoords;
	public String targetUsername;	// Types 3, 4
	public int numAttackingTroops;	// type 4
		
	//Request type 0
	public Message(int request, String username){
		this.requestType = request;
		this.username = username;
		this.latitude = Double.MAX_VALUE;
		this.longitude = Double.MAX_VALUE;
		this.currentCoords = "0,0";
		this.numAttackingTroops = Integer.MAX_VALUE;
	}
	
	//Request type 1
	public Message(int request, String username, double lat, double lng){
		this.requestType = request;
		this.username = username;
		this.latitude = lat;
		this.longitude = lng;
		this.currentCoords = "0,0";
		this.numAttackingTroops = Integer.MAX_VALUE;
	}
	
	//Request type 2 & 3
	public Message(int request, String username, String coords){
		this.requestType = request;
		this.username = username;
		this.latitude = Double.MAX_VALUE;
		this.longitude = Double.MAX_VALUE;
		this.currentCoords = coords;
		this.numAttackingTroops = Integer.MAX_VALUE;
	}
	
	
	
	
	
}