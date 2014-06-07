package helps;

import java.util.Arrays;
import java.util.Comparator;

import utils.Value;
import utils.Value.value;

public class Ranks {

//	public static enum hands {highCard, onePair, twoPair, triple, straight, flush, fullHouse, fourOfAKind, straightFlush, royalFlush};
	
	public static Comparator<Integer> compValues = new Comparator<Integer>() {
		public int compare(Integer c1, Integer c2) {
			return Value.getCardValue(c1).ordinal() - Value.getCardValue(c2).ordinal();
		}
	};
	
	private static Comparator<Integer> compSuits = new Comparator<Integer>() {
		public int compare(Integer c1, Integer c2) {
			return Value.getCardSuit(c1).ordinal() - Value.getCardSuit(c2).ordinal();
		}
	};

	private static byte getByte(int ord){
		switch (ord) {
		case 0:
			return (byte) 1;
		case 1:
			return (byte) 2;
		case 2:
			return (byte) 4;
		case 3:
			return (byte) 8;
		default:
			return (byte) 0;
		}
	}

	public static int findScore(Integer[] cards){
		return getHand(Arrays.copyOf(cards, cards.length));
//		return getHand(cards);
	}

	private static int getHand(Integer[] cards){
		int res;
		if ((res = isRoyalFlush(cards)) != 0)
			return res;
		if ((res = isStraightFlush(cards)) != 0)
			return res;
		if ((res = isFourOfAKind(cards)) != 0)
			return res;
		if ((res = isFullHouse(cards)) != 0)
			return res;
		if ((res = isFlush(cards)) != 0)
			return res;
		if ((res = isStraight(cards)) != 0)
			return res;
		if ((res = isTriple(cards)) != 0)
			return res;
		if ((res = isTwoPair(cards)) != 0)
			return res;
		if ((res = isOnePair(cards)) != 0)
			return res;
		if ((res = isHighCard(cards)) != 0)
			return res;
		return 0;
	}

	private static int isHighCard(Integer[] cards){
		boolean[] bucket = new boolean[13];
		int k1 = -1, k2 = -1, k3 = -1, k4 = -1, k5 = -1;
		Arrays.fill(bucket, false);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal()] = true;
		}
		for (int i = bucket.length - 1; i >= 0; i--){
			if (bucket[i]){
				if (k1 == -1)
					k1 = i;
				else if (k2 == -1)
					k2 = i;
				else if (k3 == -1)
					k3 = i;
				else if (k4 == -1)
					k4 = i;
				else if (k5 == -1)
					k5 = i;
			}
		}
		return 0x600000 | k1 << 0x10 | k2 << 0xc | k3 << 0x8 | k4 << 0x4 | k5;
	}
	
	private static int isOnePair(Integer[] cards){
		int[] bucket = new int[13];
		int pair = -1, k1 = -1, k2 = -1, k3 = -1;
		Arrays.fill(bucket, 0);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal()] += 1;
		}
		for (int i = bucket.length - 1; i >= 0; i--){
			if (bucket[i] == 2)
				pair = i;
			else 
				if (bucket[i] == 1){
					if (k1 == -1)
						k1 = i;
					else if (k2 == -1)
						k2 = i;
					else if (k3 == -1)
						k3 = i;
				}
		}
		if (pair != -1)
			return 0x700000 | pair << 0x10 | k1 << 0xc | k2 << 0x8 | k3 << 0x4;
		return 0;
	}

	private static int isTwoPair(Integer[] cards){
		int[] bucket = new int[13];
		int pmax = -1;
		int pmin = -1;
		int kicker = -1;
		Arrays.fill(bucket, 0);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal()] += 1;
		}
		for (int i = bucket.length - 1; i >= 0; i--){
			if (bucket[i] == 2){
				if (pmax == -1){
					pmax = i;
				} else 
					if (pmin == -1){
						pmin = i;
					}
					else
						if (kicker == -1){
							kicker = i;
							break;
						}
			} else {
				if (bucket[i] == 1)
					if (kicker == -1)
						kicker = i;
			}
//			if (pmin != -1)
//				if (kicker == -1)
//					kicker = i;
		}
		if (pmin != -1 && pmax != -1)
			return 0x800000 | pmax << 0x10 | pmin << 0xc | kicker << 0x8;
		return 0;
	}

	private static int isTriple(Integer[] cards){
		int[] bucket = new int[13];
		int triple = -1;
		int max1 = -1;
		int max2 = -1;
		Arrays.fill(bucket, 0);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal()] += 1;
		}
		for (int i = bucket.length - 1; i >= 0; i--){
			if (bucket[i] == 3){
				if (triple == -1)
					triple = i;
			} else {
				if (max1 == -1){
					if (bucket[i] > 0)
						max1 = i;
				} else {
					if (max2 == -1)
						if (bucket[i] > 0)
							max2 = i;		
				}	
			}
		}
		if (triple != -1)
			return 0x900000 | triple << 0x10 | max1 << 0xc | max2 << 0x8;
		return 0;
	}
	
	private static int isStraight(Integer[] cards){
		int[] bucket = new int[14];
		int count = 0;
		Arrays.fill(bucket, 0);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal() + 1] += 1;
			if (Value.getCardValue(cards[i]).equals(value._A))
				bucket[0] += 1;
		}
		for (int i = value._A.ordinal() + 1; i >= 0; i--){
			if (bucket[i] > 0) {
				count += 1;
			} else
				count = 0;
			if (count == 5)
				return 0xa00000 | i << 0x10;
		}
		return 0;
	}
	
	private static int isFlush(Integer[] cards){
		int count = 0;
		boolean[] bucket = new boolean[13];
		Arrays.fill(bucket, false);
		int res = 0xb00000;
		int color = -1;
		Arrays.sort(cards, compSuits);
		for (int i = 0; i < cards.length - 1; i++){
			if (Value.getCardSuit(cards[i]).ordinal() == Value.getCardSuit(cards[i + 1]).ordinal()){
				count += 1;
			}
			else {
				count = 0;
			}
			if (count == 4){
				color = Value.getCardSuit(cards[i]).ordinal();
				break;
			}
		}
		for (int i = 0; i < cards.length; i++){
			if (Value.getCardSuit(cards[i]).ordinal() == color)
				bucket[Value.getCardValue(cards[i]).ordinal()] = true;
		}
		if (color != -1){
			count = 5;
			for (int i = bucket.length - 1; i >= 0; i--){
				if (count == 0)
					break;
				if (bucket[i]){
					count -= 1;
					res |= (i << (count*4));
				}
			}
		}
		if (color != -1)
			return res;
		return 0;
	}
	
	private static int isFullHouse(Integer[] cards){
		int[] bucket = new int[13];
		int _2 = -1, _3 = -1;
		Arrays.fill(bucket, 0);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal()] += 1;
		}
		for (int i = bucket.length - 1; i >= 0; i--){
			if (bucket[i] == 3){
				_3 = i;
				break;
			}
		}
		if (_3 == -1)
			return 0;
		for (int i = bucket.length - 1; i >= 0; i--){
			if (bucket[i] >= 2)
				if (i != _3){
					_2 = i;
					break;
				}
		}
		if (_2 != -1 && _3 != -1)
			return 0xc00000 | Math.max(_2, _3) << 0x10 | Math.min(_2, _3) << 0xc;
		return 0;
	}

	private static int isFourOfAKind(Integer[] cards){
		int[] bucket = new int[13];
		Arrays.fill(bucket, 0);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal()] += 1;
			if (bucket[Value.getCardValue(cards[i]).ordinal()] == 4)
				return 0xd00000 | Value.getCardValue(cards[i]).ordinal() << 0x10;
		}
		return 0;
	}

	private static int isStraightFlush(Integer[] cards){
		byte[] bucket = new byte[14];
		byte res = (byte) 15;
		int count = 0;
		Arrays.fill(bucket, (byte) 0);
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal() + 1] |= getByte(Value.getCardSuit(cards[i]).ordinal());
			if (Value.getCardValue(cards[i]).equals(value._A))
				bucket[0] |= getByte(Value.getCardValue(cards[i]).ordinal());
		}
		for (int i = 0; i <= value._A.ordinal(); i++){
			if (bucket[i] != 0){
				if ((bucket[i] & bucket[i + 1]) != 0){
					if ((res & bucket[i + 1]) != 0){
						res &= bucket[i];
						count += 1;
					} else {
						res = bucket[i + 1];
						count = 1;
					}
				} else {
					count = 0;
					res = (byte) 15;
				}
				if (count == 4)
					if (res != 0){
						return 0xe00000 | (i + 1) << 0x10;
					}
			} else {
				count = 0;
				res = (byte) 15;
			}
		}
		return 0;
	}
	
	private static int isRoyalFlush(Integer[] cards){
		byte[] bucket = new byte[13];
		Arrays.fill(bucket, (byte) 0);
		byte res = (byte) 15;
		for (int i = 0; i < cards.length; i++){
			bucket[Value.getCardValue(cards[i]).ordinal()] |= (byte) getByte(Value.getCardSuit(cards[i]).ordinal());
		}
		for (int i = value._10.ordinal(); i <= value._A.ordinal(); i++){
			res &= bucket[i];
		}
		if (res != 0)
			return 0xf00000;
		return 0;
	}
}
