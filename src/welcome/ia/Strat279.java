package welcome.ia;
import java.util.ArrayList;
import java.util.Collections;

import welcome.Jeu;
import welcome.utils.RandomSingleton;
import welcome.Travaux;
import welcome.utils.Couleur;
import welcome.Joueur;

public class Strat279 extends Strat {
    private int tour = 0;
    private int numeroInterim;
    public Strat279(){
    }
    
    @Override
    public String nomVille(){
        return "Ville de Lily";
    }
    
    @Override
    public String nomJoueur(){
        return "TILHAC, Lily";
    }
    
    //On importe la méthode construirePossibilite car elle est privée dans la classe Jeu mais on en aura besoin dans la strat
    private ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList<>(); //List des possibilités Ã  construire
        for(int i=0; i<3; i++){//Pour chaque rue
            min=joueur.ville.rues[i].taille-1;
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

    private boolean peutPlacer(Joueur joueur, int numero) {
        ArrayList<Integer> placeValide = construirePossibilite(numero, joueur);
        for (int i = placeValide.size() - 1 ; i >= 0 ; i--) {
            switch(placeValide.get(i) / 100) {
                case 0:
                    if (placeValide.get(i) % 100 + 1 == numero) return true;
                    break;
                case 1:
                    if (placeValide.get(i) % 100 + 5 == numero) return true;
                    break;
                case 2:
                    if (placeValide.get(i) % 100 + 2 == numero) return true;
                    break;
            }
        }
        return false;
    }


    //Choisir parmi les 3 numéros en fonction de la carte travaux (parcs> piscine > barrières > agent) et qi il n'y a pas ces cartes alors on choisit en focntion du numéro
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        tour++;
        //On met les 3 duos numéros et actions dans un tableau à deux dimensions
        int [][]number_action = new int[][]{
            {((Travaux)j.numeros[0].top()).getNumero(),((Travaux)j.actions[0].top()).getAction()},
            {((Travaux)j.numeros[1].top()).getNumero(),((Travaux)j.actions[1].top()).getAction()},
            {((Travaux)j.numeros[2].top()).getNumero(),((Travaux)j.actions[2].top()).getAction()}
        };

        //On crée le tableau des emplacements idéaux
        int[] choixEmplacements = new int[3];
        boolean[] emplacementTrouve = new boolean[3];

        //On crée un tableau de listes de places valides pour les prendre en compte
        ArrayList<ArrayList<Integer>> placesValides = new ArrayList<>();
        for (int i = 0 ; i < 3 ; i++) {
            placesValides.add(construirePossibilite(number_action[i][0], j.joueurs[joueur]));
            choixEmplacements[i] = choixEmplacement(j, joueur, number_action[i][0], placesValides.get(i));
            emplacementTrouve[i] = peutPlacer(j.joueurs[joueur], number_action[i][0]);
        }

        //On s'interesse d'abord aux parcs, on cherche si il y a une carte travaux "paysagiste"
        for (int i=0; i<3; i++){
            if (emplacementTrouve[i]) {
                int rue = placesValides.get(i).get(choixEmplacements[i]) / 100;
                if (number_action[i][1]== 3 && j.joueurs[joueur].ville.nbParcs[rue] < rue + 3) return i;
            }
        }
        //On va s'intéresser aux cartes "géomètre"
        for (int i=0; i<3; i++){
            if (number_action[i][1]== 5 && emplacementTrouve[i]) return i;
        }

        //On s'intéresse aux cartes "agent immobilier"
        for (int i=0; i<3; i++){
            if (number_action[i][1]== 4 && emplacementTrouve[i]) return i;
        }

        //On s'intéresse aux interimaires
        for (int i=0 ; i<3;i++) {
            if (number_action[i][1] == 1) {
                for (int k = Math.max(number_action[i][0] - 2, 0); k < number_action[i][0] + 2 ; k++) {
                    if (peutPlacer(j.joueurs[joueur], k)) {
                        numeroInterim = k;
                        return i;
                    }
                }
            }
        }

        // Si on ne trouve aucune des cartes précédentes, on fait en fonction du numéro et on utilise le plus extrème des 3 combinaisons
        int res = 0;
        int ecart = 0;
        for (int i = 0 ; i < 3 ; i++) {
            int ecartActuel = Math.abs(number_action[i][0] - 8);
            if (ecartActuel > ecart && emplacementTrouve[i]) {
                res = i;
                ecart = ecartActuel;
            }
        }
        return res;

    }
    
    //Choisir de placer un numéro bis, 
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return tour > 25 ? placeValide.size() - 1 : 0;
    }
    
    //Choisi l'emplacement en fonction de si c'est possible ou non et le mettre 
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        for (int i = placeValide.size() - 1 ; i >= 0 ; i--) {
            switch(placeValide.get(i) / 100) {
                case 0:
                    if (placeValide.get(i) % 100 + 1 == numero) return i;
                    break;
                case 1:
                    if (placeValide.get(i) % 100 + 5 == numero) return i;
                    break;
                case 2:
                    if (placeValide.get(i) % 100 + 2 == numero) return i;
                    break;
            }
        }
        return 0;
    }
    
    //choix interim en fonction de ce que qu'on a trouvé dans le choixCombinaison
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return Math.abs(numero - numeroInterim) <= 2 ? numeroInterim : numero;
    }
    
    //Valorise les lotissements de taille 5 et 6
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        if (j.joueurs[joueur].ville.avancementPrixLotissement[0] < 1) return 1;
        if (j.joueurs[joueur].ville.avancementPrixLotissement[5] < 4) return 6;
        if (j.joueurs[joueur].ville.avancementPrixLotissement[1] < 2) return 2;
        return 3;
    }
    
    //On va chercher à placer les barrières de manière à faire des lotissements de 5 et de 6 
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){

        if (placeValide.contains(4)){
            return placeValide.indexOf(4);
        }
        if (placeValide.contains(105)){
            return placeValide.indexOf(105);
        }
        if (placeValide.contains(206)){
            return placeValide.indexOf(206);
        }
        if (placeValide.contains(104)){
            return placeValide.indexOf(104);
        }
        if (placeValide.contains(103)){
            return placeValide.indexOf(103);
        }
        if (placeValide.contains(102)){
            return placeValide.indexOf(102);
        }
        if (placeValide.contains(1)){
            return placeValide.indexOf(1);
        }
        if (placeValide.contains(2)){
            return placeValide.indexOf(2);
        }
        if (placeValide.contains(3)){
            return placeValide.indexOf(3);
        }
        if (placeValide.size() >= 2) return 1;
        else return 0;
    }
    
    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return true;
    }
    
    @Override
    public void resetStrat(){ tour = 0;};
}