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
        lancerMode(2);
        s.close();
    }

    public static void lancerMode(int choix){
        switch(choix){
            case 1: exempleLanceIA(); break;
            case 2: exempleLanceJeuHumain(); break;
            case 3: championnat(); break;
            default: System.out.println("Choix invalide");
        }
    }

    public static void championnat() {   //TEST 1
        try{
            Championnat1v1 c = new Championnat1v1(new int[] {241, 88}, 10000);
            c.run();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void exempleLanceIA() {   //TEST 1
        try{
            TousEnsemble t = new TousEnsemble(new int[] {241, 88}, 10000);
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
        Joueur[] joueurs = {j0};

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
