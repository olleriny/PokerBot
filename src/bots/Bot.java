package bots;

import game.GameEngine;
import game.State;
import utils.Value;

public abstract class Bot {
	
	private int steps = 0;
	protected int ID;
	protected int score;
	
	protected double pPot, nPot, ehs;
	protected double[] hs = new double[3];
	
	protected double chips;
	protected double totalStake;
	protected double roundStake;
	
	protected String name;
	
	protected Integer c1, c2;
	
	protected Value.state state;

	private GameEngine engine;
	
	public Bot(int id, double chips){
		this.ID = id;
		this.chips = chips;
	}

	public abstract void doAct(double max, State state);
	
	// TODO act if chips == 0
	public void act(double max, State state){
		steps = 0;
		if (chips == 0){
			
		} else
			doAct(max, state);
	}
	
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
	
	public void setEngine(GameEngine engine){
		this.engine = engine;
	}
	
	public void dealCards(Integer c1, Integer c2){
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public String toString() {
		return getName();
	}

	public int getID() {
		return ID;
	}
	
	public double getChips(){
		return chips;
	}
	
	public void setState(Value.state state){
		this.state = state;
	}
	
	public Value.state getState(){
		return state;
	}
	
	public Integer[] getCards(){
		return new Integer[] {c1, c2};
	}
	
	public Integer getCard1(){
		return this.c1;
	}
	
	public Integer getCard2(){
		return this.c2;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public void winsChips(double chips){
		this.chips += chips;
	}
	
	public double getTotalStake(){
		return this.totalStake;
	}
	
	public void nullEverything(){
		score = 0;
		totalStake = 0;
		roundStake = 0;
	}
	
	public void takeFromTotalStake(double chips){
		this.totalStake -= chips;
	}
	
	public double getRoundStake(){
		return roundStake;
	}
	
	public void newInnerRound(){
		roundStake = 0;
	}
	
	public double payBlinds(double blind){
		if (chips > blind){
			update(blind);
			return blind;
		} else {
			setState(Value.state.allIn);
			update(chips);
			return totalStake;
		}
	}
	
	private boolean stepsAreOK(){
		if (steps != 0){
			System.out.println("only one action is allowed");
			return false;
		}
		return true;
	}
	
	private boolean chipsAreOK(double chips){
		if (chips > this.chips){
			allIn();
			System.out.println(ID + ": you dont have enough chips to do that act, you went all in");
			return false;
		}
		return true;
	}

	public void end(){
		c1 = Value.emptyCard;
		c2 = Value.emptyCard;
		setState(Value.state.left);
		engine.plLeft(ID);
	}
	
	private void update(double chips){
		this.chips -= chips;
		totalStake += chips;
		roundStake += chips;
	}
	
	private boolean passed(double chips){
		return stepsAreOK() && chipsAreOK(chips);
	}
	
	//****** player's actions *****//
	
	private void setAction(Value.state state, double chips) {
		if (!passed(chips))
			return;
		steps += 1;
		update(chips);
		setState(state);
		engine.setAction(ID, state, chips);
	}
	
	public void check(){
		setAction(Value.state.checked, 0);
	}

	public void call(double chips){
		setAction(Value.state.called, chips);
	}
	
	public void raise(double chips){
		setAction(Value.state.raised, chips);
	}
	
	public void fold(){
		setAction(Value.state.folded, 0);
	}
	
	public void allIn(){
//		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		setAction(Value.state.allIn, chips);
	}
}
