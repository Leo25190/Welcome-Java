/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package welcome.ia;

import java.util.ArrayList;
import welcome.Jeu;

/**
 *
 * @author Mimi
 */
public abstract class Strat implements Strategie{
    
    //Retourne le nom de la ville
    public abstract String nomVille();
    
    //Retourne le nom du Joueur
    public abstract String nomJoueur();
    
    //Retourne la combinaison choisi parmi les 3 tirages
    public abstract int choixCombinaison(Jeu j, int joueur);
    
    //Choisir ou mettre son numéro
    public abstract int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide);
    
    //Choisir quel numéro mettre quand un intérimaire est choisi
    public abstract int choixNumero(Jeu j, int joueur, int numero);

    //Choisir quel lotissement valoriser
    public abstract int valoriseLotissement(Jeu j, int joueur);

    //Choisir ou mettre une barrière
    public abstract int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide);

    //Choisir de valider un plan ou non
    public abstract boolean validePlan(Jeu j, int joueur, int plan);
}
