//Léo Marquant
// Je mets d'abord les parcs, ensuite les lotissements et ensuite les agents. J'essaye aussi de placer au maximum correctement mes numéros
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
    
    @Override
    public String nomVille(){
        return "Ville du Vélo";
    }
    
    @Override
    public String nomJoueur(){
        return "MARQUANT, Léo";
    }


    @Override
    public int choixCombinaison(Jeu j, int joueur){
        int[] numeros = new int[3];
        int[] actions = new int[3];

        // Extraction des numéros et des actions
        for (int i = 0; i < 3; i++) {
            numeros[i] = ((Travaux) j.numeros[i].top()).getNumero();
            actions[i] = ((Travaux) j.actions[i].top()).getAction();
        }

        //Je donne un poids à mes actions si les numéros associés sont posables
        int[] placements = new int[3];
        int[] choixPoids = new int[3];
        for (int i = 0; i< numeros.length;i++) {
            placements[i] = choixEmplacement(j, joueur, numeros[i], construirePossibilite(numeros[i], j.joueurs[joueur]));
            if (placements[i] != 0) {
                choixPoids[i] = Math.abs(numeros[i] - 8);
                switch (actions[i]) {
                    case 3:
                        choixPoids[i] += 15;
                        break;
                    case 5:
                        choixPoids[i] += 6;
                        break;
                    case 4:
                        choixPoids[i] += 5;
                        break;
                }
            }
        }

        // Je trie mes différentes cartes par ordre d'importance
        int index = 0;
        int max = choixPoids[0];
        for (int i = 1; i < numeros.length; i++) {
            if (choixPoids[i] > max) {
                index = i;
                max = choixPoids[i];
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
        //Avec mes trois tableaux idéaux, j'essaye de placer au maximum bien mes numéros choisis
        int[] emplacementsIdeaux1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] emplacementsIdeaux2 = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] emplacementsIdeaux3 = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        for (int i = placeValide.size() - 1 ; i >= 0 ;i--){
            int numeroRue = placeValide.get(i) / 100;
            int numeroCase = placeValide.get(i) % 100;
            switch(numeroRue){
                case 0:
                    if (numero == emplacementsIdeaux1[numeroCase]) return i;
                    break;
                case 1:
                    if (numero == emplacementsIdeaux2[numeroCase]) return i;
                    break;
                case 2:
                    if (numero == emplacementsIdeaux3[numeroCase]) return i;
                    break;
            }
        }
        return 0;
    }
    

    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return numero;
    }
    
    //Je valorise les lotissements de 6 et de 1
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        if (j.joueurs[joueur].ville.avancementPrixLotissement[0] < 1) {
            return 1;
        }
        return 6;
    }
    
    //Comme mes numéros ne se placent pas très bien dans la première rue, je place des lotissements de 6 en rue 2 et 3 et des lotissements de 1 dans les emplacements restant en rue 2 et je finis par compléter la rue 1 avec des lotissements de 1
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        if (placeValide.contains(105)) return placeValide.indexOf(105);
        if (placeValide.contains(206)) return placeValide.indexOf(206);
        if (placeValide.contains(101)) return placeValide.indexOf(101);
        if (placeValide.contains(102)) return placeValide.indexOf(102);
        if (placeValide.contains(103)) return placeValide.indexOf(103);
        if (placeValide.contains(104)) return placeValide.indexOf(104);
        return placeValide.size() >= 3 ? 1 : 0;
    }
    
    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return true;
    }
    
    @Override
    public void resetStrat(){};

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
}
