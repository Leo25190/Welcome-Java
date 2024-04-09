package welcome.ia;

import welcome.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Strat88 extends Strat{
    /*private final double[][] basePositions = {
        {6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
        {1.67, 3.52, 4.78, 5.89, 6.85, 7.67, 8.33, 9.15, 10.11, 11.22, 12.48, 14.33}
    };*/
    private final double[][] basePositions = {
            {1.5, 3.5, 5.1, 6.2, 7, 8.44, 9.46, 10.59, 12.09, 14.11},
            {1.78, 3.65, 4.8, 6, 6.8, 8., 8.86, 9.78, 10.86, 12.35, 14.22},
            {1.67, 3, 4.2, 4.8, 6.2, 7, 8, 9.15, 10.11, 11.22, 12.48, 14.33}
    };
    private int tour = 0;
    private int emplacementMaisonIndex = 0;
    private int indexNumero = 0;
    private int indexActuelBarrieres = 0;
    private int indexActuelLotissements = 0;
    private int numeroInterim = 0;
    private final double[] ecartParametre = {3, 1, 2};
    private final double[] probabilites = {9./81, 8./81, 7./81, 6./81, 5./81, 4./81, 3./81, 3./81, 3./81, 4./81, 5./81, 6./81, 7./81, 8./81, 9./81};
    private final int[] emplacementsPiscines = {2, 6, 7, 103, 107, 201, 206, 210};
    private final int[] emplacementsBarrieres = {206, 106, 6, 7, 8, 9};
    private final int[] upgradesLotissements = {6, 6, 6, 6, 1};

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
        tour++;

        indexNumero = indexChoixNumero(j, joueur);
        return indexNumero;
    }

    @Override
    public int choixNumero(Jeu j, int joueur, int numero) {
        return numeroInterim;
    }

    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide) {
        emplacementMaisonIndex = getEmplacementMaison(numero, placeValide);
        return emplacementMaisonIndex == -1 ? 0 : emplacementMaisonIndex;
    }

    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide) {
        return 0;
    }

    @Override
    public int valoriseLotissement(Jeu j, int joueur) {
        if (indexActuelLotissements < upgradesLotissements.length) {
            indexActuelLotissements++;
            return upgradesLotissements[indexActuelLotissements - 1];
        }
        return 1;
    }

    @Override
    public int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide) {
        if (indexActuelBarrieres < emplacementsBarrieres.length) {
            indexActuelBarrieres++;
            return placeValide.indexOf(emplacementsBarrieres[indexActuelBarrieres - 1]);
        }
        return 1;
    }

    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return true;
    }

    @Override
    public void resetStrat(){

    }

    //Méthodes pour les choix

    //Méthode qui cherche l'index idéal dans placesValide
    public int getEmplacementMaison(int numero, ArrayList<Integer> placesValides) {
        if (placesValides.size() == 0 || placesValides == null) {
            return -1;
        }

        /*ArrayList<Integer> placesRues0 = getPossibilitesByRue(placesValides, 0);
        ArrayList<Integer> placesRues1 = getPossibilitesByRue(placesValides, 1);
        ArrayList<Integer> placesRues2 = getPossibilitesByRue(placesValides, 2);
        double ecart = 100;
        int index = -1;

        for (Integer integer : placesRues2) {
            double tempEcart = Math.abs(basePositions[2][integer % 100] - numero);
            if (tempEcart <= ecartParametre[2]) {
                return placesValides.indexOf(integer);
            }
        }

        for (Integer integer : placesRues1) {
            double tempEcart = Math.abs(basePositions[1][integer % 100] - numero);
            if (tempEcart < ecart && tempEcart < ecartParametre[1]) {
                ecart = tempEcart;
                index = placesValides.indexOf(integer);
            }
        }
        if (ecart != 100) {
            return index;
        }

        for (Integer integer : placesRues0) {
            double tempEcart = Math.abs(basePositions[0][integer % 100] - numero);
            if (tempEcart < ecart && tempEcart < ecartParametre[0]) {
                ecart = tempEcart;
                index = placesValides.indexOf(integer);
            }
        }*/

        double ecart = Math.abs(basePositions[placesValides.get(0) / 100][placesValides.get(0) % 100] - numero);
        int indexEcartMin = 0;
        for (int i = 1 ; i < placesValides.size() ; i++) {

            double tempEcart = Math.abs(basePositions[placesValides.get(i) / 100][placesValides.get(i) % 100] - numero);
            if (tour < 15 && contains(emplacementsPiscines, placesValides.get(i))) {
                tempEcart = 100;
            }
            if (tempEcart < ecart) {
                ecart = tempEcart;
                indexEcartMin = i;
            }
        }
        return indexEcartMin;
        //return index;
    }

    public int indexChoixNumero(Jeu jeu, int joueur) {
        //Création de la liste des numéros, des actions et des pondérations de chaque carte
        int[] numeros = {((Travaux) jeu.numeros[0].top()).getNumero(), ((Travaux) jeu.numeros[1].top()).getNumero(), ((Travaux) jeu.numeros[2].top()).getNumero()};
        int[] actions = {((Travaux) jeu.actions[0].top()).getAction(), ((Travaux) jeu.actions[1].top()).getAction(), ((Travaux) jeu.actions[2].top()).getAction()};
        double[] poids = {0, 0, 0};

        //Logique de pondération
        for (int i = 0 ; i < 3 ; i++) {
            ArrayList<Integer> placesValides = construirePossibilite(numeros[i], jeu.joueurs[joueur]);
            int emplacementIndex = getEmplacementMaison(numeros[i], placesValides);

            if (emplacementIndex >= 0) {
                poids[i] += 81 * (probabilites[numeros[i] - 1]);
                switch(actions[i]) {
                    case 0:
                        if (contains(emplacementsPiscines, placesValides.get(emplacementIndex))){
                            poids[i] += 15;
                        }
                        break;
                    case 3:
                        if (jeu.joueurs[joueur].ville.nbParcs[placesValides.get(emplacementIndex) / 100] < 4 + placesValides.get(emplacementIndex) / 100) {
                            poids[i] += 10;
                        }
                        break;
                    case 4:
                        poids[i] += 3;
                        break;
                    case 1:
                        for (int j = -2 ; j < 3 ; j++) {

                            //Création de la liste des possibilités considérées pour l'interimaire
                            ArrayList<Integer> placesValidesInterim = construirePossibilite(numeros[i] + j, jeu.joueurs[joueur]);
                            //Recherche du meilleur emplacement pour le numéro modifié choisi
                            int emplacementIndexInterim = getEmplacementMaison(numeros[i] + j, placesValidesInterim);
                            double poidsInterim = 0;

                            //Recherche de la probabilité de pouvoir placer le numéro choisi
                            int indexProbabilite = numeros[i] + j - 1;
                            if (indexProbabilite < 0) {
                                indexProbabilite = 0;
                            } else if (indexProbabilite >= probabilites.length) {
                                indexProbabilite = probabilites.length - 1;
                            }

                            //On regarde si un emplacement idéal a été trouvé pour l'interimaire
                            if (emplacementIndexInterim >= 0) {
                                poidsInterim = 81 * (probabilites[indexProbabilite]);
                            }

                            if (poidsInterim > poids[i]) {
                                poids[i] = 81 * (probabilites[indexProbabilite]);
                                numeroInterim = numeros[i] + j;
                                System.out.println("Numero interim: " + numeroInterim);
                            } else if (placesValidesInterim.size() > 0) {
                                numeroInterim = numeros[i] + j;
                                poids[i] = 81 * (probabilites[indexProbabilite]);
                            }
                        }
                }
            }
        }
        System.out.println("\n\nPoids : " + Arrays.toString(poids) + "\n");
        //On cherche l'index du numéro le plus grand
        int maxIndex = 0;
        for (int i = 1 ; i < 3 ; i++) {
            if (poids[i] > poids[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    //Méthodes pour aider à faire les choix
    public ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList<>();

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

    public static ArrayList<Integer> getPossibilitesByRue(ArrayList<Integer> possibilites, int rue){
        ArrayList<Integer> possibilitesByRue = new ArrayList<>();
        for (int i = 0; i < possibilites.size(); i++) {
            if (possibilites.get(i) / 100 == rue) {
                possibilitesByRue.add(possibilites.get(i));
            }
        }
        return possibilitesByRue;
    }

    public static boolean contains(int[] liste, int valeur) {
        for (int n : liste) {
            if (n == valeur) {
                return true;
            }
        }
        return false;
    }
}
