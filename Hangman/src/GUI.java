package org.app.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.sql.SQLException;
import java.util.Objects;

public class GUI extends JFrame implements ActionListener{
  public static Image icon = Toolkit.getDefaultToolkit().getImage("Hangman/ImageIcon.png");

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
  Model model;

  public static Spieler getSpieler() {
    return spieler;
  }

  static Spieler spieler;
  public GUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();

    setIconImage(icon);
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
    String usernameText;
    StringBuilder passwordText;
    usernameText = usernameTextField.getText();
    char[] passwordTextChars = passwordField.getPassword();
    passwordText = new StringBuilder();
    for (char s :
            passwordTextChars) {
      passwordText.append(s);
    }

    if (e.getSource().equals(loginButton)) {
      sqLiteConnection = new SQLiteConnection();
      if(Objects.equals(usernameText, "Shrek")){
        startRickroll();
        System.exit(0);
      }
      try {
        // If the login is successful
        if(sqLiteConnection.userIsRegistered(usernameText) && sqLiteConnection.passwordEqualsWithUsername(usernameText, passwordText.toString())){
          spieler = new Spieler(usernameText, passwordText.toString(),0,0);
          dispose();
          runGame();
          sqLiteConnection.closeConnection();
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
    if(e.getSource().equals(registerFinalButton)) {
      sqLiteConnection = new SQLiteConnection();
      System.out.println(e.getActionCommand());
      // Check if the user is not registered in the database and
      // usernameText & passwordText have a higher length than 1 and
      // check if the password is secure
      if (!sqLiteConnection.userIsRegistered(usernameText) && usernameText.length() >= 1
              && passwordText.toString().length() >= 1
              && PasswordSecurityChecker.passwordIsSecure(passwordText.toString())) {
        spieler = new Spieler(usernameText, passwordText.toString(),0,0);
        dispose();
        runGame();
        sqLiteConnection.closeConnection();

      }
      else if(usernameText.length() == 0){
        JOptionPane.showMessageDialog(this, "Dein Benutzername muss mindestens 1 Character lang sein.");
      }
      else if(passwordText.toString().length() == 0){
        JOptionPane.showMessageDialog(this, "Dein Passwort ist zu kurz.");
      }
      else if( !PasswordSecurityChecker.passwordIsSecure(passwordText.toString())){
        JOptionPane.showMessageDialog(this, String.format("Dein Passwort ist zu unsicher. Bitte verwende mindestens 5 " +
                "Zeichen, mindestens einen Groß- und Kleinbuchstaben, eine Zahl sowie " +
                "mindestens eins dieser Sonderzeichen: [ %s ]", PasswordSecurityChecker.allowedChars()));
      }
      else{
        JOptionPane.showMessageDialog(this, "Dieser Username ist bereits vergeben.");
      }
    }


    }

  public void startRickroll(){
    try{
      Desktop desktop = Desktop.getDesktop();
      desktop.browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

public void runGame() {
    try{
      GameGUI gameGUI = new GameGUI();
      gameGUI.setIconImage(icon);
      gameGUI.setTitle("Hangman");
      gameGUI.setVisible(true);
      gameGUI.setBounds(10,10,400,420);
      gameGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      gameGUI.setLocationRelativeTo(null);
      gameGUI.setResizable(false);
      gameGUI.setSpieler(spieler);
    }
    catch (Exception e){
      e.printStackTrace();
    }
}

}


