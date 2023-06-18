package org.app.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GUI extends JFrame implements ActionListener, GUIinterface{

  SQLiteConnection sqLiteConnection;

  Container container = getContentPane();
  JLabel usernameLabel = new JLabel("Username");
  JLabel passwordLabel = new JLabel("Password");
  JTextField usernameTextField = new JTextField();
  JPasswordField passwordField = new JPasswordField();
  JButton loginButton = new JButton("Anmelden");
  JButton registerButton = new JButton("Registrieren");
  JButton registerFinalButton = new JButton("Registrieren");
  JCheckBox showPassword = new JCheckBox("Passwort anzeigen");

  public Spieler getSpieler() {
    return spieler;
  }

  public void setSpieler(Spieler spieler) {
    this.spieler = spieler;
  }

  Spieler spieler;
  public GUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }
  public void setLayoutManager(){
    container.setLayout(null);
  }
  public void setLocationAndSize() {
    usernameLabel.setBounds(50,50,100,23);
    passwordLabel.setBounds(50,80,100,23);
    usernameTextField.setBounds(150,50,150,23);
    passwordField.setBounds(150,80,150,23);
    showPassword.setBounds(150,110,150,30);
    loginButton.setBounds(50,200,120,30);
    registerButton.setBounds(180,200,120,30);
    registerFinalButton.setBounds(50,200,120,30);
    registerFinalButton.setVisible(false);
    passwordField.setEchoChar('*');
  }
  public void addComponentsToContainer(){
    container.add(usernameLabel);
    container.add(passwordLabel);
    container.add(usernameTextField);
    container.add(passwordField);
    container.add(showPassword);
    container.add(loginButton);
    container.add(registerButton);
    container.add(registerFinalButton);
  }
  public void addActionEvent(){
    loginButton.addActionListener(this);
    registerButton.addActionListener(this);
    showPassword.addActionListener(this);
    registerFinalButton.addActionListener(this);
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    sqLiteConnection = new SQLiteConnection();
    String usernameText;
    StringBuilder passwordText;
    String passwordTextStr;
    usernameText = usernameTextField.getText();
    char[] passwordTextChars = passwordField.getPassword();
    passwordText = new StringBuilder();
    for (char s :
            passwordTextChars) {
      passwordText.append(s);
    }

    if (e.getSource().equals(loginButton)) {

      try {
        // If the login is successful
        if(sqLiteConnection.userIsRegistered(usernameText) && sqLiteConnection.passwordEqualsWithUsername(usernameText, passwordText.toString())){
          dispose();
          runGame();
        }
        else{
          JOptionPane.showMessageDialog(this, "Username oder Passwort ist falsch");
        }
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    }

    // if the checkmark of showPassword has been changed
    if(e.getSource().equals(showPassword)){
      if(showPassword.isSelected()) passwordField.setEchoChar((char)0);
      else passwordField.setEchoChar('*');
    }

    // First step of adding a new user, if the user first clicks the Register-Button
    if(e.getSource().equals(registerButton)){
      registerButton.setVisible(false);
      // JOptionPane.showMessageDialog(this, "Gib hier deine neuen Anmeldedaten ein");
      setTitle("Registrieren");
      loginButton.addActionListener(this);
      // Hide the buttons and add the new register button for the next case of the ActionListener
      registerFinalButton.setVisible(true);
      loginButton.setVisible(false);
      usernameTextField.setText("");
      passwordField.setText("");

    }
    // Final step of creating a new user
    if(e.getSource().equals(registerFinalButton)){
      System.out.println(e.getActionCommand());
      if(!sqLiteConnection.userIsRegistered(usernameText)){
        spieler = new Spieler(usernameText, passwordText.toString());
        sqLiteConnection.printTable(SQLiteConnection.Table.MANAGEMENT);
        dispose();
        runGame();
      }
      else{
        JOptionPane.showMessageDialog(this, "Dieser Username ist bereits vergeben.");
      }
    }
  }

  // Outdated ActionListener
  @Override
  public void onRegistered(ActionListener e){
    System.out.println("Button pressed, after");
  }

public void runGame() {
    try{
      GameGUI gameGUI = new GameGUI();
      gameGUI.setTitle("Hangman");
      gameGUI.setVisible(true);
      gameGUI.setBounds(10,10,400,300);
      gameGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      gameGUI.setLocationRelativeTo(null);
      gameGUI.setResizable(false);
    }
    catch (Exception e){
      e.printStackTrace();
    }
}

}



class Test{
  public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    GUI gui = new GUI();
    gui.setTitle("Log In");
    gui.setVisible(true);
    gui.setBounds(10,10,400,320);
    gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
    gui.setLocationRelativeTo(null);
    gui.setResizable(false);
  }

}