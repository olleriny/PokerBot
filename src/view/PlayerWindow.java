package view;

import game.GameEngine;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bots.Bot;
import bots.Person;

public class PlayerWindow extends ViewWindow {

	private static final long serialVersionUID = 1L;
	
	private JPanel pnlRight = new JPanel();
	
	private Dimension dimComp = new Dimension(70, 25);

	private JButton btnCheck = new JButton("<html><body>Check</body></html>");
	private JButton btnFold = new JButton("<html><body>Fold</body></html>");
	private JButton btnCall = new JButton("<html><body>Call<br>(0)</body></html>");
	private JButton btnRaise = new JButton("<html><body>Raise<br>(0)</body></html>");
	
	private JSlider sldRaise;
	
	private Person person;
	
	public PlayerWindow(GameEngine gE, Bot pl){
		super(gE, pl);
		person = (Person) pl;
		person.registerWindow(this);
		initComps();
		setRightPanel();
		addPanelRight(pnlRight);
		addPanelBot(sldRaise);
		setEnabledComps(false);
	}
	
	private void initComps(){
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.PAGE_AXIS));
		
		sldRaise = new JSlider(JSlider.HORIZONTAL, 0, 3000, 0);
		sldRaise.setPreferredSize(dimComp);
		sldRaise.setOpaque(false);
		sldRaise.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				setBtnRaiseChips();
			}
		});
		
		btnCheck.setPreferredSize(dimComp);
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				person.check();
				person.stop();
			}
		});

		btnFold.setPreferredSize(dimComp);
		btnFold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				person.fold();
				person.stop();
			}
		});

		btnRaise.setPreferredSize(dimComp);
		btnRaise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				person.raise(sldRaise.getValue());
				person.stop();
			}
		});

		btnCall.setPreferredSize(dimComp);
		btnCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				person.call(person.getMax());
				person.stop();
			}
		});
	}

	private void setBtnRaiseChips(){
		btnRaise.setText("<html><body>Raise<br>(" + sldRaise.getValue() + ")</body></html>");
	}
	
	private void setBtnCallChips(double chips){
		btnCall.setText("<html><body>Call<br>(" + chips + "$)</body></html>");
	}
	
	public void go(){
		setEnabledComps(true);
		filterComps();
	}
	
	private void filterComps(){
		if (person.getMax() == 0){
			btnCall.setEnabled(false);
		} else {
			btnCheck.setEnabled(false);
			setBtnCallChips(person.getMax());
		}
		setIntervalSlider((int) person.getMax(), (int) person.getChips());
	}
	
	private void setIntervalSlider(int min, int max){
		sldRaise.setMinimum(min);
		sldRaise.setMaximum(max);
	}
	
	public void setEnabledComps(boolean yes){
		btnCall.setEnabled(yes);
		btnCheck.setEnabled(yes);
		btnFold.setEnabled(yes);
		btnRaise.setEnabled(yes);
	}
	
	private void setRightPanel(){
		pnlRight.add(btnCheck);
		pnlRight.add(btnFold);
		pnlRight.add(btnCall);
		pnlRight.add(btnRaise);
	}
}
