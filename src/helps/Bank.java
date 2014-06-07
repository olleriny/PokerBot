package helps;


import java.util.ArrayList;
import java.util.Collections;

import bots.Bot;

import utils.ListOfPlayers;
import utils.Value;

public class Bank {

	private ListOfPlayers bots;
	private double chips = 0;
	
	public Bank(ListOfPlayers bots){
		this.bots = bots;
	}
	
	public void addChips(double chips){
		this.chips += chips;
	}
	
	public double getChips(){
		return chips;
	}
	
	public String splitAll(){
		String result = "Bank = " + chips + ", winners: ";
		double min, temp;
		double split = 0;
		int score, count = 0;
		double res = 0;
		ArrayList<Bot> winners = new ArrayList<>(bots.getActivePlayers());
		Collections.sort(winners, Value.handComparator);
		for (Bot b: winners){
			if (chips == 0)
				return result;
			count = 0;
			min = b.getTotalStake();
			score = b.getScore();
			for (Bot others: bots.getAllPlayers()){
				if (others.getScore() == score){
					count += 1;
				}
				temp = Math.min(min, others.getTotalStake());
				others.takeFromTotalStake(temp);
				split += temp;
			}
//			if (count > 1) try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			if (count == 0){
				System.out.println("something is wrong with bank split...");
				System.out.println(chips);
			} else {
				res = (int) split/count;
				chips -= res;
				b.winsChips(res);
				split -= res;
				result += ", " + b.getName() + b.getID() + " - " + res + " (" + Value.getScoreToString(b.getScore()) + ")";
				b.setScore(0);
			}
		}
		return result;
	}
}
