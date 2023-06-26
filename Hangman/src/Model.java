package org.app.utils;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class Model {
private final int versuche=10;
private int fails=0;
private GameGUI gameGUI;
private Spieler player = GUI.getSpieler();

    public String getSiegwort() {
        return siegwort;
    }

    public String siegwort;
    ArrayList<String> woerter = new ArrayList<>();



    public Model(GameGUI gameGUI) {
        initWords();
        this.gameGUI = gameGUI;
    }


    //beispielwörter
    public void initWords(){
        woerter= new SQLiteConnection().getWordsFromTable();
    }

    //Wort eingabe Methode
    public String inputWort(JTextField textField){
        return  textField.getText();
    }


public void nochmalSpielen() {
    Scanner scanner = new Scanner(System.in);
    String input="";
    System.out.println("möchtest du nochmal spielen? (J/N)");
    input=scanner.nextLine();
    if (input.equalsIgnoreCase("j")||input.equalsIgnoreCase("ja")){
        //Game(player);

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

        if(buchstabeS.length() == 0) return new ArrayList<>();
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

public String getChosenWord(){
    Random rand = new Random();
    int nr=rand.nextInt(woerter.size()-1);
    ArrayList<String> tempWord=new ArrayList<>();
    this.siegwort = woerter.get(nr);
    return siegwort;
}

public boolean inheritsChar(JTextField textField){
    ArrayList<String> tempWord=new ArrayList<>();
    for(int i = 0; i < siegwort.length(); i++){
        tempWord.add("");
    }
    String inputWort=inputWort(textField);
    ArrayList<Integer> buchstabenPos = new ArrayList<>();
    buchstabenPos=Buchstabencheck(inputWort,siegwort);
    if (!buchstabenPos.isEmpty()){
        for(int i = 0; i < buchstabenPos.size(); i++){
            tempWord.set(buchstabenPos.get(i), inputWort);
        }
        return true;
    }
    else{
        fails++;
    }
    return false;
}
}
