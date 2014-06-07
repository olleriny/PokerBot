package bots;

import game.State;

import java.util.Random;

//import utils.CardGenerator;

import computes.HandStrength;

public class EasyBot extends Bot {

	private static String staticName = "Easy bot";
	private State state;
	private Integer[] deck;

//	private CardGenerator cGen = new CardGenerator();
//	private ArrayList<Integer> cards;
	
	public EasyBot(int id, double chips) {
		super(id, chips);
		setName(getStaticName());
	}

	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void doAct(double max, State state) {
		this.state = state;
		deck = state.deck;
		max -= roundStake;
//		simpleAct(max);
		computeAct(max);
	}
	
	private void computeAct(double max){
//		if (state.round == 0)
//		cards = cGen.getCards();
		if (state.round == 1)
			round1();
		if (state.round == 2)
			round2();
		if (state.round == 3)
			round3();
		call(max);
	}
	
	private void simpleAct(double max){
		if (max == 0){
			Random r = new Random();
			if (r.nextBoolean())
				check();
			else
				raise(20);
		} else {
			Random r = new Random();
			if (r.nextInt(10) < 8)
				call(max);
			else
				if (r.nextInt(10) < 7)
					raise(max + 10);
				else
					fold();
		}
	}
	
	private void round1(){
		hs[0] = HandStrength.countHandStrengthRound1(deck, getCards());
		double[] probabs = HandStrength.countEffectiveHandStrength(hs[0], deck, getCards());
		System.out.println("ROUND " + state.round + " ID = " + ID);
		ehs = probabs[0];
		pPot = probabs[1];
		nPot = probabs[2];
		System.out.println("\ths = " + hs[0]);
		System.out.println("\tpPot = " + pPot + ", nPot = " + nPot);
		System.out.println("\teHs = " + ehs);
//		int all = 0;
//		removeCardsFromDeck();
//		for (int i = 0; i < cards.size() - 1; i++){
//			for (int j = i + 1; j < cards.size(); j++){
//				deck[3] = cards.get(i);
//				deck[4] = cards.get(j);
//				hs += countHandStrength(i, j);
//				all += 1;
//			}
//		}
//		hs = (double) hs / all;
//		deck[3] = null;
//		deck[4] = null;
//		System.out.println(hs - HandStrength.countHandStrength(1, state.deck, getCards()));
//		countEffectiveHandStrength();
//		ehs = hs * (1 - nPot) + (1 - hs) * pPot;
	}
	
	private void round2(){
		hs[1] = HandStrength.countHandStrengthRound2(deck, getCards());
		System.out.println("ROUND " + state.round + " ID = " + ID);
		System.out.println("\ths = " + hs[1]);
//		int all = 0;
//		removeCardsFromDeck();
//		for (int i = 0; i < cards.size() - 1; i++){
//			deck[4] = cards.get(i);
//			hs += countHandStrength(-1, i);
//			all += 1;
//		}
//		hs = (double) hs / all;
//		deck[4] = null;
//		System.out.println(hs - HandStrength.countHandStrength(2, state.deck, getCards()));
	}
	
	private void round3(){
		hs[2] = HandStrength.countHandStrengthRound3(deck, getCards());
		System.out.println("ROUND " + state.round + " ID = " + ID);
		System.out.println("\ths = " + hs[2]);
//		removeCardsFromDeck();
//		double hs = countHandStrength(-1, -1);
//		System.out.println(hs - HandStrength.countHandStrength(3, state.deck, getCards()));
	}

//	private void removeCardsFromDeck(){
//		cards.removeAll(Arrays.asList(deck));
//		cards.removeAll(Arrays.asList(getCards()));
//	}
	
//	private void countEffectiveHandStrength(){
//		int[][] hp = new int[3][3];
//		int[] hpTot = new int[3];
//		Integer[] board = Arrays.copyOf(deck, 5);
//		Integer[] opCards = new Integer[2];
//		board[3] = getCard1();
//		board[4] = getCard2();
//		int myActRank = Ranks.findScore(board);
//		int myRank;
//		int opRank, index;
//		double pPot, nPot;
//		for (int i = 0; i < cards.size() - 1; i++){
//			for (int j = i + 1; j < cards.size(); j++){
//				opCards[0] = cards.get(i);
//				opCards[1] = cards.get(j);
//				board[3] = opCards[0];
//				board[4] = opCards[1];
//				opRank = Ranks.findScore(board);
//				if (myActRank > opRank)
//					index = 0;
//				else if (myActRank == opRank)
//					index = 1;
//				else
//					index = 2;
//				for (int k = 0; k < cards.size() - 1; k++){
//					for (int l = k + 1; l < cards.size(); l++){
//						if (i != k && i != l && j != k && j != l){
//							board[3] = cards.get(k);
//							board[4] = cards.get(l);
//							myRank = Ranks.findScore(Value.concatCards(board, getCards()));
//							opRank = Ranks.findScore(Value.concatCards(board, opCards));
//							if (myRank > opRank)
//								hp[index][0] += 1;
//							else if (myRank == opRank)
//								hp[index][1] += 1;
//							else
//								hp[index][2] += 1;
//							hpTot[index] += 1;
//						}
//					}
//				}
//			}
//		}
//		pPot = (double) (hp[1][0]/2 + hp[2][0] + hp[2][1]/2) / (hpTot[2] + hpTot[1]/2);
//		nPot = (double) (hp[0][1]/2 + hp[0][2] + hp[1][2]/2) / (hpTot[0] + hpTot[1]/2);
//		
////		pPot = (double) (hp[2][0] + hp[2][1]/2 + hp[1][0]/2) / (hpTot[2] + hpTot[1]);
////		nPot = (double) (hp[0][2] + hp[1][2]/2 + hp[0][1]/2) / (hpTot[0] + hpTot[1]);
////		Ppot = (HP[behind][ahead]+HP[behind][tied]/2+HP[tied][ahead]/2)/(HPTotal[behind]+HPTotal[tied])
////		Npot = (HP[ahead][behind]+HP[tied][behind]/2+HP[ahead][tied]/2)/(HPTotal[ahead]+HPTotal[tied])
//		this.pPot = pPot;
//		this.nPot = nPot;
//		deck[3] = null;
//		deck[4] = null;
//	}
	
//	private double countHandStrength(int i, int j){
//		int all = 0, good = 0;
//		int myRank = Ranks.findScore(Value.concatCards(deck, getCards()));
//		int opRank;
//		for (int k = 0; k < cards.size(); k++){
//			for (int l = k + 1; l < cards.size(); l++){
//				if (k != i && k != j && l != i && l != j){
//					opRank = Ranks.findScore(Value.concatCards(deck, new Integer[] {cards.get(k), cards.get(l)}));
//					if (myRank >= opRank)
//						good += 1;
//					all += 1;
//				}
//			}
//		}
//		return (double) good / all;
//	}
}
