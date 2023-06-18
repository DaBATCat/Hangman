package org.app.utils;

import javax.swing.*;
import java.awt.*;

public class GameGUI extends JFrame{
  Container container = getContentPane();
  String[] imagePaths = new String[10];
  ImageIcon imageIcon;
  JLabel imageLabel = new JLabel();
  JLabel triesLabel = new JLabel("Versuche:");
  JLabel triesCountLabel = new JLabel("10/10");
  JLabel searchedWordLabel = new JLabel("");
  Model model = new Model();
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
    // addActionEvent();

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
  }
  public void initWords(){
    //TODO: Jlabel searchedWordLabel in central display, JMenu for stats & options
  }
}
class Test2{
  public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    GameGUI gameGUI = new GameGUI();
    gameGUI.setTitle("Hangman");
    gameGUI.setVisible(true);
    gameGUI.setBounds(10,10,400,400);
    gameGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    gameGUI.setLocationRelativeTo(null);
    gameGUI.setResizable(false);
  }
}