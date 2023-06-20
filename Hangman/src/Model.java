package org.app.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class Model {
private final int versuche=10;
private int fails=0;
private Spieler player = GUI.getSpieler();
   private String siegwort;
    ArrayList<String> woerter = new ArrayList<String>();



    public Model() {
        initWords();
    }


    //beispielwörter
    public void initWords(){
        woerter= new SQLiteConnection().getWordsFromTable();
    }

    public void setFails(int fails) {
        this.fails = fails;
    }

    //Wort eingabe Methode
    public String inputWort(){
        Scanner scanner = new Scanner(System.in);
        String wort="";

        //verbietet, dass man ein leeres Wort eingibt und fehler verursacht
        do {
            System.out.println("gib ein Wort oder einen Buchstaben  ein");
             wort = scanner.nextLine();
        } while (wort.length()==0);

        System.out.println("eingegebenes Wort: " + wort);

        return wort;
    }

//überprüft ob das eingegebene Wort das gesuchte Wort ist
public boolean WortCheck(String dasWort, String siegwort){

    if (dasWort.equalsIgnoreCase(siegwort)){
        return true;
    }
    else return false;

}



public void nochmalSpielen() throws SQLException {
    Scanner scanner = new Scanner(System.in);
    String input="";
    System.out.println("möchtest du nochmal spielen? (J/N)");
    input=scanner.nextLine();
    if (input.equalsIgnoreCase("j")||input.equalsIgnoreCase("ja")){
        Game(player);

    }
    else if (input.equalsIgnoreCase("n")||input.equalsIgnoreCase("nein")){
        System.out.println("Ok Tschüss");
        return;
    }
    else{
        System.out.println("DAS STAND NICHT ZUR VERFÜGUNG!");
        nochmalSpielen();
    }

}


//prüft ob der eingegebene Buchstabe im gesuchten Wort vorkommt und returnt in einer Integer Arraylist die Position der Buchstaben im gesuchten Wort
public ArrayList<Integer> Buchstabencheck(String derBuchstabe, String siegwortt){

        String siegWort=siegwortt.toLowerCase();
        String buchstabeS=derBuchstabe.toLowerCase();

        char buchstabeC=buchstabeS.charAt(0);
        ArrayList<Integer> positions =new ArrayList<>();


    if (siegWort.contains(buchstabeS)){
        for (int i = 0; i < siegWort.length(); i++) {

            if (siegWort.charAt(i)==buchstabeC){
                positions.add(i);
            }
        }
        return positions;

    }
    else return positions;
}

//einfache Methode die beim Sieg aufgerufen wird
public void Victory() throws SQLException {
    System.out.println("Du hast gewonnen!");
    System.out.println("Das Wort ist: "+siegwort);
    player.increaseWins();
    nochmalSpielen();
}

public void fail() throws SQLException {
    System.out.println("DU HAST VERLOREN!!!");
    System.out.println("Das wort war: "+siegwort);
    player.increaseFails();
    nochmalSpielen();
}

//spiel
public void Game(Spieler spieler) throws SQLException {
        player=spieler;
        setFails(0);
    Random rand = new Random();
    int nr=rand.nextInt(woerter.size()-1);
    ArrayList<String> tempWord=new ArrayList<>();

    //wählt zufälliges wort aus der Liste mit den Beispielwörtern
    siegwort = woerter.get(nr);
    System.out.println("finde das Wort:");

    //füllt das Testwort mit platzhalter Werten
    for (int i = 0; i < siegwort.length(); i++) {
        tempWord.add("_");

    }


    while (fails<10){
        System.out.println("Übrige Versuche: "+(versuche-fails));
        for (int i = 0; i < tempWord.size(); i++) {
            if (tempWord.contains("_")){
            System.out.print(tempWord.get(i)+" ");}
            else{ Victory(); return;}

        }
        String inputWort=inputWort();
        if (inputWort.length()>1){

            if (WortCheck(inputWort,siegwort)){
                Victory();
                return;
            }
            else {
                System.out.println("Falsch. Probier es Nochmal!!");
                fails++;
            }
        }
        else {
            ArrayList<Integer> buchstabenPos=new ArrayList<>();
            buchstabenPos=Buchstabencheck(inputWort,siegwort);
            if (!buchstabenPos.isEmpty()){
                System.out.println("BUCHSTABE IST DABEI");
                for (int i = 0; i < buchstabenPos.size(); i++) {
                    tempWord.set(buchstabenPos.get(i),inputWort);

                }

            }
            else{
                System.out.println("DER BUCHSTABE IST ES NICHT!");
                fails++;
            }

        }


        }
    fail();

}



//test Methode
public void soutWords(){
    for(int i = 0; i < woerter.size(); i++) {
        System.out.print(woerter.get(i)+", ");
    }
}

//test Methode
public void length(){
    System.out.println(woerter.size());
    System.out.println(woerter.get(woerter.size()-1));
}


}
