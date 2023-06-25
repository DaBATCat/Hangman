package org.app.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameGUI extends JFrame implements ActionListener {
  Container container = getContentPane();
  String[] imagePaths = new String[10];
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
  // TODO: logoutMennuItem
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
      imagePaths[i] = "Hangman/Images/Progress" + (i+1) + ".png";
    }
  }

  public GameGUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
    hasAdminPermission = false;
    initImagePaths();
    initWords();
    model = new Model(this);

    setupImage();
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    runGame();
  }
  public void runGame() throws SQLException {
    gameIsRunning = true;
    model.initWords();
    tries = 0;

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
    // searchedWordLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    // while(gameIsRunning){
    //   model.game(spieler, searchedWordLabel);
    // }
  }

  public void setupImage(){
    // Setting up the Image and its location
    imageIcon = new ImageIcon(imagePaths[0]);
    imageLabel.setIcon(imageIcon);
    imageLabel.setBounds(90,40,200,100);
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
    guessedWordTextField.setBounds(30, 250, 100, 50);
    guessedWordTextField.setFont(new Font("Arial", Font.BOLD, 15));
    guessedWordTextField.setBorder(
            BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    "Texteingabe: "));
  }

  public void setSearchedWordLabel(ArrayList<String> text){
    char[] characters = searchedWordLabel.getText().toCharArray();
    ArrayList<Character> chars = new ArrayList<>();
    for(int i = 0; i < characters.length; i++){
      chars.add(characters[i]);
    }
    // StringBuilder result = new StringBuilder();
    // for(int i = 0; i < model.getSiegwort().length(); i++){
    //   if(i > 0 && i % 2 != 0) result.append(" ");
    //   else if()
    // }
    for(int i = 0; i < model.getSiegwort().length(); i++){

    }
    System.out.println("TEST " + text);
    // String a = "";
    // for(int i = 0; i < text.size(); i++){
    //   if(text.get(i) != "_"){
    //     finalWordChar[i] = text.get(i).charAt(0);
    //   }
    //   a += text.get(i) + "_ ";
    // }
    // this.searchedWordLabel.setText(finalWordChar.toString());

    // for(int i = 0; i < tempWord.size(); i++){
    //   if(!Objects.equals(tempWord.get(i), " ")){
    //     toAddText.append(tempWord.get(i));
    //   }
    //   else if (Objects.equals(tempWord.get(i), "_")){
    //     toAddText.append(currentText.charAt(i) + " ");
    //   }
    //   else{
    //     toAddText.append(tempWord.get(i));
    //   }
    // }
  }
  public void setText(String text){

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
    System.out.println(e.getActionCommand());



    // If the player presses Enter
    if(e.getSource().equals(guessedWordTextField)){
      sqLiteConnection = new SQLiteConnection();
      try{
        System.out.println("Siegwort: " + model.getSiegwort());

        // if the written word equals the searched word
        if(guessedWordTextField.getText().equalsIgnoreCase(model.getSiegwort())){
          this.searchedWordLabel.setText(model.getSiegwort());
          sqLiteConnection.incrementWins(spieler.getUsername());
        }
        // If a char matches
        else if(model.inheritsChar(guessedWordTextField) &&
                guessedWordTextField.getText().length() == 1){
          char c = guessedWordTextField.getText().toLowerCase().charAt(0) ;
          for(int i = 0; i < charList.length; i++){
            char a = model.getSiegwort().toLowerCase().charAt(i);
            System.out.println("C: " + c + " A: " + a);
            if(c == a){
              charList[i] = c;
            }
            else{
              System.out.println("not true");
            }
          }

          String toSetText = "";
          int counter = 0;
          for(int i = 0; i < model.getSiegwort().length(); i++){
            if(charList[i] != '_'){
              toSetText += model.getSiegwort().charAt(i);
              counter++; }
            else toSetText += "_";
          }
          searchedWordLabel.setText(toSetText);
          if(counter == model.getSiegwort().length())
          {
            JOptionPane.showMessageDialog(this, "Du hast das Wort erraten!");
            incrementWins();
            runGame();
          }

          new GameGUI();
          // for(int i = 0; i < model.getSiegwort().length(); i++){
          //   if(guessedWordTextField.getText().charAt(0) == model.getSiegwort().charAt(0)){
          //     charList[i] = guessedWordTextField.toString();
          //   }
          // }
        }

      }
      catch (Exception exception){
        exception.printStackTrace();
      }
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

  public void incrementWins(){
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
    sqLiteConnection.closeConnection();
  }
  public void showStatistics(){
    String stats;
    String username = GUI.getSpieler().getUsername();
    stats = "Deine Statistiken:\n" +
            "• Name: " + username + "\n"+
            "• Spiele insgesamt: " + sqLiteConnection.getGamesPlayed(username) + "\n" +
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

  public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
    GameGUI gameGUI = new GameGUI();
    gameGUI.setTitle("Hangman");
    gameGUI.setVisible(true);
    gameGUI.setBounds(10,10,400,420);
    gameGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    gameGUI.setLocationRelativeTo(null);
    gameGUI.setResizable(false);
  }

}
