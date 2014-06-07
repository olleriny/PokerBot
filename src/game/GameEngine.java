package game;

import java.util.Arrays;

import helps.Bank;
import helps.Ranks;
import bots.Bot;

//import computes.Probability;

import utils.ListOfPlayers;
import utils.Value;
import utils.Value.*;

public class GameEngine {
	
	private Thread game;
	
	private boolean isRunning = true;
	private boolean end = false;
	
	private Talon talon = new Talon();
	
	private ViewEngine viewEngine;
	
	private int dealer = 0;
	
	private Integer[] deck = new Integer[5];
	
	private double initChips = 500000;
	
	private Bank bank;
	
	private State gameState;
	
	private ListOfPlayers players = new ListOfPlayers(initChips);
	
//	private Probability probab = new Probability();
	
	private int rounds = 0;
	private int speed = 0;

	private int actBlindIndex = 0;
	private int smallBlinds[] = {25, 50, 100, 250, 500, 1000, 1500, 2000, 5000, 10000, 15000, 25000, 50000, 100000};
	private int bigBlind = smallBlinds[actBlindIndex] * 2;
	
	public GameEngine(String[] pls){
		players.filter(pls);
		players.setEngine(this);
		viewEngine = new ViewEngine(this, players);
		isRunning = false;
		startGame();
	}
	
	private Integer getDealedCard(){
		return talon.getNextCard();
	}

	private void dealCards(Bot bot, Integer c1, Integer c2){
		bot.dealCards(c1, c2);
		viewEngine.dealCard(bot.getID(), c1, c2);
	}
	
	public int getRounds(){
		return rounds;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	/****** player's actions *****/
	
	public void setAction(int ID, state state, double chips){
		bank.addChips(chips);
		viewEngine.setAction(ID, state, chips);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(ID, state, chips);
	}
	
	private void setNoneState(int ID){
		players.getPlayer(ID).setState(Value.state.none);
		viewEngine.setAction(ID, state.none, 0);
	}
	
	public void plLeft(int ID){
		viewEngine.plLeft(ID);
	}
	
	/***** GAME IMPLEMENTATION ******/
	
	public void startGame(){
		game = new Thread(){
			public void run(){
				play();
			}
		};
		game.start();
	}
	
	public void endGame(){
		pauseGame();
		end = true;
	}
	
	public void pauseGame(){
		isRunning = false;
	}
	
	public void continueGame(){
		isRunning = true;
	}
	
	private void play(){
		dealer = players.getActivePlayers().get(0).getID();
		bank = new Bank(players);
		while (!end) {
			try {Thread.sleep(50);} catch (Exception e1) {e1.printStackTrace();}
			while (isRunning){
				rounds += 1;
				viewEngine.setRounds(rounds);
				initNewRound();
				setDealer();
				gameState = new State(dealer, smallBlinds[actBlindIndex], players);
				playRounds();
				if (speed != 0) try { Thread.sleep(speed);} catch (Exception e) {}
				viewEngine.newRound(false);
				players.checkChipsAll();
				if (isWinner())
					break;
				if (speed != 0) try {Thread.sleep(speed);} catch (Exception e) {}
			}
		}
//		probab.write();
//		probab.saveProbability();
//		try {Thread.sleep(1000);} catch (Exception e1) {e1.printStackTrace();}
//		probab.loadProbability();
		System.out.println("game ended");
	}
	
	private boolean isWinner(){
		if (players.getSize() == 1){
			System.out.println("winner is " + players.getAllPlayers().get(0).getName() + players.getAllPlayers().get(0).getID());
			end = true;
			return true;
		}
		return false;
	}
	
	private void setDealer(){
		dealer = players.getNext(dealer).getID();
	}

	private void setBlinds(){
		Bot pl = players.getNextActivePlayer(dealer);
		double sb, bb;
		if (rounds % 150 == 0){
			actBlindIndex += 1;
			bigBlind = smallBlinds[actBlindIndex] * 2;
			System.out.println("bigBlind = " + bigBlind);
		}
		viewEngine.setTxtLog("Blinds are " + smallBlinds[actBlindIndex] + "/" + bigBlind);
		
		sb = pl.payBlinds(smallBlinds[actBlindIndex]);
		bank.addChips(sb);
		gameState.addAction(pl.getID(), Value.state.smallBlind, sb);
		viewEngine.setAction(pl.getID(), state.smallBlind, sb);
		
		pl = players.getNextActivePlayer(pl.getID());
		bb = pl.payBlinds(bigBlind);
		bank.addChips(bb);
		gameState.addAction(pl.getID(), state.bigBlind, bb);
		viewEngine.setAction(pl.getID(), state.bigBlind, bb);
	}
	
	private void dealCardsToAllPlayers(){
		for (Bot p: players.getAllPlayers()){
			dealCards(p, getDealedCard(), getDealedCard());
//			dealCards(p, Value.getHash(value._A, suit.DIAMONDS), Value.getHash(value._Q, suit.CLUBS));
//			dealCards(p, Value.getHash(value._5, suit.HEARTS), Value.getHash(value._2, suit.HEARTS));
//			dealCards(p, Value.getHash(value._8, suit.SPADES), Value.getHash(value._6, suit.HEARTS));
		}
	}

	private void playRounds(){
		for (int i = 0; i <= 3; i++){
			playRound(i);
		}
		setAllScores();
		viewEngine.setTxtLog(bank.splitAll());
	}
	
	private void playRound(int i){
		clearActions();
		if (i == 0)
			setPreflop();
		if (i == 1)
			setFlop();
		if (i == 2)
			setTurn();
		if (i == 3)
			setRiver();
		if (!isEnoughPlayers()){
			return;
		}
		gameState.setRound(i, deck);
		botActions(i);
	}
	
	private boolean isEnoughPlayers(){
		return players.getActivePlayers().size() > 1;
	}
	
	// TODO
	private void botActions(int round){
		double max = 0;
		if (round == 0){
//			setBlinds();
			max = bigBlind;
		}
		Bot b = players.getNextActivePlayer(players.getNextActivePlayer(players.getNextActivePlayer(dealer).getID()).getID());
		int ID = -1;
		do {
			viewEngine.isOnMove(b.getID());
			if (speed != 0) try {Thread.sleep(speed);} catch (Exception e) {}
			if (ID == -1)
				ID = b.getID();
			gameState.setBank(bank.getChips());
			b.act(max, gameState.getState());
			if (!b.getState().equals(Value.state.folded)){
				if (b.getRoundStake() > max){
					max = b.getRoundStake();
					ID = b.getID();
				}
			} else {
				if (ID == b.getID())
					ID = -1;
			}
			if (!isEnoughPlayers())
				return;
			b = players.getNextActivePlayer(b.getID());
		} while (ID != b.getID());
	}
	
	private void clearActions(){
		for (Bot bot: players.getActivePlayers()){
			bot.newInnerRound();
			setNoneState(bot.getID());
		}
	}
	
	private void initNewRound(){
		talon.shuffle();
		for (Bot bot: players.getAllPlayers()){
			setNoneState(bot.getID());
			bot.nullEverything();
		}
		Arrays.fill(deck, null);
		viewEngine.newRound(true);
	}
	
	private void setPreflop(){
		dealCardsToAllPlayers();
	}
	
	private void setFlop(){
		for (int i = 0; i < 3; i++){
			deck[i] = talon.getNextCard();
		}
//		deck[0] = Value.getHash(value._3, suit.HEARTS);
//		deck[1] = Value.getHash(value._4, suit.CLUBS);
//		deck[2] = Value.getHash(value._J, suit.HEARTS);
		viewEngine.setFlop(deck[0], deck[1], deck[2]);
	}
	
	private void setTurn(){
		deck[3] = talon.getNextCard();
		viewEngine.setTurn(deck[3]);
	}
	
	private void setRiver(){
		deck[4] = talon.getNextCard();
		viewEngine.setRiver(deck[4]);
	}
	
	private int setAllScores(){
		int score;
		int max = 0;
		for (Bot bot: players.getActivePlayers()){
			score = Ranks.findScore(Value.concatCards(deck, bot.getCards()));
			bot.setScore(score);
			if (score >= max){
				max = score;
			}
//				Arrays.sort(cards, Rules.compValues);
//				System.out.print("bot " + bot.getID() + " - " + Value.hands.values()[((score & 0xf00000) >> 0x14) - 6] + " (" + score + ")");
//				for (int i = 0; i < cards.length; i++)
//					System.out.print(", " + cards[i]);
//				System.out.println();
		}
		for (Bot bot: players.getActivePlayers()){
			if (bot.getScore() == max){
//				probab.addCard(bot.getCard1(), bot.getCard2());
				viewEngine.addStats(bot.getCard1(), bot.getCard2());
			}
		}
		return 0;
	}
}
