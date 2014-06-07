package utils;

import java.util.Arrays;
import java.util.Comparator;

import bots.*;

public class Value {

	public static enum suit {HEARTS, DIAMONDS, CLUBS, SPADES};
	public static enum value {_2, _3, _4, _5, _6, _7, _8, _9, _10, _J, _Q, _K, _A};
	public static enum state {none, checked, folded, raised, called, allIn, left, smallBlind, bigBlind};
	public static enum hands {highCard, onePair, twoPair, triple, straight, flush, fullHouse, fourOfAKind, straightFlush, royalFlush};
	public static Integer emptyCard = -1;

	private static int valLength = value.values().length;
	private static int suitLength = suit.values().length;

	private static value[] valuesArray;
	private static suit[] suitsArray;
	
	public static void init(){
		valuesArray = new value[valLength * suitLength];
		for (int i = 0; i < valuesArray.length; i++){
			valuesArray[i] = value.values()[i % valLength];
		}
		suitsArray = new suit[valLength * suitLength];
		for (int i = 0; i < suitsArray.length; i++){
			suitsArray[i] = suit.values()[i / valLength];
		}
	}
	
	public static String getCardName(int hash){
//		return suit.values()[hash / valLength] + "" + value.values()[hash % valLength];
		return suitsArray[hash] + "" + valuesArray[hash];
	}

	public static value getCardValue(int hash){
//		return value.values()[hash % valLength];
		return valuesArray[hash];
	}

	public static suit getCardSuit(int hash){
//		return suit.values()[hash / valLength];
		return suitsArray[hash];
	}

	public static Integer getHash(value value, suit suit){
		return suit.ordinal() * valLength + value.ordinal();
	}
	
	public static Comparator<Bot> stakeComparator = new Comparator<Bot>() {
		public int compare(Bot b1, Bot b2) {
			return (int) (b2.getTotalStake() - b1.getTotalStake());
		}
	};
	
	public static String getScoreToString(int score){
		return Value.hands.values()[((score & 0xf00000) >> 0x14) - 6].name();
	}
	
	public static Comparator<Bot> handComparator = new Comparator<Bot>() {
		public int compare(Bot b1, Bot b2) {
			if (b2.getScore() == b1.getScore())
				return (int) (b2.getTotalStake() - b1.getTotalStake());
			return (int) (b2.getScore() - b1.getScore());
		}
	};
	
	public static Integer[] concatCards(Integer[] first, Integer[] second) {
		  Integer[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}
	
	public static String[] players = {
		"Empty",
		Person.getStaticName(),
		EasyBot.getStaticName()
	};
	
	public static Bot createNewPlayer(String s, int i, double chips){
		if (s.equals(Person.getStaticName()))
			return new Person(i, chips);
		if (s.equals(EasyBot.getStaticName()))
			return new EasyBot(i, chips);
		return null;
	}
}
