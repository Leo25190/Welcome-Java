package welcome.ia;

import welcome.Jeu;
import welcome.Joueur;
import welcome.Travaux;

import java.util.ArrayList;

public class Strat234 extends Strat{
    //PIERARD Jean-Baptiste
    //Ma stratégie est la suivante :
    //1. On choisit un numéro suivant si il peut être placé et qu'il contient un parc, sinon un géomètre, sinon un agent immobilier
    //2. Une fois le numéro choisi, on le place au mieux en fonction de l'index de la maison dans la rue et du numéro à placer
    public Strat234(){
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

    //Méthode permettant de savoir si un numéro peut être placé
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
        // On récupère les actions ainsi que les numeros

        //Parcourt des numéros pour voir si il est plaçable
        //Vérification pour les parcs
        for (int i=0;i<actions.length;i++){
            ArrayList<Integer> emplacements = construirePossibilite(numeros[i], j.joueurs[joueur]);
            int placement = choixEmplacement(j, joueur, numeros[i], emplacements);
            if (actions[i] == 3 && placement != 0 && j.joueurs[joueur].ville.nbParcs[placement / 100] < placement/100 + 2) {
                return i;
            }
        }

        //Vérification pour les géomètres
        for (int i= 0;i<actions.length;i++){
            ArrayList<Integer> emplacements = construirePossibilite(numeros[i], j.joueurs[joueur]);
            if (actions[i] == 5 && peutConstruire(numeros[i], emplacements)) {
                return i;
            }
        }

        //Vérification pour les agents immobiliers
        for (int i= 0;i<actions.length;i++){
            ArrayList<Integer> emplacements = construirePossibilite(numeros[i], j.joueurs[joueur]);
            if (actions[i] == 4 && peutConstruire(numeros[i], emplacements)) {
                return i;
            }
        }


        //Si aucun numéro n'est trouvé avec les actions voulues, alors on utilise le numéros qui peut être placé le plus loin de 8
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

    //Pour le choix de l'emplacement, on se base sur l'index de la maison à telle rue avec un écart de 2 pour les rues 1 et 3 et un écart de 4 pour la rue 2
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
    

    //On retourne le même numéro que celui qui a été choisi
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return numero;
    }
    

    //On fait des lotissements de 6 et de 1 et éventuellement de 2 donc on augmente le prix de ceux-ci
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        int[] avancementPrixLotissement = j.joueurs[joueur].ville.avancementPrixLotissement;
        return (avancementPrixLotissement[0] < 1) ? 1 :
                (avancementPrixLotissement[5] < 4) ? 6 : 2;
    }
    
    //Ici on fait des lotissements de 6 et de 1, si notre tableau de barrières est complétement utilisé, alors on met des maisons à chaque fois au début de placeValide
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
