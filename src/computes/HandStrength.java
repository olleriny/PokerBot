package computes;

import java.util.ArrayList;
import java.util.Arrays;

import helps.Ranks;
import utils.CardGenerator;
import utils.Value;

public class HandStrength {
	
	private static CardGenerator cGen = new CardGenerator();
	
	public static double countHandStrengthRound1(Integer[] deck, Integer[] myCards){
		int all = 0;
		double hs = 0;
		Integer[] board = Arrays.copyOf(deck, 5);
		ArrayList<Integer> cards = cGen.getCards();
		cards.removeAll(Arrays.asList(deck));
		cards.removeAll(Arrays.asList(myCards));
		for (int i = 0; i < cards.size() - 1; i++){
			board[3] = cards.get(i);
			for (int j = i + 1; j < cards.size(); j++){
				board[4] = cards.get(j);
				hs += countHS(board, myCards, cards, i, j);
				all += 1;
			}
		}
		return (double) hs / all;
	}
	
	public static double countHandStrengthRound2(Integer[] deck, Integer[] myCards){
		int all = 0;
		double hs = 0;
		Integer[] board = Arrays.copyOf(deck, 5);
		ArrayList<Integer> cards = cGen.getCards();
		cards.removeAll(Arrays.asList(board));
		cards.removeAll(Arrays.asList(myCards));
		for (int i = 0; i < cards.size(); i++){
			board[4] = cards.get(i);
			hs += countHS(board, myCards, cards, i, -1);
			all += 1;
		}
		return (double) hs / all;
	}
	
	public static double countHandStrengthRound3(Integer[] deck, Integer[] myCards){
		ArrayList<Integer> cards = cGen.getCards();
		Integer[] board = Arrays.copyOf(deck, 5);
		cards.removeAll(Arrays.asList(board));
		cards.removeAll(Arrays.asList(myCards));
		return countHS(board, myCards, cards, -1, -1);
	}
	
	private static double countHS(Integer[] board, Integer[] myCards, ArrayList<Integer> cards, int i, int j){
		int all = 0, good = 0;
		int opRank;
		int myRank = Ranks.findScore(Value.concatCards(board, myCards));
		for (int k = 0; k < cards.size() - 1; k++){
			for (int l = k + 1; l < cards.size(); l++){
				if (i != k && i != l && j != k && j != l){
					opRank = Ranks.findScore(Value.concatCards(board, new Integer[] {cards.get(k), cards.get(l)}));
					if (myRank >= opRank)
						good += 1;
					all += 1;
				}
			}
		}
		return (double) good / all;
	}
	
	public static double[] countEffectiveHandStrength(double hs, Integer[] deck, Integer[] myCards){
		int[][] hp = new int[3][3];
		int[] hpTot = new int[3];
		ArrayList<Integer> cards = cGen.getCards();
		Integer[] board = Arrays.copyOf(deck, 5);
		Integer[] opCards = new Integer[2];
		board[3] = myCards[0];
		board[4] = myCards[1];
		cards.removeAll(Arrays.asList(board));
		int myActRank = Ranks.findScore(board);
		int myRank, opRank, index;
		double pPot, nPot;
		
		for (int i = 0; i < cards.size() - 1; i++){
			opCards[0] = cards.get(i);
			board[3] = opCards[0];
			for (int j = i + 1; j < cards.size(); j++){
				opCards[1] = cards.get(j);
				board[4] = opCards[1];
				opRank = Ranks.findScore(board);
				if (myActRank > opRank)
					index = 0;
				else if (myActRank == opRank)
					index = 1;
				else
					index = 2;
				for (int k = 0; k < cards.size() - 1; k++){
					board[3] = cards.get(k);
					for (int l = k + 1; l < cards.size(); l++){
						if (i != k && i != l && j != k && j != l){
							board[4] = cards.get(l);
							myRank = Ranks.findScore(Value.concatCards(board, myCards));
							opRank = Ranks.findScore(Value.concatCards(board, opCards));
							if (myRank > opRank)
								hp[index][0] += 1;
							else if (myRank == opRank)
								hp[index][1] += 1;
							else
								hp[index][2] += 1;
							hpTot[index] += 1;
						}
					}
				}
			}
		}
		pPot = (double) (hp[1][0]/2 + hp[2][0] + hp[2][1]/2) / (hpTot[2] + hpTot[1]/2);
		nPot = (double) (hp[0][1]/2 + hp[0][2] + hp[1][2]/2) / (hpTot[0] + hpTot[1]/2);
		return new double[] {hs * (1 - nPot) + (1 - hs) * pPot, pPot, nPot};
	}
}
