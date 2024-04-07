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
    private final double ecartParametre = 0.99;

    public Strat88(){
        basePositions = new double[][] {    {6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
                                            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
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

        for (int i = placesValides.size() - 1 ; i >= 0 ; i--) {
            int rue = placesValides.get(i) / 100;
            int emplacement = placesValides.get(i) % 100;
            double ecart = Math.abs(basePositions[rue][emplacement] - numero);
            if (ecart <= ecartParametre) {
                if (numero ==1){
                    return i; }
                return i;
            }
        }

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

    public boolean rueContientNumero(Rue rue, int numero){
        for(int i=0; i<rue.taille; i++){
            if(rue.maisons[i].numero == numero)
                return true;
        }
        return false;
    }
}
