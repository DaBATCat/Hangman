package org.app.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordGUI extends JFrame implements ActionListener {
  Container container = getContentPane();
  JLabel adminPasswordLabel = new JLabel("Admin-Passwort:");
  JPasswordField passwordField = new JPasswordField();
  JButton button = new JButton("Weiter");
  public static String ADMINPASSWORD = "hugo";

  public void setGameGUI(GameGUI gameGUI) {
    this.gameGUI = gameGUI;
  }

  public GameGUI gameGUI;

  public PasswordGUI(GameGUI gameGUI){
    this.gameGUI = gameGUI;
    setLayoutManager();
    setLocationAndSize();
    addComponentsToContainer();
    addActionEvent();
  }
  public void addActionEvent(){
    button.addActionListener(this);
  }
  public void addComponentsToContainer(){
    container.add(adminPasswordLabel);
    container.add(passwordField);
    container.add(button);
  }
  public void setLocationAndSize(){
    adminPasswordLabel.setBounds(10,10,100,23);
    passwordField.setBounds(10,40,120,23);
    button.setBounds(10,70,120,23);
  }
  public void setLayoutManager(){
    container.setLayout(null);
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    // If the button is pressed, check if the password is right
    StringBuilder passwordText;
    char[] passwordTextChars = passwordField.getPassword();
    passwordText = new StringBuilder();
    for (char s :
            passwordTextChars) {
      passwordText.append(s);
    }
    // If the login is a success, go back to GameGUI and remove the word there
    if(e.getSource().equals(button)){
      if (passwordText.toString().equals(ADMINPASSWORD)) {
        dispose();
        gameGUI.removeWordPermission();
      }
      else{
        JOptionPane.showMessageDialog(this, "Passwort stimmt nicht überein.");
      }
    }
  }
}
