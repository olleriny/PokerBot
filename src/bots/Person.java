package bots;

import view.PlayerWindow;
import game.State;

public class Person extends Bot {

	private static String staticName = "Player";
	private boolean run = true;
	private PlayerWindow window;
	private double max = 0;
	
	public Person(int id, double chips) {
		super(id, chips);
		setName(getStaticName());
	}
	
	public void registerWindow(PlayerWindow window){
		this.window = window;
	}
	
	public double getMax(){
		return max;
	}
	
	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void doAct(double max, State state) {
		this.max = max - getRoundStake();
		window.go();
		while(run){
			try {Thread.sleep(100);} catch (Exception e) {e.printStackTrace();}
		}
		run = true;
	}
	
	public void stop(){
		window.setEnabledComps(false);
		run = false;
	}
}
