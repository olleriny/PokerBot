package utils;

import java.util.ArrayList;

import utils.Value.suit;
import utils.Value.value;

public class CardGenerator {

	private ArrayList<Integer> cards = new ArrayList<Integer>();
	
	public CardGenerator(){
		createAllCards();
	}
	
	private void createAllCards(){
		cards.clear();
		for (suit s: Value.suit.values()){
			for (value v: Value.value.values()){
				cards.add(Value.getHash(v, s));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getCards(){
		return (ArrayList<Integer>) cards.clone();
	}
	
	public void checkCards(){
		System.out.println("This is CardGenerator: " + Value.getCardName(cards.get(0)));
	}
	
}
