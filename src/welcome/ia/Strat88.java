package welcome.ia;

import welcome.Jeu;
import welcome.Joueur;
import welcome.Rue;
import welcome.Travaux;

import java.util.ArrayList;
import java.util.Arrays;

public class Strat88 extends Strat{
    public final int[][] basePositions = {  {-1, -1, -1, -1, 10, 11, 12, 13, 14, 15},
                                            {1, 2, 3, 4, 5, 6, -1, -1, -1, -1, -1},
                                            {1, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 15}};
    public final int[][][] basePositions2 = {   { {-1}, {-1}, {-1}, {-1}, {10, 9}, {11, 10}, {12, 11}, {13, 12}, {14, 13}, {15, 14} },
                                                { {1, 2}, {2, 3}, {3, 4}, {4, 5}, {6, 7}, {-1}, {-1}, {-1}, {-1}, {-1}, {-1} },
                                                { {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8, 9, 10}, {11, 10}, {12, 11}, {13, 12}, {14, 13}, {15, 14} }};

    public final int[][] rue0Base = { {-1}, {-1}, {-1}, {-1}, {10, 9}, {11, 10}, {12, 11}, {13, 12}, {14, 13}, {15, 14} };
    public final int[][] rue1Base = { {1, 2}, {2, 3}, {3, 4}, {4, 5}, {6, 7}, {-1}, {-1}, {-1}, {-1}, {-1}, {-1} };
    public final int[][] rue2Base = { {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8, 9, 10}, {11, 10}, {12, 11}, {13, 12}, {14, 13}, {15, 14} };

    public Strat88(){
    }

    @Override
    public String nomVille() {
        return "Ville de fou malade pupuce";
    }

    @Override
    public String nomJoueur() {
        return "Nom de fou malade pupuce";
    }

    @Override
    public int choixCombinaison(Jeu j, int joueur){
        //TODO pondérer le choix des rues
        //Remplir la liste des numeros affichés.
        int[] numeros = new int[]{((Travaux) j.numeros[0].top()).getNumero(), ((Travaux) j.numeros[1].top()).getNumero(), ((Travaux) j.numeros[2].top()).getNumero()};
        int[] classementIndex = trierNumerosIndex(numeros);

        //Test sur les cartes par ordre d'importance
        for (int i = 0 ; i < 3 ; i++) {
            //Parcourir chaque rue et voir si la carte choisie est plaçable dans les emplacements par défaut
            for (int k = 2 ; k >= 0 ; k--){
                int indexMaison = trouverIndexMaison(k, numeros[classementIndex[i]]);
                //Trouver la carte où l'emplacement de maison par défaut est disponible
                if (j.joueurs[joueur].ville.rues[k].maisons[indexMaison].estVide()) {
                    return classementIndex[i];
                }
            }
        }
        

        //TODO faire d'autres choix dans le cas où les nombres ne respectent pas les cases par défaut
        return 0;
    }

    @Override
    public int choixNumero(Jeu j, int joueur, int numero) {
        return numero;
    }

    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide) {
        //Parcourt des rues pour trouver la première place valide dans la plus grande rue possible
        for (int i = 2 ; i >= 0 ; i--) {
            //Recherche du placement idéal pour la rue :
            int positionIdeale = -1;
            for (int k = 0 ; k < basePositions[i].length ; k++) {
                if (basePositions[i][k] == numero) {
                    positionIdeale = 100 * i + k;
                }
            }

            //Recherche de l'index de la position idéale
            if (positionIdeale != -1) {
                for (int k = 0 ; k < placeValide.size() ; k++) {
                    if (positionIdeale == placeValide.get(k)) {
                        return k;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide) {
        return 0;
    }

    @Override
    public int valoriseLotissement(Jeu j, int joueur) {
        return 1;
    }

    @Override
    public int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide) {
        return 1;
    }

    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return false;
    }

    @Override
    public void resetStrat(){

    }

    //Utilitaires
    public static int[] trierNumerosIndex(int[] numeros){
        //Création du tableau des distances à 8
        int[] distances = new int[3];
        for (int i = 0 ; i < 3 ; i++){
            distances[i] = Math.abs(8 - numeros[i]);
        }

        //Création du tableau des index
        int[] index = new int[]{0, 1, 2};
        //Tri des numeros du plus loin au plus proche de 8
        for (int i = 0; i < numeros.length - 1; i++) {
            for (int j = i + 1; j < numeros.length; j++) {
                if (distances[j] > distances[i]) {
                    int temp = index[j];
                    index[j] = index[i];
                    index[i] = temp;

                    temp = distances[j];
                    distances[j] = distances[i];
                    distances[i] = temp;
                }
            }
        }
        return index;
    }

    public int trouverIndexMaison(int numeroRue, int numeroMaison) {
        //Parcourir la rue choisie et voir quel index de la rue correspond au numéro de maison à placer
        for (int i = 0 ; i < 10 + numeroRue ; i++) {
            if (basePositions[numeroRue][i] == numeroMaison) {
                return i;
            }
        }
        return 0;
    }

    public ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList<>(); //List des possibilités Ã  construire

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

    public static int[][] updateRueBase(Rue rue, int[][] rueBase) {
        // Si un tel numéro est déjà placé dans la rue, mettre -2, à tous les autres endroits où ce numéro apparaît
        // Si tel numéro, ne peut plus être plaçable à tel emplacement, mettre -2 également

        // Création de la liste des numéros déjà posés
        ArrayList<Integer> listeNum = new ArrayList<>();
        for (int i = 0 ; i < rue.taille ; i++){
            if(!rue.maisons[i].estVide()) {
                listeNum.add(rue.maisons[i].numero);
            }
        }

        // Mise à jour de la rue avec les numéros déjà posés
        for (Integer num : listeNum) {
            for (int j = 0; j < rueBase.length; j++) {
                for (int k = 0; k < rueBase[j].length; k++) {
                    if (rueBase[j][k] == num) {
                        rueBase[j][k] = -2;
                    }
                }
            }
        }

        // Mettre un -2 partout dans les endroits où il y à déjà un numéro
        for (int i = 0; i < rue.taille; i++) {
            if (!rue.maisons[i].estVide()) {
                Arrays.fill(rueBase[i], -2);
            }
        }

        return rueBase;
    }
}
