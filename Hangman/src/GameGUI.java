package org.app.utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GameGUI extends JFrame implements ActionListener {
  Container container = getContentPane();
  String[] imagePaths = new String[11];
  ImageIcon imageIcon;
  JLabel imageLabel = new JLabel();
  JLabel triesLabel = new JLabel("Versuche:");
  JLabel triesCountLabel = new JLabel("10/10");
  JLabel searchedWordLabel = new JLabel("");
  Model model;
  JMenuBar menuBar = new JMenuBar();
  JMenu menu = new JMenu("Menü");
  JMenuItem statisticsMenuItem = new JMenuItem("Statistiken");
  JMenuItem deleteAccountMenuItem = new JMenuItem("Account löschen");
  JMenuItem addWordMenuItem = new JMenuItem("Wort hinzufügen");
  JMenuItem removeWordItem = new JMenuItem("Wort löschen (admin)");
  JTextField guessedWordTextField = new JTextField("");
  SQLiteConnection sqLiteConnection;
  boolean hasAdminPermission;
  boolean gameIsRunning;
  int tries;
  ArrayList<Character> finalWordChar;
  char[] charList;


  public void setSpieler(Spieler spieler) {
    this.spieler = spieler;
  }

  Spieler spieler;
  private void initImagePaths(){
    for(int i = 0; i < 10; i++){
      imagePaths[i] = "Hangman/Images/Progress" + (i) + ".png";
    }
  }

  public GameGUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
    printtln("Main GUI is being created");
    tries = 10;
    hasAdminPermission = false;
    initImagePaths();
    model = new Model(this);

    setupImage();
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();
    setCurrentImage();

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    printtln("Main GUI is fully configured and running the game now");
    runGame();
  }
  public void runGame() throws SQLException {
    printtln("Loop game started");
    setCurrentImage();
    triesCountLabel.setText(tries + "/10");
    guessedWordTextField.setText("");
    gameIsRunning = true;
    model.initWords();

    // Set up the placeholder text in the label
    StringBuilder placeHolder;
    placeHolder = new StringBuilder();
    placeHolder.append("_".repeat(Math.max(0, model.getChosenWord().length() - 1)));
    placeHolder.append("_");
    String placeHolderResult = placeHolder.toString();
    searchedWordLabel.setText(placeHolderResult);
    setSearchedWordLabel();
    finalWordChar = new ArrayList<>();
    charList = new char[model.getSiegwort().length()];
    Arrays.fill(charList, '_');
    printtln("A word is chosen and ready for guessing");
  }

  public void setupImage(){
    // Setting up the Image and its location
    imageIcon = new ImageIcon(imagePaths[0]);
    imageLabel.setIcon(imageIcon);
    imageLabel.setBounds(90,40,200,100);
    imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
  }

  public void setCurrentImage(){
    imageIcon = new ImageIcon(imagePaths[Math.abs(this.tries-10)]);
    imageLabel.setIcon(imageIcon);
    printtln("Image ist being updated to stage " + Math.abs(this.tries-10));
  }
  public void setLayoutManager(){
    container.setLayout(null);
  }

  public void setLocationAndSize(){
    setupImage();
    triesLabel.setBounds(270, 250, 70, 10);
    triesCountLabel.setBounds(275, 270, 100, 50);
    triesCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
    guessedWordTextField.setBounds(30, 250, 100, 50);
    guessedWordTextField.setFont(new Font("Arial", Font.BOLD, 15));
    guessedWordTextField.setBorder(
            BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    "Texteingabe: "));
  }

  public void setSearchedWordLabel(){
    searchedWordLabel.setBounds(90,170,300,40);
    searchedWordLabel.setFont(new Font("Arial", Font.BOLD, 20));
  }
  public void addComponentsToContainer(){
    container.add(imageLabel);
    container.add(triesLabel);
    container.add(triesCountLabel);
    container.add(searchedWordLabel);
    container.add(guessedWordTextField);
    // Upper menu components
    menu.add(statisticsMenuItem);
    menu.add(deleteAccountMenuItem);
    menu.add(addWordMenuItem);
    menu.add(removeWordItem);
    menuBar.add(menu);
    setJMenuBar(menuBar);
    printtln("All components added to the container");
  }
  public void addActionEvent(){
    removeWordItem.addActionListener(this);
    addWordMenuItem.addActionListener(this);
    deleteAccountMenuItem.addActionListener(this);
    statisticsMenuItem.addActionListener(this);
    guessedWordTextField.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e){
    printtln("Action performed: " + e.getActionCommand());

    // If the player wants to delete his account
    if(e.getSource().equals(deleteAccountMenuItem)){
      deleteAccount();
    }


    // If the player presses Enter
    if(e.getSource().equals(guessedWordTextField)){

      sqLiteConnection = new SQLiteConnection();
      try{
        System.out.println("\u001B[31m" + "Cheating: " + model.getSiegwort() + "\u001B[0m");

        // if the written word equals the searched word
        if(guessedWordTextField.getText().equalsIgnoreCase(model.getSiegwort())){
          this.searchedWordLabel.setText(model.getSiegwort());
          JOptionPane.showMessageDialog(this, "Du hast das Wort erraten!");
          tries = 10;
          incrementWins();
          runGame();

        }
        // If a char matches
        else if(model.inheritsChar(guessedWordTextField) &&
                guessedWordTextField.getText().length() == 1){
          char c = guessedWordTextField.getText().toLowerCase().charAt(0);
          for(int i = 0; i < charList.length; i++){
            char a = model.getSiegwort().toLowerCase().charAt(i);
            if(c == a){
              charList[i] = c;
              printtln("Character matches at position " + i);
            }
          }

          StringBuilder toSetText = new StringBuilder();
          int counter = 0;
          for(int i = 0; i < model.getSiegwort().length(); i++){
            if(charList[i] != '_'){
              toSetText.append(model.getSiegwort().charAt(i));
              counter++; }
            else toSetText.append("_");
          }
          searchedWordLabel.setText(toSetText.toString());
          printtln("The displayed text has been updated");
          printtln("It is checked if the word matches with the searched so far");
          if(counter == model.getSiegwort().length())
          {
            printtln("The word matches!");
            JOptionPane.showMessageDialog(this, "Du hast das Wort erraten!");
            printtln("Incrementing wins and running the game");
            tries = 10;
            incrementWins();
            runGame();
          }
        }
        else if(guessedWordTextField.getText().length() == 0){return;}
        else{
          initFail();
        }

      }
      catch (Exception exception){
        exception.printStackTrace();
      }



      guessedWordTextField.setText("");
    }

    // Show statistics of the player
    if(e.getSource().equals(statisticsMenuItem)){
      sqLiteConnection = new SQLiteConnection();
      showStatistics();
    }
    // Add word menu
    if(e.getSource().equals(addWordMenuItem)){

      sqLiteConnection = new SQLiteConnection();
      showAddWord();

    }
    // Remove a word from the database
    if(e.getSource().equals(removeWordItem)){
      sqLiteConnection = new SQLiteConnection();
      removeWord();
      sqLiteConnection.closeConnection();
    }
    sqLiteConnection.closeConnection();
  }

  public void deleteAccount(){
    JPasswordField passwordField = new JPasswordField();
    int confirmWindow = JOptionPane.showConfirmDialog(null, passwordField, "Bitte gib dein Passwort ein...",
            JOptionPane.OK_CANCEL_OPTION);
    StringBuilder pswd = new StringBuilder();
    for(int i = 0; i < passwordField.getPassword().length; i++){
      pswd.append(passwordField.getPassword()[i]);
    }
    String confirm = pswd.toString();
    sqLiteConnection = new SQLiteConnection();

    if(confirmWindow == JOptionPane.OK_OPTION){
      if(sqLiteConnection.passwordIsConfirmed(spieler.getUsername(), confirm)){
        String secondConfirm = JOptionPane.showInputDialog("Schreibe zur Bestätigung bitte deinen Usernamen ('"
                + spieler.getUsername() + "') voll aus...");
        if(Objects.equals(secondConfirm, spieler.getUsername())){
          new SQLiteConnection().deleteUser(spieler.getUsername());
          JOptionPane.showMessageDialog(this, "Dein Account wurde gelöscht.");
          sqLiteConnection.closeConnection();
          System.exit(0);
        }
        else{
          JOptionPane.showMessageDialog(this, "Der Name stimmt nicht überein.");
          deleteAccount();
        }
      }
      else{
        JOptionPane.showMessageDialog(this, "Das Passwort stimmt nicht überein.");
        deleteAccount();
      }
    }
    sqLiteConnection.closeConnection();

  }

  public void initFail(){
    printtln("The user tipped a wrong character");
    if (this.tries >= 2){
      printtln("Decrementing tries and updating the image");
      this.tries -= 1;
      triesCountLabel.setText(tries + "/10");
      setCurrentImage();
    }
    else{
      initTotalFail();
    }
  }

  public void initTotalFail(){
    tries--;
    printtln("The user lost with the last try. Setting up the default game again");
    setCurrentImage();
    incrementGamesPlayed();
    try{
      JOptionPane.showMessageDialog(this, "Du hast verloren! Das Wort war: " + model.getSiegwort());
      new SQLiteConnection().incrementLosses(spieler.getUsername());
      tries = 10;
      triesCountLabel.setText(tries + "/10");
      runGame();
    }
    catch (SQLException e){
      e.printStackTrace();
    }
  }

  public void incrementGamesPlayed(){
    try{
      new SQLiteConnection().incrementGamesPlayed(spieler.getUsername());
    }
    catch (SQLException e){
      e.printStackTrace();
    }
  }
  public void incrementWins(){
    incrementGamesPlayed();
    try{
      new SQLiteConnection().incrementWins(spieler.getUsername());
      // sqLiteConnection.incrementWins(spieler.getUsername());

    }
  catch (SQLException e){
      e.printStackTrace();
  }
  }
  // Appears if the user had the admin password
  public void removeWordPermission(){
    printtln("Remove words Window with admin permission");
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
    printtln("Menu for adding words");
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
    sqLiteConnection.closeConnection();
  }
  public void showStatistics(){
    String stats;
    printtln("Stats will be taken from the DB and displayed");
    String username = GUI.getSpieler().getUsername();
    stats = "Deine Statistiken:\n" +
            "• Name: " + username + "\n"+
            "• Spiele insgesamt: " + sqLiteConnection.getGamesPlayed(username) + "\n" +
            "• Spiele gewonnen: "  + sqLiteConnection.getWins(username) + "\n" +
            "• Spiele verloren: "  + sqLiteConnection.getLosses(username) + "\n"+
            "• Siegesquote: "      + ((int) (((double) sqLiteConnection.getWins(username)/
            (sqLiteConnection.getWins(username) +
                    sqLiteConnection.getLosses(username))*100)*100))/100d + "%";
    JOptionPane.showMessageDialog(this, stats);
  }
  public void createPasswordGUI(){
    printtln("Password GUI will be created");
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

  public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
    GameGUI gameGUI = new GameGUI();
    gameGUI.setTitle("Hangman");
    gameGUI.setVisible(true);
    gameGUI.setBounds(10,10,400,420);
    gameGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    gameGUI.setLocationRelativeTo(null);
    gameGUI.setResizable(false);
  }

  private static void printtln(String defaultText){
    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    String formattedDate = localDateTime.format(dateTimeFormatter);
    System.out.println("\u001B[32m" + "DB[" + formattedDate + "] " + defaultText + "\u001B[0m");
  }
}
