package welcome;

import welcome.utils.ClavierSingleton;
import java.util.ArrayList;
import welcome.ia.Strat;

//Classe JoueurHumain
//Doit implémenter Stratégie et ses méthodes (dù à l'héritage de Joueur)
public class Bot extends Joueur{
    
    Strat strat;
    
    //Constructeur nécéssitant le nom du joueur ainsi que le nom de sa ville.
    public Bot(Strat _strat, String _nom, String _nomVille){
        super(_nom, _nomVille);
        strat=_strat;
        verbose=true; //verbose mode actif pour voir le jeu
    }
    
    //Choix de la combinaison de carte Travaux 0, 1 ou 2
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        return strat.choixCombinaison(j, joueur);
    }
    
    //Choix de l'emplacement pour poser son numéro
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        return strat.choixEmplacement(j, joueur, numero, placeValide);
    }
    
    //Choix de placement du numéro bis dans le cas où une action bis à été choisie
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return strat.choixBis(j, joueur, placeValide);
    }
    
    //Choix du numéro dans le cas où un intérimaire à été choisi
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return strat.choixNumero(j, joueur, numero);
    }
    
    //Choix du lotissment à valoriser
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        return strat.valoriseLotissement(j, joueur);
    }
    
    //Choix de l'endroit où mettre une barrière
    @Override
    public int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return strat.choixBarriere(j, joueur, placeValide);
    }
    
    //Choix pour valider un plan ou non
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan){
        return strat.validePlan(j, joueur, plan);
    }
    
    @Override
    public void resetStrat(){
        strat.resetStrat();
    };
    
}
