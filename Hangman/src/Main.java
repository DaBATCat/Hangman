package org.app.utils;

import java.sql.SQLException;

public class Main {
  public static void main(String[] args) throws SQLException {
    Model model = new Model();
    Spieler spieler1 = new Spieler("ja","nein",0,0);
    model.Game(GUI.getSpieler());
    System.out.println("siegesquote:"+spieler1.getSiegesquote()+"%");
    SQLiteConnection sqLiteConnection = new SQLiteConnection();
    for(int i = 0; i < sqLiteConnection.getWordsFromTable().size(); i++){
      System.out.println(sqLiteConnection.getWordsFromTable().get(i));
    }
  }
}

