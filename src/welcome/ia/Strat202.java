package welcome.ia;

import welcome.Jeu;
import welcome.Joueur;
import welcome.Travaux;
import welcome.utils.Couleur;
import welcome.utils.RandomSingleton;

import java.util.ArrayList;

public class Strat202 extends Strat{

    public Strat202(){
    }

    private ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList(); //List des possibilités Ã  construire
        for(int i=0; i<3; i++){//Pour chaque rue
            min=joueur.ville.rues[i].taille-1; //on part de la fin
            while(min>=0  && (joueur.ville.rues[i].maisons[min].numero==-1 || joueur.ville.rues[i].maisons[min].numero > numero))
                min--; // on décrement le min tant qu'on a pas trouvé un numéro <=
            if(min<0 || joueur.ville.rues[i].maisons[min].numero!=numero){

                min++;// On part de la case suivante
                while(min < joueur.ville.rues[i].taille && joueur.ville.rues[i].maisons[min].numero == -1){
                    possibilite.add((Integer)(min+ 100*i)); // on construit les possibilités tant qu'on a des cases vides
                    min++;
                }
            }
        }
        return possibilite;
    }

    private boolean peutConstruire(int numero, ArrayList<Integer> placeValide){
        for (int i = placeValide.size() - 1; i >= 0; i--) {
            int numeroRue = placeValide.get(i) / 100;
            int numeroCase = placeValide.get(i) % 100;

            switch (numeroRue) {
                case 0, 2:
                    if (numeroCase + 2 == numero) {
                        return true;
                    }
                    break;
                case 1:
                    if (numeroCase + 4 == numero) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
    
    @Override
    public String nomVille(){
        return "BidonVille";
    }
    
    @Override
    public String nomJoueur(){
        return "RandMan";
    }


    @Override
    public int choixCombinaison(Jeu j, int joueur){
        int[] numeros = {((Travaux) j.numeros[0].top()).getNumero(), ((Travaux) j.numeros[1].top()).getNumero(), ((Travaux) j.numeros[2].top()).getNumero()};
        int[] actions = {((Travaux) j.actions[0].top()).getAction(), ((Travaux) j.actions[1].top()).getAction(), ((Travaux) j.actions[2].top()).getAction()};

        for (int i=0;i<actions.length;i++){
            ArrayList<Integer> emplacements = construirePossibilite(numeros[i], j.joueurs[joueur]);
            int placement = choixEmplacement(j, joueur, numeros[i], emplacements);
            if (actions[i] == 3 && placement != 0 && j.joueurs[joueur].ville.nbParcs[placement / 100] < placement/100 + 2) {
                return i;
            }
        }

        for (int i= 0;i<actions.length;i++){
            ArrayList<Integer> emplacements = construirePossibilite(numeros[i], j.joueurs[joueur]);
            if (actions[i] == 5 && peutConstruire(numeros[i], emplacements)) {
                return i;
            }
        }

        for (int i= 0;i<actions.length;i++){
            ArrayList<Integer> emplacements = construirePossibilite(numeros[i], j.joueurs[joueur]);
            if (actions[i] == 4 && peutConstruire(numeros[i], emplacements)) {
                return i;
            }
        }

        int index = 0;
        int ecart = 0;
        for (int i=0;i<numeros.length;i++) {
            ArrayList<Integer> emplacements = construirePossibilite(numeros[i], j.joueurs[joueur]);
            int placement = choixEmplacement(j, joueur, numeros[i], emplacements);
            int ecartActuel = !emplacements.isEmpty() ? Math.abs(8 - emplacements.get(placement)) : 0;
            if (ecartActuel > ecart && peutConstruire(numeros[i], emplacements)) {
                index = i;
                ecart = ecartActuel;
            }
        }
        return index;
    }
    

    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return 0;
    }
    

    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        for (int i = placeValide.size() - 1; i >= 0; i--) {
            int numeroRue = placeValide.get(i) / 100;
            int numeroCase = placeValide.get(i) % 100;

            switch (numeroRue) {
                case 0, 2:
                    if (numeroCase + 2 == numero) {
                        return i;
                    }
                    break;
                case 1:
                    if (numeroCase + 4 == numero) {
                        return i;
                    }
                    break;
            }
        }
        return 0;
    }
    

    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return numero;
    }
    

    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        int[] avancementPrixLotissement = j.joueurs[joueur].ville.avancementPrixLotissement;
        return (avancementPrixLotissement[0] < 1) ? 1 :
                (avancementPrixLotissement[5] < 4) ? 6 : 2;
    }
    
    //Met une barrière à une position aléatoire
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        int[] elementsToCheck = {206, 106, 6, 107, 108, 109, 110, 7, 8, 9};
        for (int element : elementsToCheck) {
            if (placeValide.contains(element)) {
                return placeValide.indexOf(element);
            }
        }
        if (placeValide.size() >= 2) {
            return 1;
        }
        return 0;
    }
    
    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return true;
    }
    
    @Override
    public void resetStrat(){};
}
