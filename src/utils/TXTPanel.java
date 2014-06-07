package utils;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class TXTPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel pnlBot = new JPanel(new BorderLayout(20, 0));
	
	private JTextArea txtGame = new JTextArea();
	
	private JScrollPane scrGame = new JScrollPane(txtGame, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	private JButton btnSave = new JButton("Save");
	
	private JCheckBox chckImmediateSave = new JCheckBox("Immediate save");
	
	private JFileChooser flChooser = new JFileChooser("C:/SKOLA/dipl");
	
	private FileWriter fileWriter;
	private File file = null;
	
	private boolean canRun = true;
	private boolean writeToFile = false;
	
	public TXTPanel(){
		initFrame();
		initComponents();
		addComponents();
	}
	
	private void closeFile(){
		try {
			if (writeToFile)
				fileWriter.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void end(){
		closeFile();
	}
	
	public void canRun(boolean yes){
		canRun = yes;
	}
	
	private void initComponents(){
		scrGame.setViewportView(txtGame);
		txtGame.setEditable(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = flChooser.showSaveDialog(txtGame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (safeLog(flChooser.getSelectedFile()))
						JOptionPane.showMessageDialog(txtGame, "File was succesfully saved to\n" + flChooser.getSelectedFile().toString());
					else
						JOptionPane.showMessageDialog(txtGame, "File was NOT succesfully saved,\n" + "repeat the action.");
				}
			}
		});
		chckImmediateSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (chckImmediateSave.isSelected()){
						int returnVal = flChooser.showSaveDialog(txtGame);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							file = flChooser.getSelectedFile();
							fileWriter = new FileWriter(file);
							writeToFile = true;
							JOptionPane.showMessageDialog(txtGame, "Everything will be saved to file \n" + file.getCanonicalPath());
						} else {
							chckImmediateSave.setSelected(false);
						}
					} else {
						file = null;
						writeToFile = false;
						fileWriter.close();
					}
				} catch (Exception e) {}
			}
		});
	}
	
	private void addComponents(){
		pnlBot.add(btnSave, BorderLayout.CENTER);
		pnlBot.add(chckImmediateSave, BorderLayout.WEST);
		add(scrGame, BorderLayout.CENTER);
		add(pnlBot, BorderLayout.SOUTH);
	}
	
	private void initFrame(){
		setLayout(new BorderLayout());
		setVisible(true);
	}

	private boolean safeLog(File file){
		try {
			txtGame.write(new FileWriter(file));
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	protected void addLog(String s){
		if (canRun){
			if (writeToFile){
				try {
					fileWriter.write(s + "\n");
				} catch (IOException e) {
					writeToFile = false;
					e.printStackTrace();
					System.out.println("Dont worry, everything was saved..");
				}
			} else {
				txtGame.append(s + "\n");
				txtGame.setCaretPosition(txtGame.getDocument().getLength());
			}
		}
	}
}
