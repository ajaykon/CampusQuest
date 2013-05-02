import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import users.Rolodex;
import users.User;
import world.Location;
import world.World;
import battle.Fight;
import battle.FightList;

public class Clock extends TimerTask{
	World world;
	Rolodex rolodex;
	FightList fightList;
	
	Semaphore messagesSem;
	Semaphore responseSem;
	
	Integer clockTick;
	
	public static final int territorialReward = 500;

	public Clock (	World world,
					Rolodex rolodex,
					FightList fightList,
					Semaphore messagesSem,
					Semaphore responseSem,
					Integer clockTick){
		this.world = world;
		this.rolodex = rolodex;
		this.fightList = fightList;
		
		this.messagesSem = messagesSem;
		this.responseSem = responseSem;
		this.clockTick = clockTick;
	}

	@Override
	public void run() {
		// Clock operations
		try {
			messagesSem.acquire();
			responseSem.acquire();
			
			System.out.println("clocking");
			
			// Go through every location, award owner 500 gold
			
			for (int i = 0; i < World.mapDimension; i++){
				for (int j = 0; j < World.mapDimension; j++){
					Location loc = world.grid[i][j];
					User owner = loc.currentOwner;
					if (owner != null)
						owner.gold = owner.gold + territorialReward;
				}
			}
			
			// Settle all fights.
			
			ArrayList<Fight> fights = fightList.fights;
			
			for (int i = 0; i < fights.size(); i++){
				Fight iter = fights.get(i);
				
				User attacker = iter.attacker;
				User defender = iter.defender;

				int lostTroops = Math.min(iter.attackerTroopCommittment, iter.defenderTroopCommittment);
				
//				System.out.println("lost troops : "+lostTroops);
								
				attacker.troops = attacker.troops - lostTroops;
				defender.troops = defender.troops - lostTroops;
				
				if (iter.attackerTroopCommittment > iter.defenderTroopCommittment) {
					defender.troops = defender.troops - (iter.attackerTroopCommittment - iter.defenderTroopCommittment);
				} else {
					attacker.troops = attacker.troops - (iter.defenderTroopCommittment - iter.attackerTroopCommittment);
				}
				
//				System.out.println(attacker.troops);
//				System.out.println(defender.troops);

				iter.attackerTroopCommittment = 0;
				iter.defenderTroopCommittment = 0;
			}
			
			clockTick++;
			
			responseSem.release();
			messagesSem.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("Problem in the clockwork");
			e.printStackTrace();
		}
	}

}
