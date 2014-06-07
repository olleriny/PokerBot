package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import utils.ListOfPlayers;
import utils.Value;
import utils.ViewControlPanel;
import utils.Value.state;
import view.OutputPanel;
import view.GamePanel;
import view.StatisticsPanel;

public class ViewEngine extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private int frameWidth = 700, frameHeight = 550, tabWidth = frameWidth - 100, tabHeight = frameHeight - 100;
	
	private String title = "Game";
	
	private JTabbedPane tbPane = new JTabbedPane();

	private static ImageIcon[] icons = new ImageIcon[Value.suit.values().length * Value.value.values().length];
	private static ImageIcon emptyIcon = new ImageIcon();
	
	private GameEngine gameEngine;
	
	private GamePanel gamePanel;
	private OutputPanel outputPanel;
	private StatisticsPanel statsPanel;
	private ViewControlPanel gameControlPanel;
	
	public ViewEngine(GameEngine gameEngine, ListOfPlayers players){
		this.gameEngine = gameEngine;
		initFrame();
		initComponents(players);
		addComponents();
		createAllCardIcons();
	}
	
	public static ImageIcon getIcon(int hash){
		return icons[hash];
	}
	
	public static ImageIcon getEmptyIcon(){
		return emptyIcon;
	}
	
	private static void createAllCardIcons(){
		for (int i = 0; i < icons.length; i++){
			icons[i] = new ImageIcon(concatenate(i, "img/"));
		}
		emptyIcon = new ImageIcon("img/empty.png");
	}
	
	private static Image concatenate(int hash, String path){
		BufferedImage img1, img2, img = null;
		int height = 16;
		try {
			img1 = ImageIO.read(new File(path + Value.getCardSuit(hash).toString() + ".png"));
			img2 = ImageIO.read(new File(path + Value.getCardValue(hash).toString() + ".png"));
			int widthImg1 = img1.getWidth();
			int heightImg1 = img1.getHeight();
			int widthImg2 = img2.getWidth();
			img = new BufferedImage(widthImg1 + widthImg2, heightImg1 + height, BufferedImage.TYPE_INT_ARGB);
			img.getGraphics().setColor(Color.WHITE);
			img.getGraphics().fillRect(0, 0, widthImg2 + widthImg1, heightImg1 + height);
			img.createGraphics().drawImage(img1, 0, height/2, null);
			img.createGraphics().drawImage(img2, widthImg1, height/2, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
}
	
	private void initComponents(ListOfPlayers pls){
		gameControlPanel = new ViewControlPanel(this);
		gamePanel = new GamePanel(gameEngine, pls);
		outputPanel = new OutputPanel();
		statsPanel = new StatisticsPanel();
		tbPane.setPreferredSize(new Dimension(tabWidth, tabHeight));
		addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	        	outputPanel.end();
	        	statsPanel.end();
	        	gameEngine.endGame();
	        }
	    });
	}
	
	private void addComponents(){
		tbPane.addTab("Game", gamePanel);
		tbPane.addTab("Console", outputPanel);
		tbPane.addTab("Statistics", statsPanel);
		
		add(tbPane, BorderLayout.CENTER);
		add(gameControlPanel, BorderLayout.NORTH);
		pack();
	}
	
	private void initFrame(){
		setTitle(title);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(frameWidth, frameHeight));
		setLocation(50, screenSize.height/2 - frameHeight/2);
		setLocation(screenSize.width/2 - frameWidth/2, screenSize.height/2 - frameHeight/2);
		setLayout(new BorderLayout(0, 10));
		setVisible(true);
		setResizable(false);
	}
	
	public void isOnMove(int ID){
		gamePanel.isOnMove(ID);
	}
	
	public void setSpeed(int speed){
		gameEngine.setSpeed(speed);
	}
	
	public void setGameViewVisible(boolean yes){
		gamePanel.canRun(yes);
	}
	
	public void setOutputViewVisible(boolean yes){
		outputPanel.canRun(yes);
	}
	
	public void setStatsViewVisible(boolean yes){
		statsPanel.canRun(yes);
	}
	
	public void setRounds(int i){
		gameControlPanel.setRound(i);
	}
	
	public void stopGame(){
		gameEngine.pauseGame();
	}	
	
	public void continueGame(){
		gameEngine.continueGame();
	}
	
	public void addStats(Integer c1, Integer c2){
		statsPanel.statsCards(c1, c2);
	}
	
	public void newRound(boolean cards){
		gamePanel.newRound(cards);
	}
	
	public void setFlop(Integer c1, Integer c2, Integer c3){
		gamePanel.setFlop(c1, c2, c3);
	}
	
	public void setTurn(Integer c){
		gamePanel.setTurn(c);
	}
	
	public void setRiver(Integer c){
		gamePanel.setRiver(c);
	}
	
	public void dealCard(int ID, Integer c1, Integer c2){
		outputPanel.dealedCard(ID, c1, c2);
		gamePanel.dealedCards(ID);
	}
	
	public void setTxtLog(String s){
//		outputPanel.addToLog(s);
		gamePanel.setTxtLog(s);
	}
	
	public void plLeft(int ID){
		gamePanel.plLeft(ID);
	}

	//****** player's actions *****//
	
	public void setAction(int ID, state state, double chips){
		outputPanel.setAction(ID, state, chips);
		gamePanel.setAction(ID, state, chips);
	}
}
