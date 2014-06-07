package game;

import java.util.ArrayList;
import java.util.Collections;

import utils.CardGenerator;

public class Talon {

	private ArrayList<Integer> cards = new ArrayList<>();
	private static CardGenerator cGen = new CardGenerator();
	private int index = 0, size = 0;
	
	public Talon(){
		createAllCards();
		shuffle();
	}
	
	public Integer getNextCard(){
		index = (index + 1) % size;
		return cards.get(index);
	}
	
	public void shuffle(){
		index = 0;
		Collections.shuffle(cards);
	}
	
	private void createAllCards(){
		cards = cGen.getCards();
		size = cards.size();
	}
}
