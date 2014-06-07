package computes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

//import game.Card;
import utils.Value;
import utils.Value.suit;
import utils.Value.value;

public class Probability implements Serializable {
	
	private static final long serialVersionUID = 1L;

//	private static String fileName = "C:/SKOLA/dipl/probab.txt";
	private static String fileName = "C:/SKOLA/dipl/p2players.txt";
	
	private int values = Value.value.values().length;
	private int suits = Value.suit.values().length;
	private int all = suits * values;
	
	private int alll = 0;
	
	private int[][] cards = new int[all][all];
	
	public Probability() {
		createAllCards();
	}
	
	private void createAllCards(){
		for (int[] i: cards){
			Arrays.fill(i, 0);
		}
	}
	
	public void computeProbab(){
		for (int i = 0; i < all; i++){
			for (int j = 0; j < i; j++){
				alll += cards[i][j];
			}
		}
		for (int i = 0; i < all; i++){
			for (int j = 0; j < i; j++){
				if ((double) cards[i][j] / alll * 1000 > 1)
					System.out.println(getCard(i) + " " + getCard(j) + " - " + (double) cards[i][j] / alll * 1000);
			}
		}
	}
	
//	public void addCard(Card c1, Card c2){
//		if (c1.getHash() > c2.getHash())
//			cards[c1.getHash()][c2.getHash()] += 1;
//		else
//			cards[c2.getHash()][c1.getHash()] += 1;
//	}
	
	public void addCard(int c1, int c2){
		if (c1 > c2)
			cards[c1][c2] += 1;
		else
			cards[c2][c1] += 1;
	}
	
	public void write(){
		for (int i = 0; i < all; i++){
			for (int j = 0; j < i; j++){
				System.out.println(getCard(i) + " " + getCard(j) + " - " + cards[i][j]);
			}
		}
	}

	private String getCard(int hash){
		return suit.values()[hash / value.values().length] + "" + value.values()[hash % value.values().length];
	}
	
	public static Probability loadProbability(){
		 try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			Probability probab = (Probability) ois.readObject();
			System.out.println("this is loaded Probability...");
//		    probab.write();
		    ois.close();
		    return probab;
		 } catch (Exception e) {
			 e.printStackTrace();
		return null;
		 }
	}
	
	public void saveProbability(){
		try {
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
	        oos.writeObject(this);
	        oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
