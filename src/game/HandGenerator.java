//package game;
//
//import helps.Ranks;
//
//import utils.Value;
//import utils.Value.suit;
//import utils.Value.value;
//
//public class HandGenerator {
//	
//	public HandGenerator() {
//		testAll();
//	}
//	
//	private void testAll(){
//		genRoyalFlush();
//		genStraightFlush();
//		genFourOfAKind();
//		genFullHouse();
//		genFlush();
//		genStraight();
//	}
//	
//	private void genStraight(){
//		Integer[] cards;
//		int i, errors = 0;
//		for (suit s: Value.suit.values()){
//			for (int v = 0; v < Value.value.values().length - 4; v++){
//				cards = new Integer[5];
//				i = 0;
//				for (int k = v; k < v + 5; k++){
//					cards[i] = new Card(Value.value.values()[k], s);
//					i += 1;
//				}
//				if ((((Ranks.findScore(cards) & 0xf00000) >> 0x14) - 6)  != Value.hands.straight.ordinal()){
//					errors += 1;
////					System.out.print("wrong eval");
////					for (int j = 0; j < cards.length; j++)
////						System.out.print(", " + cards[j]);
////					System.out.println();
//				}
//			}
//		}
//		System.out.println("Straight  - " + errors + " errors");
//	}
//	
//	private void genFlush(){
//		Integer[] cards = new Integer[5];
//		int errors = 0;
//		for (int i0 = 0; i0 < Value.value.values().length - 4; i0++){
//			cards[0] = new Card(Value.value.values()[i0], suit.CLUBS);
//			for (int i1 = i0 + 1; i1 < Value.value.values().length - 3; i1++){
//				cards[1] = new Card(Value.value.values()[i1], suit.CLUBS);
//				for (int i2 = i1 + 1; i2 < Value.value.values().length - 2; i2++){
//					cards[2] = new Card(Value.value.values()[i2], suit.CLUBS);
//					for (int i3 = i2 + 1; i3 < Value.value.values().length - 1; i3++){
//						cards[3] = new Card(Value.value.values()[i3], suit.CLUBS);
//						for (int i4 = i3 + 1; i4 < Value.value.values().length; i4++){
//							cards[4] = new Card(Value.value.values()[i4], suit.CLUBS);
//							if ((((Ranks.findScore(cards) & 0xf00000) >> 0x14) - 6)  != Value.hands.flush.ordinal()){
//								errors += 1;
////								System.out.print("wrong eval");
////								for (int j = 0; j < cards.length; j++)
////									System.out.print(", " + cards[j]);
////								System.out.println();
//							}
//						}
//					}
//				}
//			}
//		}
//		System.out.println("Flush - " + errors + " errors");
//	}
//
//	private void genFullHouse(){
//		Integer[] cards = new Integer[5];
//		int errors = 0;
//		for (value v1: Value.value.values()){
//			for (int i = 0; i < 3; i++){
//				cards[i] = new Card(v1, suit.CLUBS);
//			}
//			for (value v2: Value.value.values()){
//				for (int i = 3; i < 5; i++){
//					cards[i] = new Card(v2, suit.CLUBS);
//				}
//
//				if ((((Ranks.findScore(cards) & 0xf00000) >> 0x14) - 6)  != Value.hands.fullHouse.ordinal()){
//					errors += 1;
////					System.out.print("wrong eval");
////					for (int j = 0; j < cards.length; j++)
////						System.out.print(", " + cards[j]);
////					System.out.println();
//				}
//			}
//		}
//		System.out.println("Full House - " + errors + " errors");
//	}
//	
//	private void genFourOfAKind(){
//		Integer[] cards;
//		int i, errors = 0;
//		for (value v: Value.value.values()){
//			cards = new Integer[5];
//			for (value v1: Value.value.values()){
//				for (suit s1: Value.suit.values()){
//					cards[0] = new Card(v1, s1);
//					i = 1;
//					for (suit s: Value.suit.values()){
//						cards[i] = new Card(v, s);
//						i += 1;
//					}
//					if ((((Ranks.findScore(cards) & 0xf00000) >> 0x14) - 6)  != Value.hands.fourOfAKind.ordinal()){
//						errors += 1;
////						System.out.print("wrong eval");
////						for (int j = 0; j < cards.length; j++)
////							System.out.print(", " + cards[j]);
////						System.out.println();
//					}
//				}
//			}
//		}
//		System.out.println("Four of a kind - " + errors + " errors");
//	}
//	
//	private void genStraightFlush(){
//		Integer[] cards;
//		int i, errors = 0;
//		for (suit s: Value.suit.values()){
//			for (int v = 0; v <= Value.value.values().length - 5; v++){
//				cards = new Integer[5];
//				i = 0;
//				for (int k = v; k < v + 5; k++){
//					cards[i] = Value.createHash(Value.value.values()[k], s);
//					i += 1;
//				}
//				if ((((Ranks.findScore(cards) & 0xf00000) >> 0x14) - 6)  != Value.hands.straightFlush.ordinal()){
//					errors += 1;
////					System.out.print("wrong eval");
////					for (int j = 0; j < cards.length; j++)
////						System.out.print(", " + cards[j]);
////					System.out.println();
//				}
//			}
//		}
//		System.out.println("Straight flushes - " + errors + " errors");
//	}
//
//	private void genRoyalFlush(){
//		Integer[] cards;
//		int i, errors = 0;
//		for (suit s: Value.suit.values()){
//			cards = new Integer[5];
//			i = 0;
//			for (value v: Value.value.values()){
//				if (v.ordinal() >= Value.value._10.ordinal()){
//					cards[i] = Value.createHash(v, s);
//					i += 1;
//				}
//			}
//			if ((((Ranks.findScore(cards) & 0xf00000) >> 0x14) - 6)  != Value.hands.royalFlush.ordinal()){
//				errors += 1;
////				System.out.print("wrong eval" + cards);
////				for (int j = 0; j < cards.length; j++)
////					System.out.print(", " + cards[j]);
////				System.out.println();
//			}
//		}
//		System.out.println("Royal flushes - " + errors + " errors");
//	}
//}
