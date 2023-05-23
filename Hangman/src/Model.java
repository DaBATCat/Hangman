import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class Model {
private final int versuche=10;
private int fails=0;
    ArrayList<String> woerter = new ArrayList<String>();



    public Model() {
        initWords();
    }



private String siegWort="test";

    public void ScannerTest(){
    Scanner scanner = new Scanner(System.in);
    System.out.println("gib ein Wort ein");

    String wort = scanner.nextLine();
    System.out.println("eingegebenes Wort: " + wort);
}

public boolean WortTest(String dasWort){
    Scanner scanner = new Scanner(System.in);
    System.out.println("gib Das Wort ein");

    String wort = scanner.nextLine();
    if (wort.equalsIgnoreCase(dasWort)){
        return true;
    }
    else return false;

}


private void initWords(){
    woerter.add("Toast");
    woerter.add("Apfel");
    woerter.add("test");
    woerter.add("Zaun");
    woerter.add("Daniel");
}

public void firstEzGame(){
    Random rand = new Random();
    int nr=rand.nextInt(woerter.size()-1);

    String siegwort;
    siegwort = woerter.get(nr);
    while (fails<10){
        if (WortTest(siegwort)){
            System.out.println("SIEEEGGGGGGGG");
            return;
        }
        else {
            System.out.println("Falsch. Probier es Nochmal!!");
            fails++;
        }


        }
    System.out.println("DU HAST VERLOREN!!!");
    System.out.println("Das wort war: "+siegwort);
}




public void soutWords(){
    for(int i = 0; i < woerter.size(); i++) {
        System.out.print(woerter.get(i)+", ");
    }
}


public void length(){
    System.out.println(woerter.size());
    System.out.println(woerter.get(woerter.size()-1));
}


}
