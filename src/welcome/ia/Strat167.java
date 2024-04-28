package welcome.ia;

import welcome.Jeu;
import welcome.Joueur;
import welcome.Travaux;
import welcome.utils.Couleur;

import java.util.ArrayList;

// KASRAOUI Wessim-Abderazak
// Stratégie (n°)
// La stratégie ci-dessous se base sur un tableau idéal. Quand le numéro à placer a une place valide dans la ville alors il est placé en fonction de ce tableau idéal.
// Les actions sont également prises en compte de telle sorte que si une piscine est choisie avec un 8 alors elle sera utilisée avant un 14 avec un bis par exemple.
// Ce système fonctionne avec une pondération en fonction de la rareté d'un numéro (s'il est plaçable) et de l'importance de son action associée.

public class Strat167 extends Strat{
    // On initialise les différentes variables servant à mémoriser différents index
    private int choixPlacesValidesIndex = -1;
    private int indexBarrieres = 0;
    private int indexLotissements = 0;

    public Strat167() {}

    @Override
    public String nomVille(){
        return "WestCoastRanch";
    }

    @Override
    public String nomJoueur(){
        return "KASRAOUI Wessim-Abderazak";
    }

    //Choix du numéro par pondération
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        // La logique de cette méthode prend en compte le numéro (et s'il est plaçable sur le plateau) ainsi que l'action associée pour faire une pondération
        // Nous faisons cette pondération pour chaque pioche
        int[] poidsActions = {0, 0, 0}; // initialisation des poids

        for (int i = 0 ; i < 3 ; i++) {
            int numero = ((Travaux) j.numeros[i].top()).getNumero();
            int action = ((Travaux) j.actions[i].top()).getAction();
            ArrayList<Integer> possibilites = construirePossibilite(numero, j.joueurs[joueur]);

            int index = meilleurChoixIndex(possibilites, numero, action);

            if (index != -1) poidsActions[i] += Math.abs(numero - 8) + 1;

            if (index != -1) { // On vérifie quelle action est associée au numéro actuel pour lui donner une pondération en fonction
                int[] piscines = {2, 6, 7, 103, 107, 201, 206, 210};
                if (action == 0 && contient(piscines, index)) poidsActions[i] += 20;
                int nbRue = possibilites.get(index) / 100;
                int nbParcs = j.joueurs[joueur].ville.nbParcs[nbRue];
                if (action == 3 && nbParcs < nbRue + 3) poidsActions[i] += 15;
                if (action == 5) poidsActions[i] += 5;
                if (action == 4) poidsActions[i] += 4;
            }
        }
        // Logique de choix de l'index en prenant le poids le plus grand
        int index = 0;
        for (int i = 1 ; i < 3 ; i++) {
            if (poidsActions[i] > poidsActions[index]) index = i;
        }
        // Enregistrement du choix de l'index pour les placesValides
        choixPlacesValidesIndex = meilleurChoixIndex(construirePossibilite(((Travaux) j.numeros[index].top()).getNumero(), j.joueurs[joueur]), ((Travaux) j.numeros[index].top()).getNumero(), ((Travaux) j.actions[index].top()).getAction());
        return index;
    }

    //Pas de logique spécifique pour les bis
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return 0;
    }

    //Pour l'emplacement, il nous suffit de renvoyer l'index obtenu et si aucun n'était trouvé (index = -1), alors on renvoie 0
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        return choixPlacesValidesIndex == -1 ? 0 : choixPlacesValidesIndex;
    }

    //Choisir le même numéro que celui de la carte quand l'action est un intérimaire
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return numero;
    }

    //On valorise nos lotissements en fonction des lotissements que l'on veut créé (cf. choixBarriere)
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        int[] lotissements = {1, 6, 6, 6, 6, 2, 2};
        return lotissements[indexLotissements < lotissements.length - 1 ? indexLotissements++ : indexLotissements];
    }

    //On choisit nos barrières de sorte à faire le maximum de barrière de taille 6 et ensuite on remplit les lotissements de taille autre que 6 à l'aide d'un tableau
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        int[] barrieres = {206, 106, 4, 3, 2, 1, 110, 109, 108, 107, 0};
        int index = -1;
        if (indexBarrieres < barrieres.length) {
            index = placeValide.indexOf(barrieres[indexBarrieres++]);
        }
        return index == -1 ? 0 : index;
    }

    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return true;
    }

    //Mise à jour de nos 3 variables
    @Override
    public void resetStrat() {
        choixPlacesValidesIndex = -1;
        indexBarrieres = 0;
        indexLotissements = 0;
    };


    //Méthodes utiles
    //Copie de construire possibilite servant à faire nos choix de pondération et d'emplacement avant choixEmplacement
    private ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        if(joueur.verbose){
            //System.out.println(joueur.ville);
            System.out.println("Possibilités de placement: position dans la rue (numero du choix à entrer au clavier)");
        }
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList(); //List des possibilités Ã  construire
        for(int i=0; i<3; i++){//Pour chaque rue
            min=joueur.ville.rues[i].taille-1; //on part de la fin
            if(joueur.verbose){
                System.out.print("Rue " + (i+1) + ":");
            }
            while(min>=0  && (joueur.ville.rues[i].maisons[min].numero==-1 || joueur.ville.rues[i].maisons[min].numero > numero))
                min--; // on décrement le min tant qu'on a pas trouvé un numéro <=
            if(min<0 || joueur.ville.rues[i].maisons[min].numero!=numero){

                min++;// On part de la case suivante
                while(min < joueur.ville.rues[i].taille && joueur.ville.rues[i].maisons[min].numero == -1){
                    possibilite.add((Integer)(min+ 100*i)); // on construit les possibilités tant qu'on a des cases vides
                    if(joueur.verbose){
                        if(i==0)
                            System.out.print("\t" + Couleur.CYAN + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        else if(i==1)
                            System.out.print("\t" + Couleur.JAUNE + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        else
                            System.out.print("\t" + Couleur.VERT + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                    }
                    min++;
                }
            }
            if(joueur.verbose){
                System.out.println("");
            }
        }
        return possibilite;
    }

    //Méthode servant à trouver le meilleur index de placesValides en fonction d'un tableau idéal et du numéro choisit
    private int meilleurChoixIndex(ArrayList<Integer> placesValides, int numero, int action) {
        int[] piscines = {2, 6, 7, 103, 107, 201, 206, 210};
        int[][] plateauIdeal= {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
                {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}
        };
        // Si piscine alors on ne s'arrête pas au premier numéro trouvé correspondant à notre plateau idéal
        if (action == 0) {
            for (int i = 0 ; i < placesValides.size() ; i++) {
                if (contient(piscines, placesValides.get(i) / 100) && plateauIdeal[placesValides.get(i) / 100][placesValides.get(i) % 100] == numero) {
                    return i;
                }
            }
        }

        //On parcourt nos rues en sens inverse pour trouver en priorité un emplacement correspondant à notre plateau idéal dans les rues décroissantes
        for (int i = placesValides.size() - 1 ; i >= 0 ; i--) {
            if (plateauIdeal[placesValides.get(i) / 100][placesValides.get(i) % 100] == numero) {
                return i;
            }
        }
        return -1;
    }

    // Méthode servant à vérifier qu'un index est bien compris dans nos emplacement de piscine
    private boolean contient(int[] tab, int num) {
        for (int i = 0 ; i < tab.length ; i++) {
            if (tab[i] == num) return true;
        }
        return false;
    }
}
