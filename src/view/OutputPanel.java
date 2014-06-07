package view;

import utils.TXTPanel;
import utils.Value;
import utils.Value.state;

public class OutputPanel extends TXTPanel {

	private static final long serialVersionUID = 1L;
	
	String s;
	
	public OutputPanel(){
		super();
	}

	public void dealedCard(int ID, Integer c1, Integer c2){
		addLog("player " + ID + " was dealed cards " + Value.getCardName(c1) + ", " + Value.getCardName(c2));
	}
	
	public void setAction(int ID, state state, double chips){
		if (state != Value.state.none){
			if (chips == 0)
				s = "player " + ID + " " + state.name();
			else
				s = "player " + ID + " " + state.name() + " (" + chips + ").";
			addLog(s);
		}
	}
}
