package view;

import game.GameEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.Value;

public class MenuPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel = new JPanel();
	
	private int frameWidth = 200, frameHeight = 300, menuWidth = frameWidth;
	private int nmrPlayers = 9;
	
	private String title = "Poker Casino";
	
	private Color clBackground = Color.GRAY;
	
	private JButton btnNewGame = new JButton("New Game");

	private ArrayList<JComboBox<String>> listPlayers = new ArrayList<>();
	
	private Dimension compDim = new Dimension(150, 50);
	
	public MenuPanel(){
		initFrame();
		initPanel();
		initComponents();
		addComponents();
	}
	
	private void initPanel(){
		mainPanel.setPreferredSize(new Dimension(menuWidth, frameHeight));
		mainPanel.setBackground(clBackground);
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	}
	
	private void initComponents(){
		setBtnNewGame();
		setListPlayers();
	}
	
	private void setListPlayers(){
		for (int i = 0; i < nmrPlayers; i++){
			listPlayers.add(new JComboBox<String>(Value.players));
//			if ((i == 2) || (i == 3) || (i > 6))
//				listPlayers.get(i).setSelectedIndex(2);
			if ((i == 3) || (i == 6))
				listPlayers.get(i).setSelectedIndex(2);
		}
	}
	
	private void setBtnNewGame(){
		btnNewGame.setPreferredSize(compDim);
		btnNewGame.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				Value.init();
				new GameEngine(getPlayers());
			}
		});
	}
	
	private String[] getPlayers(){
		String[] res = new String[nmrPlayers];
		for (int i = 0; i < nmrPlayers; i++){
			res[i] = listPlayers.get(i).getSelectedItem().toString();
		}
		return res;
	}
	
	private void addComponents(){
		mainPanel.add(btnNewGame);
		for (JComboBox<String> combo: listPlayers){
			mainPanel.add(combo);
		}
		add(mainPanel, BorderLayout.CENTER);
		pack();
	}
	

	private void initFrame(){
		setTitle(title);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(frameWidth, frameHeight));
		setLocation(50, screenSize.height/2 - frameHeight/2);
//		setLocation(screenSize.width/2 - frameWidth/2, screenSize.height/2 - frameHeight/2);
		setLayout(new BorderLayout());
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
