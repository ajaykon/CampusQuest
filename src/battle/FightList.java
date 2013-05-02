package battle;

import java.util.*;

import users.Rolodex;
import users.User;

public class FightList {
	public ArrayList<Fight> fights;
	Rolodex rolodex;
	
	public FightList(Rolodex rolodex){
		this.fights = new ArrayList<Fight>();
		this.rolodex = rolodex;
	}
	
	public ArrayList<Fight> findFights(User user){
		ArrayList<Fight> answer = new ArrayList<Fight>();
		for (int i = 0; i < fights.size(); i++){
			
			System.out.println(fights.get(i).attacker.getUsername());
			System.out.println(fights.get(i).defender.getUsername());
			System.out.println(user.getUsername());
			if(fights.get(i).attacker.getUsername().equalsIgnoreCase(user.getUsername())){
				answer.add(fights.get(i));
				continue;
			}
			
			if(fights.get(i).defender.getUsername().equalsIgnoreCase(user.getUsername())){
				answer.add(fights.get(i));
				continue;
			}
		}
		return answer;
	}
	
	public Fight findFight(User user1, User user2){
		ArrayList<Fight> user1Fights = this.findFights(user1);
		Fight answer = null;
		
		for(int i = 0; i < user1Fights.size(); i++){
			Fight iter = user1Fights.get(i);
			if ((iter.attacker.equals(user2)) || (iter.defender.equals(user2))){
				answer = iter;
				break;
			}
		}
		
		return answer;
	}
	
	
	public void registerFight(String attacker,  String defender){
		Fight newFight = new Fight(attacker, defender, rolodex);
		
		System.out.println(newFight.attacker.getUsername());
		System.out.println(newFight.defender.getUsername());
		
		for (int i = 0; i < fights.size(); i++){
			if (fights.get(i).equivFight(newFight)) return;	// Do nothing
		}
		
		fights.add(newFight);
	}
	
}
