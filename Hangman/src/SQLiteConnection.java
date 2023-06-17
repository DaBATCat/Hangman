package org.app.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SQLiteConnection {
    // hangman.db:

    // Name der Tabelle: words
    // Spalten der Tabelle: id (PK) | word
    // -----------------------------------------------------------
    // management:
    // Name der Tabelle: management
    // Spalten der Tabelle: username varchar(32) (PK) | passwort varchar(64) | spiele int | wins int | losses int
    static enum Table {
      WORDS,
      MANAGEMENT
    }
    static String query = "select * from management";
    static Connection connection = null;
    static String url ;
    public static void main(String[] args) throws SQLException {
      // Welche Datenbank wird angesteuert?
      url = "jdbc:sqlite:hangman.db";

        try {
      connect(url);

      // executions here
          // new SQLiteConnection().createUser("Daniel", "penis1");
          new SQLiteConnection().printTable(Table.MANAGEMENT);
    }
    catch (SQLException e){
          printtln("Error!");
      e.printStackTrace();
    }

    assert connection != null;
    connection.close();
    printtln("Closed connection");
 }

 public void addWord(String word) throws SQLException {
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("insert into words (word) values ('%s')", word);
   statement.executeUpdate(tableQuery);
   printtln("Query executed! (" + tableQuery + ")");
 }

 public void addUser(String username, String password) throws SQLException {
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("insert into management values(" +
           "'%s', '%s', 0, 0, 0);", username, password);
   statement.executeUpdate(tableQuery);
   printtln("Query executed! (" + tableQuery + ")");

 }

 public void createUser(String username, String password) throws SQLException {
      String query = String.format("insert into management values ('%s', '%s', 0, 0, 0);", username, password);
    new SQLiteConnection().executeSimpleQuery(query);
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

 public static String[] getWordsFromWordsTable(Connection connection) throws SQLException{
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
  // veraltet, nur für words
  public void executeStatements(Connection connection, String query, boolean printTable) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
    printtln("Query executed! (" + query + ")");
    if(printTable)printTable(Table.WORDS);
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

  //TODO: Dingsbums management abfrage
  private void printTable(Table table) throws SQLException {
    Statement statement = connection.createStatement();
    String tableQuery;
    if(table == Table.WORDS) {
      tableQuery = "select * from words";
      statement.executeUpdate(tableQuery);
      printtln("Query executed! (" + tableQuery + ")");
      ResultSet resultSet = statement.executeQuery(tableQuery);

      printtln("Printing Table: " + table.toString().toLowerCase());
      System.out.println("ID\tword");
      while (resultSet.next()) {
        System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2));
      }
    }
    else{
      tableQuery = "select * from management";
      statement.executeUpdate(tableQuery);
      printtln("Query executed! (" + tableQuery + ")");
      ResultSet resultSet = statement.executeQuery(tableQuery);

      System.out.println("username\tpassword\tspiele\twins\tlosses");
      System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2) + "\t"+
              resultSet.getInt(3) + "\t" + resultSet.getInt(4) + "\t" + resultSet.getInt(5));

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

    public void executeSimpleQuery(String query) throws SQLException {
      Statement statement = connection.createStatement();
      statement.executeUpdate(query);
      printtln("Query executed! (" + query + ")");
      //ResultSet resultSet = statement.executeQuery(query);
    }

}
