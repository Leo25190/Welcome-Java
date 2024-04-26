// PARREL Robin
// Strat226
package welcome.ia;
import java.util.ArrayList;
import welcome.Jeu;
import welcome.Joueur;
import welcome.Travaux;

//Cette stratégie utilise un plateau idéal pour placer au mieux les numéros.
//Elle vérifie ensuite si on peut placer une piscine, sinon si on peut placer un parc, sinon si on peut placer un géomètre et sinon si on peut placer un agent.
//Si elle ne peut placer aucun de ces choix, alors elle prend le numéro le plus extrème qu'elle peut placer
public class Strat226 extends Strat{
    private final int[][] ideal = {{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}};
    private int numeroChoisi = 0;

    public Strat226() {}

    // Copie de construirePossibilite
    private ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){ // Copie de construirePossibilite
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

    @Override
    public String nomVille(){
        return "Gotham City";
    }

    @Override
    public String nomJoueur(){
        return "Robin Parrel";
    }

    //Choix en fonction des différentes conditions
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        int[] numeros = {((Travaux) j.numeros[0].top()).getNumero(), ((Travaux) j.numeros[1].top()).getNumero(), ((Travaux) j.numeros[2].top()).getNumero()};
        int[] actions = {((Travaux) j.actions[0].top()).getAction(), ((Travaux) j.actions[1].top()).getAction(), ((Travaux) j.actions[2].top()).getAction()};

        //On vérifie d'abord si une piscine peut être placée sur un emplacement idéal
        for (int i = 0 ; i < 3 ; i++) {
            ArrayList<Integer> placeValide = construirePossibilite(numeros[i], j.joueurs[joueur]);

            if (actions[i] == 0) {
                int[] piscines = {210, 206, 201, 107, 103, 7, 2, 6};
                for (int position : placeValide) {
                    int rue = position / 100;
                    int maison = position % 100;
                    boolean piscine = false;
                    for (int k = 0; k < 8; k++) {
                        if (position == piscines[k]) {
                            piscine = true;
                        }
                    }
                    if (piscine && numeros[i] == ideal[rue][maison]) {
                        numeroChoisi = placeValide.contains(position) ? placeValide.indexOf(position) : 0;
                        return i;
                    }
                }
            }
        }

        //Vérification sur les parcs
        for (int i = 0 ; i < 3 ; i++) {
            if (actions[i] == 3) {
                ArrayList<Integer> placeValide = construirePossibilite(numeros[i], j.joueurs[joueur]);
                for (int k = placeValide.size() -1 ; k >= 0 ; k--) {
                    int rue = placeValide.get(k) / 100;
                    int maison = placeValide.get(k) % 100;
                    if (ideal[rue][maison] == numeros[i] && j.joueurs[joueur].ville.nbParcs[rue] < 3 + rue) {
                        numeroChoisi = k;
                        return i;
                    }
                }
            }
        }

        //Vérification sur les géomètres
        for (int i = 0 ; i < 3 ; i++) {
            if (actions[i] == 5) {
                ArrayList<Integer> placeValide = construirePossibilite(numeros[i], j.joueurs[joueur]);
                for (int k = placeValide.size() -1 ; k >= 0 ; k--) {
                    int rue = placeValide.get(k) / 100;
                    int maison = placeValide.get(k) % 100;
                    if (ideal[rue][maison] == numeros[i]) {
                        numeroChoisi = k;
                        return i;
                    }
                }
            }
        }

        //Vérification sur les agents immo
        for (int i = 0 ; i < 3 ; i++) {
            if (actions[i] == 4) {
                ArrayList<Integer> placeValide = construirePossibilite(numeros[i], j.joueurs[joueur]);
                for (int k = placeValide.size() -1 ; k >= 0 ; k--) {
                    int rue = placeValide.get(k) / 100;
                    int maison = placeValide.get(k) % 100;
                    if (ideal[rue][maison] == numeros[i]) {
                        numeroChoisi = k;
                        return i;
                    }
                }
            }
        }

        //Recherche du numéro plaçable le plus extrème
        int placeValideIndex = 0;
        int numeroIndex = 0;
        int ecart = 0;
        for (int i = 0 ; i < 3 ; i++) {
            ArrayList<Integer> placeValide = construirePossibilite(numeros[i], j.joueurs[joueur]);
            for (int k = placeValide.size() -1 ; k >= 0 ; k--) {
                if (ideal[placeValide.get(k) / 100][placeValide.get(k) % 100] == numeros[i] && Math.abs(numeros[i] - 8) > ecart) {
                    placeValideIndex = k;
                    numeroIndex = i;
                    ecart = Math.abs(numeros[i] - 8);
                    break;
                }
            }
        }
        numeroChoisi = placeValideIndex;
        return numeroIndex;
    }

    //Rien pour les bis
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return 0;
    }

    //Nous récupérons simplement l'emplacement choisit dans choix combinaison
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        return numeroChoisi;
    }

    //Même numéro
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return numero;
    }

    //On fait des lotissements de 6 donc on les augmente et on augmente en premier les lotissements de 1
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        if (j.joueurs[joueur].ville.avancementPrixLotissement[0] < 1) {
            return 1;
        } else if (j.joueurs[joueur].ville.avancementPrixLotissement[5] < 4) {
            return 6;
        }
        return 2;

    }

    //On fait des lotissements de 6 puis de 1 au début du plateau
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        int[] barrieres = {206, 106, 4, 3, 2, 1, 110, 109, 108, 107, 0};
        if (placeValide.contains(4)) {
            return placeValide.indexOf(4);
        } else if(placeValide.contains(206)) {
            return placeValide.indexOf(206);
        } else if(placeValide.contains(106)) {
            return placeValide.indexOf(106);
        }
        return placeValide.size() >= 2 ? 1 : 0;
    }

    //On valide toujours les plans
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return true;
    }

    //Besoin de rien dans resetStrat
    @Override
    public void resetStrat() {
    }
}
