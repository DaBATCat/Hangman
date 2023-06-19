package org.app.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame implements ActionListener {
  Container container = getContentPane();
  String[] imagePaths = new String[10];
  ImageIcon imageIcon;
  JLabel imageLabel = new JLabel();
  JLabel triesLabel = new JLabel("Versuche:");
  JLabel triesCountLabel = new JLabel("10/10");
  JLabel searchedWordLabel = new JLabel("");
  Model model = new Model();
  JMenuBar menuBar = new JMenuBar();
  JMenu menu = new JMenu("Menü");
  JMenuItem statisticsMenuItem = new JMenuItem("Statistiken");
  JMenuItem deleteAccountMenuItem = new JMenuItem("Account löschen");
  SQLiteConnection sqLiteConnection;

  public void setSpieler(Spieler spieler) {
    this.spieler = spieler;
  }

  Spieler spieler;
  private void initImagePaths(){
    for(int i = 0; i < 10; i++){
      imagePaths[i] = "Hangman/Images/Progress" + (i+1) + ".png";
    }
  }

  public GameGUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    initImagePaths();
    initWords();

    setupImage();
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }

  public void setupImage(){
    // Setting up the Image and its location
    imageIcon = new ImageIcon(imagePaths[0]);
    imageLabel.setIcon(imageIcon);
    imageLabel.setBounds(10,250,200,100);
    imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
  }
  public void setLayoutManager(){
    container.setLayout(null);
  }

  public void setLocationAndSize(){
    setupImage();
    triesLabel.setBounds(270, 250, 70, 10);
    triesCountLabel.setBounds(275, 270, 100, 50);
    triesCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
  }
  public void addComponentsToContainer(){
    container.add(imageLabel);
    container.add(triesLabel);
    container.add(triesCountLabel);
    // Upper menu components
    menu.add(statisticsMenuItem);
    menu.add(deleteAccountMenuItem);
    menuBar.add(menu);
    setJMenuBar(menuBar);
  }
  public void addActionEvent(){
    deleteAccountMenuItem.addActionListener(this);
    statisticsMenuItem.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e){
    System.out.println(e.getActionCommand());
    sqLiteConnection = new SQLiteConnection();

    // Show statistics of the player
    if(e.getSource().equals(statisticsMenuItem)){
      showStatistics();
    }
  }
  public void showStatistics(){
    String stats;
    String username = GUI.getSpieler().getUsername();
    stats = "Deine Statistiken:\n" +
            "• Name: " + username + "\n"+
            "• Spiele insgesamt: " + sqLiteConnection.getWins(username) + "\n" +
            "• Spiele gewonnen: " + sqLiteConnection.getWins(username) + "\n" +
            "• Spiele verloren: " + sqLiteConnection.getLosses(username) + "\n";
    JOptionPane.showMessageDialog(this, stats);
  }
  public void initWords(){
    //TODO: Jlabel searchedWordLabel in central display, JMenu for stats & options
  }

  public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    GameGUI gameGUI = new GameGUI();
    gameGUI.setTitle("Hangman");
    gameGUI.setVisible(true);
    gameGUI.setBounds(10,10,400,420);
    gameGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    gameGUI.setLocationRelativeTo(null);
    gameGUI.setResizable(false);
  }

}
