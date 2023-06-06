package org.app.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SQLiteConnection {
    String query = "select word from words";
    static Connection connection = null;
    static String url = "jdbc:sqlite:hangman.db";
    public static void main(String[] args) throws SQLException {
        // String[] a = getWordsFromTable();
    /*
        try {
      connection = DriverManager.getConnection(url);
      printtln("Connection successfull!");
      // executions here

      // new SQLiteConnection().executeStatements(connection, query, true) ;
        new SQLiteConnection().executeStatements(connection, new SQLiteConnection().query, false);
    }
    catch (SQLException e){
      e.printStackTrace();
    }
    */
        connection = DriverManager.getConnection(url);
        new SQLiteConnection().executeStatements(connection, new SQLiteConnection().query, false);
    assert connection != null;
    connection.close();
    printtln("Closed connection");
 }
 public static String[] getWordsFromTable(){
    try {
        connect(url);
        Statement statement = connection.createStatement();
        statement.executeUpdate("Select word from words");
        ResultSet resultSet = statement.executeQuery("Select word from words");
        String[] result = new String[5];
        for(int i = 1; i < 6; i++){
            // result[i] = resultSet.getString(1, i);
        }
        return result;
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
    throw new IllegalArgumentException();
 }

  // for default executions
  public void executeStatements(Connection connection, String query, boolean printTable) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
    printtln("Query executed! (" + query + ")");
    if(printTable)printTable(statement);
    else{
        ResultSet resultSet = statement.executeQuery(new SQLiteConnection().query);
        System.out.println(resultSet.getString(1));
    }
  }

  // called executions here
  //public void executions(Connection connection) throws SQLException {
  //  executeStatements(connection, "insert into ", true);
  //}
  //// called executions here
  //public void executions(Connection connection, String query) throws SQLException {
  //  executeStatements(connection, "insert into ", true);
  //}

  private void printTable(Statement statement) throws SQLException {
    ResultSet resultSet = statement.executeQuery(new SQLiteConnection().query);
    printtln("Printing Table:");
    while(resultSet.next()){
      System.out.println("ID\tword");
      System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2));
    }

  }

  private static void printtln(String defaultText) {
    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    String formattedDate = localDateTime.format(dateTimeFormatter);
    System.out.println("[" + formattedDate + "] " + defaultText);
  }

  /* Table name: words
   * attributes: (id#, word)
   */

    private static void connect(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        printtln("Connection successfull!");
    }

}
