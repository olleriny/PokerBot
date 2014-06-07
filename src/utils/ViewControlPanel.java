package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.ViewEngine;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ViewControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ViewEngine viewEngine;

	private JButton btnStop = new JButton("Stop");
	private JButton btnStart = new JButton("Start");

	private JCheckBox chckSpeed = new JCheckBox("fast game");
	private JCheckBox chckGameView = new JCheckBox("view");
	private JCheckBox chckOutputView = new JCheckBox("output");
	private JCheckBox chckStats = new JCheckBox("stats");
	
	private JLabel rounds = new JLabel("games: 0");
	
	private Dimension compDim = new Dimension(100, 30);
	
	public ViewControlPanel(ViewEngine ve) {
		this.viewEngine = ve;
		init();
		initComps();
		addComps();
	}

	private void init(){
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
	}
	
	private void initComps(){
		btnStop.setPreferredSize(compDim);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewEngine.stopGame();
			}
		});
		
		btnStart.setPreferredSize(compDim);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewEngine.continueGame();
			}
		});

		chckGameView.setSelected(true);
		chckGameView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewEngine.setGameViewVisible(chckGameView.isSelected());
			}
		});

		chckOutputView.setSelected(true);
		chckOutputView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewEngine.setOutputViewVisible(chckOutputView.isSelected());
			}
		});
		
		chckSpeed.setSelected(true);
		chckSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckSpeed.isSelected())
					viewEngine.setSpeed(0);
				else
					viewEngine.setSpeed(1500);
			}
		});
		
		chckStats.setSelected(true);
		chckStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewEngine.setStatsViewVisible(chckStats.isSelected());
			}
		});
	}
	
	private void addComps(){
		add(rounds);
		add(btnStart);
		add(btnStop);
		add(chckSpeed);
		add(chckGameView);	
		add(chckOutputView);
		add(chckStats);
		setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
	}

	public void setRound(int i){
		rounds.setText("games: " + i);
	}
}
