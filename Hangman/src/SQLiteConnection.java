package org.app.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SQLiteConnection {
  public static void main(String[] args) throws SQLException {
    Connection connection = null;
    String url = "jdbc:sqlite:hangman.db";
    try {
      connection = DriverManager.getConnection(url);
      printtln("Connection successfull!");
      // executions here
      String query = "Select * from words";
      new SQLiteConnection().executeStatements(connection, query, true) ;
    }
    catch (SQLException e){
      e.printStackTrace();
    }
    assert connection != null;
    connection.close();
    printtln("Closed connection");




  }

  // for default executions
  public void executeStatements(Connection connection, String query, boolean printTable) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
    printtln("Query executed! (" + query + ")");
    if(printTable)printTable(statement);
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
    ResultSet resultSet = statement.executeQuery("select * from words;");
    printtln("Printing Table:");
    while(resultSet.next()){
      System.out.println("ID\tword");
      System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2));
    }

  }

  private static void printtln(String defaultText) {
    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDate = localDateTime.format(dateTimeFormatter);
    System.out.println("[" + formattedDate + "] " + defaultText);
  }

  /* Table name: words
   * attributes: (id#, word)
   */

}
