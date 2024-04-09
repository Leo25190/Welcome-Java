package welcome;
import welcome.ia.*;
import welcome.utils.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        Scanner s = new Scanner(System.in);
        //Mode 1 : plein de parties - Mode 2 : détail de la partie
        System.out.println("CHOIX DU TYPE DE TEST\nMode 1 : plein de parties - Mode 2 : détail de la partie");
        lancerMode(s.nextInt());
        s.close();
    }

    public static void lancerMode(int choix){
        switch(choix){
            case 1: exempleLanceIA(); break;
            case 2: exempleLanceJeuHumain(); break;
            default: System.out.println("Choix invalide");
        }
    }
    
    public static void exempleLanceIA() {   //TEST 1
        try{
            TousEnsemble t = new TousEnsemble(new int[] {88}, 100);
            t.run();
        }        
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void exempleLanceJeuHumain() {    //TEST 2
        JoueurHumain joueur = new JoueurHumain("Humain", "TrizoLand");
        Joueur j0= new Bot(new Strat88(), "Leo", "Loos en gohelle");
        Joueur j1= new Bot(new Strat241(), "Jules241", "Los Angeles");
        Joueur j2= new Bot(new Strat243(), "Jules243", "Los Angeles");
        Joueur[] joueurs = {j2};

        Jeu j= new Jeu(joueurs);
        try{
            int[] score = j.jouer();
            for(int i=0; i<score.length; i++)
               System.out.println("Joueur " + joueurs[i].nom + " fini avec " + score[i] + " points.");
        }        
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
