package org.app.utils;
//JEDE INSTANZ DIESER KLASSE IST EIN EINZIGARTIGER SPIELER, DER BEIM ERSTELLEN DIESER INSTANZ GENERIERT WIRD UND AUCH IN DIE DATENBANK ÜBERFÜHRT WERDEN MUSS!!
public class Spieler {
    private String Username;
    private String Password;
    private int wins=0;
    private int losses=0;
    private int games=wins+losses;//anzahl der Spiele.Kannst du eigentlich so in der datenbank übernehmen mit der Summe aus wins+losses



    public Spieler(String username, String password) {
        Username = username;
        Password = password;

        //username und passwort werden beim Erstellen eines Spielers mitgegeben. Username ist der Primärschlüssel
    }

    public void increaseWins(){
        wins++;
        //wins auch in der Datenbank vergrößern! Geht mit dem update Befehl
    }

    public void increaseFails(){
        losses++;
    }//fails auch in der Datenbank vergrößern! Geht mit dem update Befehl

//siegesquote braucht nicht in die Datenbank
    public double getSiegesquote(){

        if(wins==0 && losses==0){
            return 0;
        }

        else  return ((int) (((double) wins/(wins+losses)*100) * 100)) / 100d;
    }



}
