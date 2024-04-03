package welcome.ia;
import java.util.ArrayList;
import welcome.*;

public interface Strategie {
    
    //Faire un choix parmi les trois Combinaisons
    //return 0 1 ou 2
    public abstract int choixCombinaison(Jeu j, int joueur);
    
    //Faire un choix de Numéro quand action Interim choisie
    //return X tel que max(0, numero -2) <= X <= numero +2 
    public abstract int choixNumero(Jeu j, int joueur, int numero);
    
    //Faire un choix pour placer "numero" parmi "placeValide": list d'entiers correspondant à (100 * Rue + position)
    //return l'indice de "placeValide" voulu
    public abstract int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide);
    
    //Faire un choix pour placer un numero bis
    //return l'indice de "placeValide" voulu
    public abstract int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide);
    
    //Choisir la taille des Lotissements à promouvoir 
    //return un entier entre 1 et 6 (checker que pas déjà au max, sinon coup dans le vide)
    public abstract int valoriseLotissement(Jeu j, int joueur);
    
    //Choisir ou mettre une barrière (TODO comment gérer les barrière)
    public abstract int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide);
    
    //Choisir si oui ou non nous voulons valider le plan en question
    //return vrai(oui) ou faux(non)
    public abstract boolean validePlan(Jeu j, int joueur, int plan);
    
    //Permet de reset certaines variables (est appelée lors des Tournois après chaque fin de partie).
    public abstract void resetStrat();
}
