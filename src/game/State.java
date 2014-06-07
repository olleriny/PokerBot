package game;

import java.util.ArrayList;

import bots.Bot;
import utils.ListOfPlayers;
import utils.Value.*;

public class State implements Cloneable {

	public int round, dealerID;
	public int smallBlind, bigBlind;
	public double bank;
	public Integer[] deck;
	public ArrayList<Round> rounds = new ArrayList<>();
	public ArrayList<Player> players = new ArrayList<>();
	
	class Action {
		public double amount;
		public int botID;
		public state state;
		
		public Action(double amount, int botID, state state){
			this.amount = amount;
			this.botID = botID;
			this.state = state;
		}
	}
	
	class Round {
		public ArrayList<Action> actions = new ArrayList<>();		
		public int round;
		
		public Round(int round){
			this.round = round;
		}
		
		public void addAction(Action a){
			actions.add(a);
		}
	}
	
	class Player {
		public int id;
		public double stack;
		public double inGame;
		public state state;
		
		public Player(int id, double stack, double inGame){
			this.id = id;
			this.stack = stack;
			this.inGame = inGame;
		}
		
		public void setChips(double stack, double inGame){
			this.stack = stack;
			this.inGame = inGame;
		}
	}
	
	public State(int dealerID, int smallBlind, ListOfPlayers pls){
		this.round = 0;
		this.smallBlind = smallBlind;
		this.bigBlind = smallBlind * 2;
		this.dealerID = dealerID;
		for (Bot b: pls.getActivePlayers())
			players.add(new Player(b.getID(), b.getChips(), b.getTotalStake()));
	}
	
	public void setBank(double bank){
		this.bank = bank;
	}
	
	public void setRound(int round, Integer[] cards){
		this.round = round;
		this.rounds.add(new Round(round));
		this.deck = cards;
	}
	
	public void addAction(int botID, state state, double amount){
		rounds.get(round).addAction(new Action(amount, botID, state));
		Player p = getPlayer(botID);
		if (p != null) {
			p.setChips(p.stack - amount, p.inGame + amount);
			p.state = state;
		}
	}
	
	private Player getPlayer(int ID){
		for (Player p: players)
			if (p.id == ID)
				return p;
		return null;
	}
	
	public String toString(){
		String res = "dealerID = " + dealerID + ", bank = " + bank + ", smallBlind = " + smallBlind + ", bigBlind = " + bigBlind + "\nplayers: ";
		for (Player p: players){
			res += "player " + p.id + " (" + p.stack + ", " + p.inGame + ", " + p.state + "), ";
		}
		res += "\ncards: ";
		for (Integer c: deck){
			res += c + ", ";
		}
		res += "\n";
		for (Round r: rounds){
			res += "round " + r.round + " - actions:\n";
			for (Action a: r.actions)
				res += "player " + a.botID + " " + a.state + " (" + a.amount +  ")\n";
		}
		return res;
	}
	
	private State getCopy(){
		try {
			return (State) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public State getState(){
		return getCopy();
	}
}
