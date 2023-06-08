package org.app.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SQLiteConnection {
    static String query = "select * from words";
    static Connection connection = null;
    static String url = "jdbc:sqlite:hangman.db";
    public static void main(String[] args) throws SQLException {
        try {
      connection = DriverManager.getConnection(url);
      printtln("Connection successfull!");
      // executions here
          new SQLiteConnection().printWords(connection);
    }
    catch (SQLException e){
      e.printStackTrace();
    }
      System.out.println(new SQLiteConnection().getWords(connection)[1]);

    assert connection != null;
    connection.close();
    printtln("Closed connection");
 }

 // Get all Words here
  public ArrayList<String> getWordsFromTable() throws SQLException {
    ArrayList<String> words = new ArrayList<>();
    connection = DriverManager.getConnection(url);
    Statement statement = connection.createStatement();
    statement.executeUpdate("select * from words");
    ResultSet resultSet = statement.executeQuery(query);
    while(resultSet.next()){
      words.add(resultSet.getString(2));
    }
    return words;
  }

 public static String[] getWordsFromTable(Connection connection) throws SQLException{
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
    ResultSet resultSet = statement.executeQuery(query);
    printtln("Printing Table:");
    System.out.println("ID\tword");
    while(resultSet.next()){
      System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2));
    }

  }
  private void printWords(Connection connection) throws SQLException{
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
    printtln("Query executed! (" + query + ")");
    ResultSet resultSet = statement.executeQuery(query);
    while(resultSet.next()){
      System.out.println(resultSet.getString(2));
    }
  }
  public String[] getWords(Connection connection) throws SQLException{
      ArrayList<String> words = new ArrayList<>();
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
    printtln("Query executed! (" + query + ")");
    ResultSet resultSet = statement.executeQuery(query);
    while(resultSet.next()){
      words.add(resultSet.getString(2));
    }
    String[] result = new String[words.size()];
    words.toArray(result);
    return result;
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
