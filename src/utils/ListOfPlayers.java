package utils;

import game.GameEngine;

import java.util.ArrayList;

import bots.Bot;

import utils.Value.*;

public class ListOfPlayers {

	private ArrayList<Bot> players = new ArrayList<>();
	private double chips;
	private int size;
	private Bot temp;
	
	public ListOfPlayers(double initChips){
		chips = initChips;
	}
	
	public ArrayList<Bot> getAllPlayers(){
		return players;
	}
	
	public Bot getNextActivePlayer(int ID){
		if ((temp = getNext(ID)).getState() != state.folded)
			return temp;
		else 
			return getNextActivePlayer(temp.getID());
	}
	
	public Bot getNext(int ID){
		for (int i = 0; i < players.size(); i++){
			if (players.get(i).getID() == ID)
				return players.get((i + 1) % size);
		}
//		System.out.println("null");
		return players.get(0);
	}
	
	public ArrayList<Bot> getActivePlayers(){
		ArrayList<Bot> res = new ArrayList<>();
		for (Bot bot: players){
			if (!bot.getState().equals(Value.state.folded))
				res.add(bot);
		}
		return res;
	}

	public void filter(String[] pls){
		Bot p = null;
		int i = 0;
		for (String s: pls){
			if ((p = Value.createNewPlayer(s, i, chips)) != null){
				p.setState(Value.state.none);
				players.add(p);
			}
			i += 1;
		}
		size = players.size();
	}
	
	public Bot getPlayer(int ID){
		for (Bot p: players){
			if (p.getID() == ID)
				return p;
		}
		return null;
	}
	
	public int getSize(){
		return size;
	}
	
	public void removePlayer(int ID){
		if (players.remove(getPlayer(ID)))
			size -= 1;
	}
	
	public void setEngine(GameEngine e){
		for (Bot p: players)
			p.setEngine(e);
	}
	
	public void checkChipsAll(){
		for (int i = players.size() - 1; i >= 0; i--){
			if (players.get(i).getChips() <= 0){
				players.get(i).end();
				players.remove(i);
				size -= 1;
			}
		}
	}
}
