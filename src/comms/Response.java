package comms;

import java.util.*;

public class Response {
	int responseType;					// All response types
	
	int clockTick;
	/*
	 * Type 0 response: Status message.
	 * 
	 * Requires username.
	 * 
	 * Answers back gold, troops, and attacks. Also acts as a login message.
	 */
	public int userTroops;
	public int userGold;
	public boolean attacked;					// Whether player has been attacked
	public ArrayList<String> attackers;		// List of attackers.
	
	/*
	 * Type 1 response: Location message.
	 * 
	 * Requires username and location.
	 * 
	 * Tell user the grid (row,column) pair for a given (latitude, longitude). Registers user as 
	 * present in their identified grid.
	 */
	
	public int gridRow, gridColumn;			// Response type 1. Location answer.
	
	/*
	 * Type 2 response: Show players present.
	 * 
	 * Requires username, (location taken from previous message).
	 * 
	 * Tells user all other users in the given location.
	 */
	public String currentOwner;
	public ArrayList<String> playersPresent;	// Player names of players present at current location 
	public ArrayList<Integer> troopCounts;		// Troop counts of players present at current location.
												// Index matched with playersPresent
	/*
	 * Type 3 response: Assign ownership of map square.
	 * 
	 * Requires username, intent to acquire.
	 * 
	 * Sends true if square acquired, false otherwise.
	 */
	
	public boolean ownershipAcknowledgement;	// true if user owns grid square he is present in.
	
	/*
	 * Type 4 response: Acknowledge that a fight request has been registered.
	 * 
	 * Requires username, username of target.
	 * 
	 * Sends true if request registered. (Should be always)
	 */
	
	public boolean fightAcknowledgment;		// true to acknowledge that a fight request has been registered.
	
	/*
	 * Type 5 response: Calculate the results of a round of battle, and tell user if he or she was victorious
	 * 
	 * Requires both usernames and troop commitments.
	 * 
	 * Sends true if request user won.
	 */
	
	public boolean battleAcknowledgment;				// true if user has won a battle (instant victory or defeat acknowledgment)
	
	public Response(int responseType, int clockTick) {
		this.responseType = responseType;
		this.clockTick = clockTick;
	}
	
}