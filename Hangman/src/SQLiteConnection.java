package org.app.utils;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class SQLiteConnection {
    // hangman.db:

    // Name der Tabelle: words
    // Spalten der Tabelle: id int (PK) autoincrement | word
    // -----------------------------------------------------------
    // management:
    // Name der Tabelle: management
    // Spalten der Tabelle: username varchar(32) (PK) | passwort varchar(64) | spiele int | wins int | losses int | addedwords int
    public static enum Table {
      WORDS,
      MANAGEMENT
    }
    // Query für Methoden, bei denen man keine Query übergibt
    static String query = "select * from management";
    static Connection connection;

  // Welche Datenbank wird angesteuert? Wir haben nur die Datenbank hangman.db
    static String url = "jdbc:sqlite:hangman.db"; ;
    public static void main(String[] args) throws SQLException {


        try {
      connect(url);

      // executions here

          new SQLiteConnection().removeWord("Rindfleischettikettierung");
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
 public boolean passwordIsConfirmed(String username, String password){
      printtln(String.format("Checked if password confirms with the given username '%s'", username));
   try{
     connect(url);
     String tableQuery;
     tableQuery = String.format("select passwort from management where username = '%s'", username);
     PreparedStatement pst = connection.prepareStatement(tableQuery);
     ResultSet resultSet = pst.executeQuery();
     Statement statement = connection.createStatement();
     if(resultSet.next()){
       printtln("Condition is " + Objects.equals(statement.executeQuery(tableQuery).getString(1), password));
       return Objects.equals(statement.executeQuery(tableQuery).getString(1), password);
     }
   }
   catch (SQLException e){
     e.printStackTrace();
   }
   finally {
     if (connection != null) {
       try {
         connection.close();
       } catch (SQLException e) {
         e.printStackTrace();
       }

     }
   }
   return false;
 }


 public SQLiteConnection() {
   try {
     connect(url);
   } catch (Exception e) {
     e.printStackTrace();
   }
 }

// Löscht ein Wort aus der Datenbank
 public void removeWord(String word){
      printtln("'" + word + "is going to be removed from the database");
      if(wordIsInTable(word)){
        try{
          connect(url);
          // Setzt das 1. Zeichen im String zu einem Großbuchstaben
          word = word.substring(0,1).toUpperCase() + word.substring(1);
          Statement statement = connection.createStatement();
          String tableQuery;
          tableQuery = String.format("delete from words where word = '%s'", word );
          PreparedStatement pst = connection.prepareStatement(tableQuery);

          try(pst){
            pst.executeUpdate();
            printtln("Removed Word: " + word);
          }
          catch (SQLException e) {
            e.printStackTrace();
          }
        }
        catch (SQLException e){
          e.printStackTrace();
        }
        finally {
          if (connection != null){
            try{
              connection.close();
            }
            catch (SQLException e){
              e.printStackTrace();
            }

          }
        }
      }
 }

 // Prüfe, ob das übergebene Wort bereits in der Datenbank ist
 public boolean wordIsInTable(String word){
      printtln("Searching for '" + word + "' in the database");
    ArrayList<String> words = getWordsFromTable();
   for (String s : words) {
     if (word.equalsIgnoreCase(s)){
       printtln("Found '" + word + "'!");
       return true;
     }
   }
   printtln("Couldn't find '" + word + "'");
   return false;
 }

 // Erhöhe die Anzahl gespielter Spiele des Users um 1
 public void incrementGamesPlayed(String username) throws SQLException {
      printtln("The number of " + username + "'s played games will be incremented by 1");
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("update management set spiele = spiele + 1 where username = '%s'", username );
   statement.executeUpdate(tableQuery);
   // printtln("Query executed! (" + tableQuery + ")");

 }
 // Verringere die Anzahl gespielter Spiele des Users um 1
 public void decrementGamesPlayed(String username) throws SQLException {
   printtln("The number of " + username + "'s played games will be decremented by 1");
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("update management set spiele = spiele - 1 where username = '%s'", username );
   statement.executeUpdate(tableQuery);
   // printtln("Query executed! (" + tableQuery + ")");

 }

 // Anzahl der gespielten Spiele zurückgeben
  public int getGamesPlayed(String username){
      printtln("The number of " + username + "'s total played games is searched");
    try{
      connect(url);
      String tableQuery;
      tableQuery = String.format("select spiele from management where username = '%s'", username);
      PreparedStatement pst = connection.prepareStatement(tableQuery);
      ResultSet resultSet = pst.executeQuery();
      Statement statement = connection.createStatement();
      if(resultSet.next()){
        return statement.executeQuery(tableQuery).getInt(1);
      }
      // resultSet.getStatement().executeUpdate(tableQuery);
      // pst.getResultSet().getStatement().executeUpdate(tableQuery);

    }
    catch (SQLException e){
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }
    }
    return 0;
  }

  // Anzahl der verlorenen Spiele zurückgeben
  public int getLosses(String username){
      printtln("The number of " + username + "'s total game losses is searched");
    try{
      connect(url);
      String tableQuery;
      tableQuery = String.format("select losses from management where username = '%s'", username);
      PreparedStatement pst = connection.prepareStatement(tableQuery);
      ResultSet resultSet = pst.executeQuery();
      Statement statement = connection.createStatement();
      if(resultSet.next()){
        return statement.executeQuery(tableQuery).getInt(1);
      }
      // resultSet.getStatement().executeUpdate(tableQuery);
      // pst.getResultSet().getStatement().executeUpdate(tableQuery);

    }
    catch (SQLException e){
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }
    }
    return 0;  }

// Anzahl der gewonnenen Spiele zurückgeben
 public int getWins(String username){
   printtln("The number of " + username + "'s total game wins is searched");
   try{
     connect(url);
     String tableQuery;
     tableQuery = String.format("select wins from management where username = '%s'", username);
     PreparedStatement pst = connection.prepareStatement(tableQuery);
      ResultSet resultSet = pst.executeQuery();
      Statement statement = connection.createStatement();
      if(resultSet.next()){
        return statement.executeQuery(tableQuery).getInt(1);
      }
     // resultSet.getStatement().executeUpdate(tableQuery);
     // pst.getResultSet().getStatement().executeUpdate(tableQuery);

   }
   catch (SQLException e){
     e.printStackTrace();
   }
   finally {
     if (connection != null){
       try{
         connection.close();
       }
       catch (SQLException e){
         e.printStackTrace();
       }

     }
   }
    return 0;
    }

 // Lösche einen User anhand seines Usernames
 public void deleteUser(String username){
      printtln("User " + username + " is going to be deleted.");
   try{
     String tableQuery;
     tableQuery = String.format("delete from management where username = '%s'", username);
     Statement statement = connection.createStatement();
     PreparedStatement pst = connection.prepareStatement(tableQuery);

     try(pst){
       pst.executeUpdate();
       printtln("User " + username + " has been deleted.");
     }
     catch (SQLException e) {
       e.printStackTrace();
     }
     // resultSet.getStatement().executeUpdate(tableQuery);
     // pst.getResultSet().getStatement().executeUpdate(tableQuery);

   }
   catch (SQLException e){
     e.printStackTrace();
   }
   finally {
     if (connection != null){
       try{
         connection.close();
       }
       catch (SQLException e){
         e.printStackTrace();
       }

     }
   }
 }

// Erhöhe losses um 1
  public void incrementLosses(String username) throws SQLException {
    printtln("The number of " + username + "'s losses will be incremented by 1");
    try{
      String tableQuery;
      tableQuery = String.format("update management set losses = losses + 1 where username = '%s'", username);
      PreparedStatement pst = connection.prepareStatement(tableQuery);

      try(pst){pst.executeUpdate();  }
      catch (SQLException e) {
        e.printStackTrace();
      }

    }
    catch (SQLException e){
      e.printStackTrace();
    }
    finally {
      if (connection != null){
        try{
          connection.close();
        }
        catch (SQLException e){
          e.printStackTrace();
        }

      }
    }
  }

  // Verringere losses um 1
  public void decrementLosses(String username) throws SQLException {
    printtln("The number of " + username + "'s total game losses will be decremented by 1");
    Statement statement = connection.createStatement();
    String tableQuery;
    tableQuery = String.format("update management set losses = losses - 1 where username = '%s'", username);
    statement.executeUpdate(tableQuery);
    // printtln("Query executed! (" + tableQuery + ")");
  }

  // Erhöhe wins um 1
 public void incrementWins(String username) throws SQLException {
   printtln("The number of " + username + "'s total game wins will be incremented by 1");
   try{
        String tableQuery;
        tableQuery = String.format("update management set wins = wins + 1 where username = '%s'", username);
        PreparedStatement pst = connection.prepareStatement(tableQuery);

        try(pst){pst.executeUpdate();  }
        catch (SQLException e) {
          e.printStackTrace();
        }
        // resultSet.getStatement().executeUpdate(tableQuery);
        // pst.getResultSet().getStatement().executeUpdate(tableQuery);

      }
      catch (SQLException e){
        e.printStackTrace();
      }
      finally {
        if (connection != null){
          try{
            connection.close();
          }
          catch (SQLException e){
            e.printStackTrace();
          }

        }
      }
   // printtln("Query executed! (" + tableQuery + ")");
 }

 // Verringere wins um 1
 public void decrementWins(String username) throws SQLException {
   printtln("The number of " + username + "'s total game wins will be decremented by 1");
   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("update management set wins = wins - 1 where username = '%s'", username );
   statement.executeUpdate(tableQuery);
   // printtln("Query executed! (" + tableQuery + ")");

 }

 // Überprüfe anhand des usernames, ob der Benutzer bereits registriert ist (returned true oder false)
 public boolean userIsRegistered(String username) {
   printtln("It is checked if the user " + username + " is registered");
   try{
        ArrayList<String> usernames = new ArrayList<>();
        connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();
        statement.executeUpdate("select * from management");
        ResultSet resultSet = statement.executeQuery("select * from management");
        while(resultSet.next()){
          usernames.add(resultSet.getString(1));
        }
        resultSet.close();
        for (String s : usernames) {
          if (Objects.equals(s, username)){
            printtln("User " + username + " is registered");
            return true;
          }
        }
        resultSet.close();
     printtln("User " + username + " is not registered");
     return false;

      }
      catch (SQLException e) {
        e.printStackTrace();

      }
      return false;
 }

 // Überprüft, ob das übergebene Passwort mit dem Usernamen übereinstimmt
 public boolean passwordEqualsWithUsername(String username, String password) throws SQLException {
   printtln("It will be checked if " + username + "s password matches with the username");
   if(userIsRegistered(username)){
     String defaultPassword;
     connection = DriverManager.getConnection(url);
     Statement statement = connection.createStatement();
     ResultSet resultSet = statement.executeQuery(String.format("select passwort from management where username = '%s'", username));
     defaultPassword = resultSet.getString(1);
     resultSet.close();
     if(defaultPassword.equals(password)){
       printtln(username + "'s password matches");
       return true;
     }
   }
   printtln(username + "'s password doesn't match");
   return false;
 }

 // Ein Wort zur Datenbank hinzufügen
 public void addWord(String word) {
      printtln("The word '" + word + "' will be added to the database");
      try{
        connection = DriverManager.getConnection(url);
        // First letter is UpperCase
        word = word.substring(0,1).toUpperCase() + word.substring(1);
        String tableQuery;
        tableQuery = String.format("insert into words (word) values ('%s')", word);
        Statement statement = connection.createStatement();
        PreparedStatement pst = connection.prepareStatement(tableQuery);

        try(pst){
          pst.executeUpdate();
          printtln("Added Word: " + word);
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
        // resultSet.getStatement().executeUpdate(tableQuery);
        // pst.getResultSet().getStatement().executeUpdate(tableQuery);

      }
      catch (SQLException e){
        e.printStackTrace();
      }
      finally {
        if (connection != null){
          try{
            connection.close();
          }
          catch (SQLException e){
            e.printStackTrace();
          }

        }
      }

 }

 // Einen Benutzer erstellen, bzw. ihn der Datenbank hinzufügen
 public void addUser(String username, String password) throws SQLException {
   printtln("The user " + username + " will be created");

   Statement statement = connection.createStatement();
   String tableQuery;
   tableQuery = String.format("insert into management values(" +
           "'%s', '%s', 0, 0, 0);", username, password);
   statement.executeUpdate(tableQuery);
   printtln("Query executed! (" + tableQuery + ")");
   connection.close();

 }

 // Get all Words here
  public ArrayList<String> getWordsFromTable(){
      printtln("All words are being requested from the table in an ArrayList");
      try{
        ArrayList<String> words = new ArrayList<>();
        connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();
        statement.executeUpdate("select word from words");
        ResultSet resultSet = statement.executeQuery("select word from words");
        while(resultSet.next()){
          words.add(resultSet.getString(1));
        }
        resultSet.close();
        connection.close();
        return words;

      }
      catch (SQLException e){
        e.printStackTrace();
      }
      printtln("Error! Couldn't get the words from the table. Returning empty ArrayList");
      return new ArrayList<>();
  }

  // Gibt die übergebene Tabelle (mehr oder weniger gut formatiert) auf der Konsole aus
  public void printTable(Table table) {
      printtln("The table " + table.toString() + " will be printed with its values");
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
          resultSet.close();
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
          resultSet.close();
        }

        connection.close();
      }
      catch (SQLException e){
        printtln("Error!");
        e.printStackTrace();
      }
  }

  // Gibt alle Wörter der Datenbank aus
  private void printWords(Connection connection) throws SQLException{
      printtln("All words will be printed out from the table");
    Statement statement = connection.createStatement();
    String tableQuery;
    tableQuery = "select word from words";
    statement.executeUpdate(tableQuery);
    printtln("Query executed! (" + tableQuery + ")");
    ResultSet resultSet = statement.executeQuery(tableQuery);
    while(resultSet.next()){
      System.out.println(resultSet.getString(1));
    }
    connection.close();

  }

  // Alle Wörter als String[] aus der Datenbank
  public String[] getWords(Connection connection) throws SQLException{
      printtln("All words will be returned as a string array");
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
    System.out.println("DB[" + formattedDate + "] " + defaultText);
  }

    private static void connect(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        printtln("Connection successfully!");
    }

    // Führt eine übergebene Query aus
    public void executeSimpleQuery(String query) throws SQLException {
      Statement statement = connection.createStatement();
      statement.executeUpdate(query);
      printtln( "Query executed! (" + query + ")" );
      //ResultSet resultSet = statement.executeQuery(query);
    }

    public void closeConnection(){
      try{
        connection.close();
        printtln("Connection closed!");
      }
      catch (SQLException e){
        e.printStackTrace();
      }
    }

}
