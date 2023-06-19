package org.app.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class SQLiteConnection {
    // hangman.db:

    // Name der Tabelle: words
    // Spalten der Tabelle: id (PK) | word
    // -----------------------------------------------------------
    // management:
    // Name der Tabelle: management
    // Spalten der Tabelle: username varchar(32) (PK) | passwort varchar(64) | spiele int | wins int | losses int
    public static enum Table {
      WORDS,
      MANAGEMENT
    }
    // Query für Methoden, bei denen man keine Query übergibt
    static String query = "select * from management";
    static Connection connection = null;

  // Welche Datenbank wird angesteuert? Wir haben nur die Datenbank hangman.db
    static String url = "jdbc:sqlite:hangman.db"; ;
    public static void main(String[] args) throws SQLException {


        try {
      connect(url);

      // executions here

          // System.out.println(new SQLiteConnection().passwordEqualsWithUsername("Daniel", "penis1"));
          // new SQLiteConnection().executeSimpleQuery();

          new SQLiteConnection().printTable(Table.MANAGEMENT);


        }
    catch (SQLException e){
          printtln("Error!");
      e.printStackTrace();
    }

    assert connection != null;
      // Datenbankverbindung wird geschlossen
      connection.close();
    printtln("Closed connection");
 }

 public int getWins(String username){
      try{
        Statement statement = connection.createStatement();
        String tableQuery;
        tableQuery = String.format("select wins from management where username = '%s'", username);
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet.getInt(1);
      }
      catch (SQLException e){
        e.printStackTrace();
      }
      return 0;
 }

 // Lösche einen User anhand seines Usernames
 public void deleteUser(String username){
      printtln("User " + username + " is going to be deleted. Old table:");
      printTable(Table.MANAGEMENT);
      try{
        executeSimpleQuery(String.format("delete from management where username = '%s'", username));
      }
      catch (SQLException e){
        e.printStackTrace();
      }
   printtln("Deleted user " + username + ", new Table:");
      printTable(Table.MANAGEMENT);
 }

// Erhöhe losses um 1
  public void incrementLosses(String username) throws SQLException {
    Statement statement = connection.createStatement();
    String tableQuery;
    tableQuery = String.format("update management set losses = losses + 1 where username = '%s'", username);
    statement.executeUpdate(tableQuery);
    // printtln("Query executed! (" + tableQuery + ")");
  }

  // Verringere losses um 1
  public void decrementLosses(String username) throws SQLException {
    Statement statement = connection.createStatement();
    String tableQuery;
    tableQuery = String.format("update management set losses = losses - 1 where username = '%s'", username);
    statement.executeUpdate(tableQuery);
    // printtln("Query executed! (" + tableQuery + ")");
  }

  // Erhöhe wins um 1
 public void incrementWins(String username) throws SQLException {
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("update management set wins = wins + 1 where username = '%s'", username);
   statement.executeUpdate(tableQuery);
   // printtln("Query executed! (" + tableQuery + ")");
 }

 // Verringere wins um 1
 public void decrementWins(String username) throws SQLException {
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("update management set wins = wins - 1 where username = '%s'", username );
   statement.executeUpdate(tableQuery);
   // printtln("Query executed! (" + tableQuery + ")");

 }

 // Überprüfe anhand des usernames, ob der Benutzer bereits registriert ist (returned true oder false)
 public boolean userIsRegistered(String username) {
      try{
        ArrayList<String> usernames = new ArrayList<>();
        connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();
        statement.executeUpdate("select * from management");
        ResultSet resultSet = statement.executeQuery("select * from management");
        while(resultSet.next()){
          usernames.add(resultSet.getString(1));
        }
        for (String s : usernames) {
          if (Objects.equals(s, username)) return true;
        }
        return false;

      }
      catch (SQLException e) {
        e.printStackTrace();

      }
      return false;
 }

 // Überprüft, ob das übergebene Passwort mit dem Usernamen übereinstimmt
 public boolean passwordEqualsWithUsername(String username, String password) throws SQLException {
   if(userIsRegistered(username)){
     String defaultPassword;
     connection = DriverManager.getConnection(url);
     Statement statement = connection.createStatement();
     ResultSet resultSet = statement.executeQuery(String.format("select passwort from management where username = '%s'", username));
     defaultPassword = resultSet.getString(1);
     if(defaultPassword.equals(password)) return true;
   }
   return false;
 }

 // Ein Wort zur Datenbank hinzufügen
 public void addWord(String word) throws SQLException {
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("insert into words (word) values ('%s')", word);
   statement.executeUpdate(tableQuery);
   printtln("Query executed! (" + tableQuery + ")");
 }

 // Einen Benutzer erstellen, bzw. ihn der Datenbank hinzufügen
 public void addUser(String username, String password) throws SQLException {
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("insert into management values(" +
           "'%s', '%s', 0, 0, 0);", username, password);
   statement.executeUpdate(tableQuery);
   printtln("Query executed! (" + tableQuery + ")");

 }

 // Veraltet
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

  // Veraltet
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
  // Veraltet, funktioniert nur für Tabelle words
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

  // Gibt die übergebene Tabelle (mehr oder weniger gut formatiert) auf der Konsole aus
  public void printTable(Table table) {
      try{
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

          while(resultSet.next()){
            System.out.println( "username='" + resultSet.getString(1) + "'\t\t" +
                    ", password='" + resultSet.getString(2) + "'\t\t" +
                    ", spiele=" + resultSet.getInt(3) + "\t\t" +
                    ", wins=" + resultSet.getInt(4) + "\t\t" +
                    ", losses=" + resultSet.getInt(5));

          }

        }
      }
      catch (SQLException e){
        printtln("Error!");
        e.printStackTrace();
      }
  }

  // Gibt alle Wörter der Datenbank aus
  private void printWords(Connection connection) throws SQLException{
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
    printtln("Query executed! (" + query + ")");
    ResultSet resultSet = statement.executeQuery(query);
    while(resultSet.next()){
      System.out.println(resultSet.getString(2));
    }
  }

  // Alle Wörter als String[] aus der Datenbank
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


  // Log-Ausgabe
  private static void printtln(String defaultText) {
    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    String formattedDate = localDateTime.format(dateTimeFormatter);
    System.out.println("[" + formattedDate + "] " + defaultText);
  }

    private static void connect(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        printtln("Connection successfull!");
    }

    // Führt eine übergebene Query aus
    public void executeSimpleQuery(String query) throws SQLException {
      Statement statement = connection.createStatement();
      statement.executeUpdate(query);
      printtln("Query executed! (" + query + ")");
      //ResultSet resultSet = statement.executeQuery(query);
    }

}
