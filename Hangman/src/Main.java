package org.app.utils;

import javax.swing.*;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {
  public static void main(String[] args) throws SQLException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    GUI gui = new GUI();
    gui.setTitle("Log In");
    gui.setVisible(true);
    gui.setBounds(10,10,400,320);
    gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
    gui.setLocationRelativeTo(null);
    gui.setResizable(false);
  }
}

