import java.util.*;
import java.util.concurrent.Semaphore;

import comms.Message;
import comms.MessageHandler;
import comms.Response;

import users.Rolodex;
import users.User;
import world.Location;
import world.World;
import battle.Fight;
import battle.FightList;

public class CampusQuest {
	
	public static final int updateInterval = 60; // Update interval in seconds
		
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("Welcome to CampusQuest!");
		
		World world;
		Rolodex rolodex;
		FightList fightList;
		
		Integer clockTick = new Integer(0);
		
		world = new World(40.527305, -74.470997, 40.518106, -74.459925);
		rolodex = new Rolodex();
		fightList = new FightList(rolodex);
		
		Semaphore messagesSem = new Semaphore(0);
		Semaphore responseSem = new Semaphore(0);
		
		MessageHandler messageHandler = new MessageHandler(messagesSem, responseSem);
		new Thread(messageHandler).start();
		
		messagesSem.acquire();
		responseSem.acquire();
		
		LinkedList<Message> messages = messageHandler.messages;
		Hashtable<Message, Response> responseTable = messageHandler.responseTable;
		
		Clock regularTasks = new Clock( world,
				 rolodex,
				 fightList,
				 messagesSem,
				 responseSem,
				 clockTick);
		
		Date currentTime = new Date();
		Timer clockTasks = new Timer();
		clockTasks.scheduleAtFixedRate(regularTasks, currentTime, updateInterval*1000);
		
		responseSem.release();
		messagesSem.release();
		
		while(true){
			// Check first if there are messages.
			messagesSem.acquire();
			if (!messages.isEmpty()){
				Message message = messages.remove();
				
				responseSem.acquire();
				
				Response response = handleMessage(message, world, rolodex, fightList, clockTick);
				responseTable.put(message, response);
				
				responseSem.release();
				messagesSem.release();

			} else {
				messagesSem.release();
				Thread.sleep(500);
			}
		}
	}
	
	public static Response handleMessage(Message message,
										World world,
										Rolodex rolodex,
										FightList fightList,
										Integer clockTick){
		Response response;
		
		if (message.requestType == 0){
			// send back status message
			response = new Response(0, clockTick);
			String username = message.username;
			
			// Check if user exists
			if (rolodex.attemptLogin(username, "")){
				// Successful login.
			} else {
				// Should be unreachable
				System.out.println("Login failure?");
				return null;
			}
			
			// Find user.
			User user = rolodex.findUser(username);
			
			// Find fights
			ArrayList<Fight> fights = fightList.findFights(user);
			ArrayList<String> opponents = new ArrayList<String>();

			for (int i = 0; i < fights.size(); i++){
				Fight iter = fights.get(i);
				if (iter.attacker.equals(user)){
					opponents.add(new String(iter.defender.username));
				} else 
					opponents.add(new String(iter.attacker.username));

			}
			// Set response fields
			
			response.userTroops = user.troops;
			response.userGold = user.gold;
			if (fights.size() <= 0) response.attacked = false;
			else {
				response.attacked = true;
				response.attackers = opponents;
			}
			
			return response;
		} else if (message.requestType == 1) {
			response = new Response(1, clockTick);
			
			String username = message.username;
			
			// Get latitude and longitude
			double userLatitude = message.latitude;
			double userLongitude = message.longitude;
			
			System.out.println("location :"+userLatitude+" "+userLongitude);
			
			User user = rolodex.findUser(username);
			
			System.out.println("found user");
			
			Location location = world.getCoordinateLocation(userLatitude, userLongitude);

			System.out.println("found location");
			
			response.gridRow = location.row;
			response.gridColumn = location.column;
			
			// Register user as present in this location
			
			user.setPosition(location);
			
			return response;
		} else if (message.requestType == 2) {
			response = new Response(2, clockTick);
			
			String username = message.username;
			User user = rolodex.findUser(username);
			Location location = user.getPosition();
			
			User currentOwner = location.currentOwner;
			
			String owner;
			
			if (currentOwner!=null){
				owner = new String(currentOwner.username);
			} else {
				owner = null;
			}
			
			ArrayList<String> usernames = new ArrayList<String>();
			ArrayList<Integer> troops = new ArrayList<Integer>();
			
			for (int i = 0; i < location.present.size(); i++) {
				User check = location.present.get(i);
				if (!check.username.equalsIgnoreCase(username)){
					usernames.add(new String(check.username));
					troops.add(check.troops);
				}
			}
			
			response.currentOwner = owner;
			response.playersPresent = usernames;
			response.troopCounts = troops;
			
			return response;
		} else if (message.requestType == 3) {
			response = new Response(3, clockTick);
			
			String username = message.username;
			User user = rolodex.findUser(username);
			Location location = user.getPosition();
			
			User currentOwner = location.currentOwner;
			
	//		if (currentOwner == null) {
				response.ownershipAcknowledgement = true;
				location.currentOwner = user;
	//		} else {
	//			response.ownershipAcknowledgement = false;
	//		}
			
			return response;
		} else if (message.requestType == 4) {
			// register a new fight
			response = new Response(4, clockTick);
			
			String username = message.username;
			String targetName = message.targetUsername;
			
			System.out.println("Fight:- Attacker :"+username);
			System.out.println("Defender: "+targetName);
			fightList.registerFight(username, targetName);
			
			response.fightAcknowledgment = true;
			
			return response;
		} else if (message.requestType == 5) {
			response = new Response(5, clockTick);
			// Find fight
			String username = message.username;
			User user = rolodex.findUser(username);

			String opponentName = message.targetUsername;
			int troopNumbers = message.numAttackingTroops;

			User opponent = rolodex.findUser(opponentName);
			Fight fight = fightList.findFight(user, opponent);
			fight.setTroopCommittment(user, troopNumbers);

			response.battleAcknowledgment = true;
			
			return response;
		}
		return null;
	}
}
