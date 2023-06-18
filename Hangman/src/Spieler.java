package org.app.utils;

import java.sql.SQLException;

//JEDE INSTANZ DIESER KLASSE IST EIN EINZIGARTIGER SPIELER, DER BEIM ERSTELLEN DIESER INSTANZ GENERIERT WIRD UND AUCH IN DIE DATENBANK ÜBERFÜHRT WERDEN MUSS!!
public class Spieler {
    private String Username;
    private String Password;
    private int wins=0;
    private int losses=0;
    private int games=wins+losses;//anzahl der Spiele.Kannst du eigentlich so in der datenbank übernehmen mit der Summe aus wins+losses

    SQLiteConnection sqLiteConnection = new SQLiteConnection();



    public Spieler(String username, String password) {
        Username = username;
        Password = password;
        //SPIELER IN DER DATENBANK HIER ERSTELLEN!

        // Hier wird überprüft, ob in der Datenbank schon ein User mit dem Usernamen vorhanden ist. Wenn er es
        // nicht ist, wird ein neuer User angelegt.
        try{
            sqLiteConnection.addUser(username, password);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        //username und passwort werden beim Erstellen eines Spielers mitgegeben. Username ist der Primärschlüssel
    }

    public void increaseWins() throws SQLException {
        wins++;
        //wins auch in der Datenbank vergrößern! Geht mit dem update Befehl
        sqLiteConnection.incrementWins(Username);
    }

    public void increaseFails() throws SQLException {
        losses++;
        sqLiteConnection.incrementLosses(Username);
    }//fails auch in der Datenbank vergrößern! Geht mit dem update Befehl

//siegesquote braucht nicht in die Datenbank
    public double getSiegesquote(){

        if(wins==0 && losses==0){
            return 0;
        }

        else  return ((int) (((double) wins/(wins+losses)*100) * 100)) / 100d;
    }



}
