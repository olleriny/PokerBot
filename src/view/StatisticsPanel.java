package view;

import utils.TXTPanel;
import utils.Value;

public class StatisticsPanel extends TXTPanel {

	private static final long serialVersionUID = 1L;
	
	public StatisticsPanel(){
		super();
	}
	
	public void statsCards(Integer c1, Integer c2){
		addLog(Value.getCardName(c1) + " " + Value.getCardName(c1));
	}
}
