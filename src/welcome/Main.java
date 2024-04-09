package welcome;
import welcome.ia.*;
import welcome.utils.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        //Mode 1 : plein de parties - Mode 2 : détail de la partie
        lancerMode(1);
    }

    public static void lancerMode(int choix){
        switch(choix){
            case 1: exempleLanceIA(); break;
            case 2: exempleLanceJeuHumain(); break;
            default: System.out.println("Choix invalide");
        }
    }
    
    public static void exempleLanceIA() {
        try{
            TousEnsemble t = new TousEnsemble(new int[] {241}, 100000);
            t.run();
        }        
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void exempleLanceJeuHumain() {
        Joueur j0= new Bot(new Strat88(), "Leo", "Loos en gohelle");
        Joueur j1= new Bot(new Strat242(), "Jules", "Los Angeles");
        Joueur[] joueurs = {j1};

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
