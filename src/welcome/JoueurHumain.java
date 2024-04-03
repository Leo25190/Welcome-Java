package welcome;

import welcome.utils.ClavierSingleton;
import java.util.ArrayList;

//Classe JoueurHumain
//Doit implémenter Stratégie et ses méthodes (dù à l'héritage de Joueur)
public class JoueurHumain extends Joueur{
    
    //Constructeur nécéssitant le nom du joueur ainsi que le nom de sa ville.
    public JoueurHumain(String _nom, String _nomVille){
        super(_nom, _nomVille);
        verbose=true; //verbose mode actif pour voir le jeu
    }
    
    //Choix de la combinaison de carte Travaux 0, 1 ou 2
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        return ClavierSingleton.getInstance().nextIntBetween(0, 2);
    }
    
    //Choix de l'emplacement pour poser son numéro
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        return ClavierSingleton.getInstance().nextIntBetween(0, placeValide.size()-1);
    }
    
    //Choix de placement du numéro bis dans le cas où une action bis à été choisie
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return ClavierSingleton.getInstance().nextIntBetween(0, placeValide.size()-1);
    }
    
    //Choix du numéro dans le cas où un intérimaire à été choisi
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return ClavierSingleton.getInstance().nextIntBetween(Math.max(0, numero-2), numero+2);    
    }
    
    //Choix du lotissment à valoriser
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        return ClavierSingleton.getInstance().nextIntBetween(1,6);
    }
    
    //Choix de l'endroit où mettre une barrière
    @Override
    public int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return ClavierSingleton.getInstance().nextIntBetween(0, placeValide.size()-1); 
    }
    
    //Choix pour valider un plan ou non
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan){
        return ClavierSingleton.getInstance().nextBoolean();
    }
    
    //Inutile pour un joueur humain
    @Override
    public void resetStrat(){};
    
}
