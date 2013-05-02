package battle;
import users.Rolodex;
import users.User;

public class Fight {	
	public User attacker;
	public User defender;
	
	public int attackerTroopCommittment;
	public int defenderTroopCommittment;
	
	public Fight(User attacker, User defender){
		this.attacker = attacker;
		this.defender = defender;
	}
	
	public Fight(String attackerUsername, String defenderUsername, Rolodex rolodex) {
		User attacker = rolodex.findUser(attackerUsername);
		User defender = rolodex.findUser(defenderUsername);
		
		System.out.println(attackerUsername+defenderUsername);
		System.out.println(attacker.getUsername()+defender.getUsername());
		
		if ((attacker == null) || (defender == null)){
			throw new NullPointerException("Error setting up fight. Users not found.");
		}
		this.attacker = attacker;
		this.defender = defender;		
	}
	
	public boolean equivFight(Fight input){
		if ((attacker == input.attacker) && (defender == input.defender)) return true;
		else return false;
	}
	
	public void setTroopCommittment(User user, int troopCommittment){
		if (user == attacker){
			attackerTroopCommittment = troopCommittment;
		} else if (user == defender) {
			defenderTroopCommittment = troopCommittment;
		}
	}
}
