package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import game.GameEngine;
import game.ViewEngine;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import bots.Bot;
import utils.Value;
import utils.Value.state;

public class ViewWindow extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int ID = -1;
	
	private Bot bot;
	
	private Color clBackground = Color.YELLOW;

	private JPanel pnlLeft = new JPanel();

	private JLabel lblState = new JLabel();
	private JLabel c1 = new JLabel();
	private JLabel c2 = new JLabel();
	private JLabel lblName = new JLabel();
	private JLabel lblChips = new JLabel();
	
	private Dimension dimPnl = new Dimension(95, 165);
	
	public ViewWindow(GameEngine gE, Bot bot){
		this.bot = bot;
		ID = bot.getID();
		init();
		setState(bot.getState());
		initLeft();
		addComponents();
	}
	
	private void initLeft(){
		lblName.setText(bot.toString() + " " + bot.getID());
		c1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		c2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		setLblChips(bot.getChips());
	}
	
	private void setLblStatus(String s){
//		lblStatus.setText("<html><body>Status:<br>" + s + "</body></html>");
		lblState.setText(s);
	}
	
	private void init(){
		setLayout(new BorderLayout());
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.PAGE_AXIS));
		pnlLeft.setBackground(clBackground);
		setBackground(clBackground);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setPreferredSize(dimPnl);
	}
	
	public void isOnMove(boolean yes){
		if (yes){
			setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		}
		else {
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}
	}
	
	public void addPanelRight(JPanel right){
		right.setBackground(clBackground);
		add(right, BorderLayout.EAST);
		dimPnl.setSize(130, dimPnl.height);
		setPreferredSize(dimPnl);
	}

	public void addPanelBot(JSlider sldRaise){
		sldRaise.setBackground(clBackground);
		add(sldRaise, BorderLayout.SOUTH);
	}
	
	private void addComponents(){
		add(lblName, BorderLayout.NORTH);
		pnlLeft.add(c1);
		pnlLeft.add(c2);
		pnlLeft.add(lblState);
		pnlLeft.add(lblChips);
		add(pnlLeft, BorderLayout.WEST);
	}
	
	public void updateCards(){
		c1.setIcon(ViewEngine.getIcon(bot.getCard1()));
		c2.setIcon(ViewEngine.getIcon(bot.getCard2()));
	}
	
	private void setState(state state){
		isOnMove(false);
		if (state.equals(Value.state.folded)){
			setOpaque(false);
			pnlLeft.setOpaque(false);
		}
		else {
			setOpaque(true);
			pnlLeft.setOpaque(true);
		}
	}
	
	private void setLblChips(double chips){
		lblChips.setText(String.valueOf(chips));
	}
	
	public int getID(){
		return ID;
	}

	public void left(){
		updateView();
		updateCards();
		setState(bot.getState());
		setOpaque(false);
		pnlLeft.setOpaque(false);
	}
	
	public void updateView(){
		setLblChips(bot.getChips());
		setLblStatus(bot.getState().name());
	}
	
	//****** player's actions *****//

	public void setAction(state state, double chips){
		setLblChips(bot.getChips());
		setLblStatus(state.name() + " " + chips);
		setState(state);
	}
}
