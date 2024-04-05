package welcome.ia;

import welcome.*;
import welcome.utils.RandomSingleton;
import java.util.ArrayList;

public class Strat241 extends Strat{
    // bot de la mort qui tue

    //Création d'un plateau idéal
    double[][] plateau_ideal;
    int pioche_choisie;
    boolean construire_piscine;


    public Strat241() {
        this.plateau_ideal = new double[][] {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
                {1, 2.3, 3.5, 4.8, 6.1, 7.4, 8.6, 9.9, 11.2, 12.6, 13.7, 15}
        };

    }
    
    @Override
    public String nomVille(){
        return "Los Angeles";
    }
    
    @Override
    public String nomJoueur(){
        return "RAMAEN, Jules";
    }
    
    //Choisir au hasard parmi les 3 numéros dispos
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        pioche_choisie = -1;

        for(int pioche_idx = 0; pioche_idx < 3; pioche_idx++){

            Travaux action = (Travaux)j.actions[pioche_idx].top();
            Travaux numero = (Travaux)j.numeros[pioche_idx].top();

            if(action.getAction() == 0 && piscineAvailableAt(j, joueur, numero.getNumero())){ //piscine
                pioche_choisie = pioche_idx;
                construire_piscine = true;
            }
            else if(action.getAction() == 3){ //parc
                pioche_choisie = pioche_idx;
            }

        }


        /*
        if(!is_full(j.joueurs[joueur].ville.rues[2])){ // si la rue 3 n'est pas vide, on la remplit
            double[][] ecarts = new double[3][12];

            for(int pioche_idx = 0; pioche_idx < 3; pioche_idx++){          //parcours les trois pioches
                for(int maison_idx = 0; maison_idx < 12; maison_idx++){     //parcours les maisons de la rue 3

                    ecarts[pioche_idx][maison_idx] = Math.abs(plateau_ideal[2][maison_idx] - ((Travaux)j.numeros[0].top()).getNumero()); //calcule l'écart entre la carte de la pioche sélectionnée et le plateau ideal

                }
            }

            int pioche_choisie = trouverIndiceLigneMin(ecarts);                       //la pioche dans laquelle se trouve le plus petit ecart
            int maison_choisie = trouverIndiceColMinDansLigne(ecarts, pioche_choisie);  //la maison avec le nombre le plus proche


        }
        else{

        }

        */

        if(pioche_choisie<0 || pioche_choisie>2)
            pioche_choisie = RandomSingleton.getInstance().nextInt(3);
        return pioche_choisie;
    }
    
    //Choisir de placer un numéro bis
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        int res=-1;
        
        //A COMPLETER
        
        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }
    
    //Choisir au hasard parmi les emplacements dispos
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        int res=-1;

        if(construire_piscine){             //on place la piscine correctement, on sait déja que c'est possible grave a isPiscineAvailable
            for(int i = 0; i < placeValide.size()-1; i++){
                if(placeValide.get(i)/100 == 0 && (placeValide.get(i)%100 == 2 || placeValide.get(i)%100 == 6 || placeValide.get(i)%100 == 7)){
                    res = i;
                }
                else if(placeValide.get(i)/100 == 1 && (placeValide.get(i)%100 == 0 || placeValide.get(i)%100 == 3 || placeValide.get(i)%100 == 7)){
                    res = i;
                }
                else if(placeValide.get(i)/100 == 2 && (placeValide.get(i)%100 == 1 || placeValide.get(i)%100 == 6 || placeValide.get(i)%100 == 10)){
                    res = i;
                }
            }
        }
        
        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }
    
    //Choisir le même numéro que celui de la carte quand l'action est un intérimaire
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        int res=-1;
        
        //A COMPLETER
        
        if((res<(numero-2) || res>(numero+2)) || res<0)
            res=Math.max(0, RandomSingleton.getInstance().nextInt(5) + numero - 2) ;
        return res;
    }
    
    //Valorise aléatoirement une taille de lotissements (proba plus forte si plus d'avancements possibles)
    @Override
    public int valoriseLotissement(Jeu j, int joueur){        
        int res=-1;
        
        //A COMPLETER
        
        if(res<1 || res>6)
            res=RandomSingleton.getInstance().nextInt(6)+1;
        return res;
    }
    
    //Met une barrière à une position aléatoire
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        int res=-1;
        
        //A COMPLETER
        
        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }
    
    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        boolean res = true;
        
        //A COMPLETER
        
        return res;
    }
    
    @Override
    public void resetStrat(){};


    // --------- UTILITAIRES ----------

    //Pour trouver les indices du minimum des écarts par rapport au plateau ideal
    /*
    public static int trouverIndiceLigneMin(double[][] tableau) {
        double min = tableau[0][0];
        int indiceLigneMin = 0;
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] < min) {
                    min = tableau[i][j];
                    indiceLigneMin = i;
                }
            }
        }
        return indiceLigneMin;
    }*/

    /*
    public static int trouverIndiceColMinDansLigne(double[][] tableau, int indiceLigne) {
        double min = tableau[indiceLigne][0];
        int indiceColMinDansLigne = 0;
        for (int j = 0; j < tableau[indiceLigne].length; j++) {
            if (tableau[indiceLigne][j] < min) {
                min = tableau[indiceLigne][j];
                indiceColMinDansLigne = j;
            }
        }
        return indiceColMinDansLigne;
    }*/

    /*
    public static boolean is_full(Rue rue){ //indique si une rue est pleine
        boolean full = true;
        for(int i = 0; i < rue.taille; i++){
            if(rue.maisons[i].estVide()){
                full = false;
            }
        }

        return full;
    }*/

    public static boolean piscineAvailableAt(Jeu j, int joueur, int numero){
        boolean out = false;
        for(int i = 0; i < 3; i++){
            if(numero <= 10 + i){
                if(j.joueurs[joueur].ville.rues[i].maisons[numero-1].emplacementPiscine && j.joueurs[joueur].ville.rues[i].maisons[numero-1].estVide()){    //si emplacement piscine et vide
                    out = true;
                }
            }

        }
        return out;
    }

    /*
    public static boolean isPiscine(int numero, int rue){
        switch (rue){
            case 1:
                if(numero == 3 || numero == 7 || numero == 8){ return true;}
                break;
            case 2:
                if(numero == 1 || numero == 4 || numero == 8){ return true;}
                break;
            case 3:
                if(numero == 2 || numero == 7 || numero == 11){ return true;}
                break;
            default:
                return false;
                break;
        }
    }*/
}
