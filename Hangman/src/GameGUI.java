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
  JMenuItem addWordMenuItem = new JMenuItem("Wort hinzufügen");
  JMenuItem removeWordItem = new JMenuItem("Wort löschen (admin)");
  // TODO: logoutMennuItem
  SQLiteConnection sqLiteConnection;
  boolean hasAdminPermission;


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
    hasAdminPermission = false;
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
    menu.add(addWordMenuItem);
    menu.add(removeWordItem);
    menuBar.add(menu);
    setJMenuBar(menuBar);
  }
  public void addActionEvent(){
    removeWordItem.addActionListener(this);
    addWordMenuItem.addActionListener(this);
    deleteAccountMenuItem.addActionListener(this);
    statisticsMenuItem.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e){
    System.out.println(e.getActionCommand());
    sqLiteConnection = new SQLiteConnection();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }

    // Show statistics of the player
    if(e.getSource().equals(statisticsMenuItem)){
      showStatistics();
    }
    // Add word menu
    if(e.getSource().equals(addWordMenuItem)){
      showAddWord();
    }
    // Remove a word from the database
    if(e.getSource().equals(removeWordItem)){
      removeWord();
    }
  }

  // Appears if the user had the admin password
  public void removeWordPermission(){
    hasAdminPermission = true;
    String removedWantedWord = JOptionPane.showInputDialog("Gib hier ein Wort ein, das gelöscht werden soll:");
    // Check, if the given word is in the database
    if(removedWantedWord != null && sqLiteConnection.wordIsInTable(removedWantedWord)) {
      // remove the word
      sqLiteConnection.removeWord(removedWantedWord);
      JOptionPane.showMessageDialog(this, removedWantedWord + " wurde entfernt.");
      removeWordPermission();
    }
    else if (removedWantedWord != null){
      JOptionPane.showMessageDialog(this, removedWantedWord + " wurde nicht gefunden.");
      removeWordPermission();
    }
  }

  public void removeWord(){
    if(!hasAdminPermission) createPasswordGUI();
    else removeWordPermission();
  }
  public void showAddWord(){
    String suggestedWord = JOptionPane.showInputDialog("Gib hier ein neues Wort ein:");
    // If the suggested word has minimum of 2 chars
    // and the word is not already defined in the database
    if(suggestedWord != null && suggestedWord.length() >= 2
            && !sqLiteConnection.wordIsInTable(suggestedWord)){
      sqLiteConnection.addWord(suggestedWord);
      JOptionPane.showMessageDialog(this, String.format("\"%s\" wurde hinzugefügt.", suggestedWord));
      showAddWord();
    }
    else if(suggestedWord != null && suggestedWord.length() <= 1 ){
      JOptionPane.showMessageDialog(this, "Das Wort muss mindestens 2 Zeichen lang sein.");
      showAddWord();
    }
    else if(suggestedWord != null)
    {
      JOptionPane.showMessageDialog(this, "Dieses Wort wurde bereits vergeben.");
      showAddWord();
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
  public void createPasswordGUI(){
    try{
      PasswordGUI passwordGUI = new PasswordGUI(this);
      passwordGUI.setTitle("Passwort erforderlich");
      passwordGUI.setVisible(true);
      passwordGUI.setBounds(10,10,155,140);
      passwordGUI.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      passwordGUI.setLocationRelativeTo(null);
      passwordGUI.setResizable(false);
    }
    catch (Exception e){
      e.printStackTrace();
    }
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
