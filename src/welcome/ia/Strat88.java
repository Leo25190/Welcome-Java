package welcome.ia;

import welcome.Jeu;
import welcome.Joueur;
import welcome.Rue;
import welcome.Travaux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Strat88 extends Strat{
    private double[][] basePositions;
    private int emplacementMaisonIndex;
    private final double[] ecartParametre = {4, 3, 1};
    private final double[] probabilites = {3./81, 3./81, 4./81, 5./81, 6./81, 7./81, 8./81, 9./81, 8./81, 7./81, 6./81, 5./81, 4./81, 3./81, 3./81};

    public Strat88(){
        basePositions = new double[][] {    {2.73, 5.73, 7.6, 9, 10, 11, 12, 13, 14, 15},
                                            {1, 2, 3, 4, 5, 6, 7, 8.2, 9.49, 11.2, 13.69},
                                            {1.67, 3.52, 4.78, 5.89, 6.85, 7.67, 8.33, 9.15, 10.11, 11.22, 12.48, 14.33}};
        emplacementMaisonIndex = -1;

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
        //Remplir la liste des numeros affichés.
        int[] numeros = new int[]{((Travaux) j.numeros[0].top()).getNumero(), ((Travaux) j.numeros[1].top()).getNumero(), ((Travaux) j.numeros[2].top()).getNumero()};
        int[] classementIndex = trierNumerosIndex(numeros);


        //Tests pour vérifier si la carte choisie est plaçable
        for (int i = 0 ; i < 3 ; i++) {
            for (int k = 0 ; k < 3 ; k++) {
                Travaux action = (Travaux) j.actions[classementIndex[k]].top();
                if (action.action == 3) {
                    int numeroActuel = numeros[classementIndex[k]];
                    ArrayList<Integer> placesValides = construirePossibilite(numeroActuel, j.joueurs[joueur]);
                    emplacementMaisonIndex = getEmplacementMaison(numeroActuel, placesValides);
                    if (emplacementMaisonIndex != -1) {
                        return classementIndex[k];
                    }
                }
            }

            //On cherche l'emplacement de la maison correspondant à nos critères. Si elle ne peut être placée, on met emplacementMaison à - 1
            int numeroActuel = numeros[classementIndex[i]];
            ArrayList<Integer> placesValides = construirePossibilite(numeroActuel, j.joueurs[joueur]);
            emplacementMaisonIndex = getEmplacementMaison(numeroActuel, placesValides);
            if (emplacementMaisonIndex != -1) {
                return classementIndex[i];
            }
        }

        return 0;
    }

    @Override
    public int choixNumero(Jeu j, int joueur, int numero) {
        return numero;
    }

    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide) {
        return emplacementMaisonIndex == -1 ? 0 : emplacementMaisonIndex;
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
        return true;
    }

    @Override
    public void resetStrat(){

    }

    //Utilitaires

    //Méthode qui cherche l'index idéal dans placesValide
    public int getEmplacementMaison(int numero, ArrayList<Integer> placesValides) {
        if (placesValides == null) {
            return -1;
        }

        ArrayList<Integer> placesRues0 = getPossibilitesByRue(placesValides, 0);
        ArrayList<Integer> placesRues1 = getPossibilitesByRue(placesValides, 1);
        ArrayList<Integer> placesRues2 = getPossibilitesByRue(placesValides, 2);
        double ecart = 100;
        int index = -1;

        for (int i = 0 ; i < placesRues2.size() ; i++) {
            double tempEcart =  Math.abs(basePositions[2][placesRues2.get(i) % 100] - numero);
            if (tempEcart < ecart && tempEcart <= ecartParametre[2] * (3 * (1 - probabilites[numero - 1]))) {
                ecart = tempEcart;
                index = placesValides.indexOf(placesRues2.get(i));
            }
        }
        if (ecart != 100) {
            return index;
        }

        for (int i = 0 ; i < placesRues1.size() ; i++) {
            double tempEcart =  Math.abs(basePositions[1][placesRues1.get(i) % 100] - numero);
            if (tempEcart < ecart && tempEcart < ecartParametre[1]) {
                ecart = tempEcart;
                index = placesValides.indexOf(placesRues1.get(i));
            }
        }
        if (ecart != 100) {
            return index;
        }

        for (int i = 0 ; i < placesRues0.size() ; i++) {
            double tempEcart =  Math.abs(basePositions[0][placesRues0.get(i) % 100] - numero);
            if (tempEcart < ecart && tempEcart < ecartParametre[0]) {
                ecart = tempEcart;
                index = placesValides.indexOf(placesRues0.get(i));
            }
        }
        if (ecart != 100) {
            return index;
        }

        /*for (int i = placesValides.size() - 1 ; i >= 0 ; i--) {
            int rue = placesValides.get(i) / 100;
            int emplacement = placesValides.get(i) % 100;
            double ecart = Math.abs(basePositions[rue][emplacement] - numero);
            if (ecart <= ecartParametre[rue]) {
                return i;
            }
        }*/

        return -1;
    }

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

    public static int getIndexOfPossibilite(ArrayList<Integer> possibilites, int emplacement){
        for (int i = 0; i < possibilites.size(); i++) {
            if (possibilites.get(i) == emplacement) {
                return i;
            }
        }
        return -1;
    }
}
