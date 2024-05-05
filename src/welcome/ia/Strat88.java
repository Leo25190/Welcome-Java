package welcome.ia;
import welcome.*;
import java.util.ArrayList;
//Strat88, Léo Delecroix, 110 pts / 100k parties
//Après pas mal de recherche sur la façon dont placer les numéros le plus efficacement, j'en suis arrivé à les placer à partir d'un écart et d'un tableau idéal.
//Les places valides sont d'abord parcourues dans le sens inverse jusqu'à renvoyer l'écart minimal entre le numéro actuel et le tableau d'emplacements idéaux
//Si un nombre trouve sa place, une pondération lui est accordée en fonction de sa probabilité d'apparition, d'un multiplicateur, ainsi que de sa carte action associé
//La priorité en des actions est dans cet ordre : Parcs > Piscines > Géomètres > Agents Imo.
//Les BIS ont une grande priorité à partir de la 29ème maison placée. Ceux-ci n'ont pas de logique spécifique, on les place juste sur le dernier emplacement des places valides
//Pour les intérimaires, on utilise la même stratégie de placement que pour les autres numéros que l'on répète sur les maximums 5 possibilités offertes par l'action.
//Si un emplacement idéal est trouvé pour n'importe lequel des numéros d'intérimaire, le meilleur d'entre eux est sauvegardé et on donne la pondération correspondante à la probabilité du numéro sauvegardé.
//Pour les barrières, nous faisons des lotissements de 6 et de 1.
//Pour les piscines, on les place là où l'écart avec son emplacement est inférieur à 0.68
//Pour les agents, on augmente les lotissements de 1 et de 6 et ensuite des autres.

public class Strat88 extends Strat{

    private int tour = 0, emplacementMaisonIndex = 0, indexNumero = 0, indexActuelBarrieres = 0, indexActuelLotissements = 0, numeroInterim = 0;
    private final int bisTour = 29;
    private final double[][] basePositions = {
            {1.5, 3.2, 4.5, 6.2, 7.2, 8, 9.46, 10.59, 12, 13.5},
            {2, 3.7, 5.2, 7.3, 8.6, 9.6, 10.7, 12, 13, 14, 15},
            {1, 2.5, 4, 5.2, 6, 7, 8, 9, 10, 11.4, 13, 14.4}
    };
    private final double[] probabilites = {15./81, 10./81, 7./81, 6./81, 5./81, 4./81, 3./81, 3./81, 3./81, 4./81, 5./81, 6./81, 7./81, 10./81, 15./81};
    private final int[] emplacementsPiscines = {2, 6, 7, 103, 107, 201, 206, 210};
    private final int[] emplacementsBarrieres = {206, 106, 6, 110, 109, 108, 107, 7, 8, 9, 5, 4, 3, 2};
    private final int[] upgradesLotissements = {1, 6, 6, 6, 6, 2, 3, 2, 3, 3, 4, 5, 4, 5, 4, 5};

    public Strat88(){
    }

    @Override public String nomVille() { return "Leo's Village"; }
    @Override public String nomJoueur() { return "DELECROIX, Léo"; }
    @Override public int choixCombinaison(Jeu j, int joueur){ indexChoixNumero(j, joueur); return indexNumero; }
    @Override public int choixNumero(Jeu j, int joueur, int numero) { return (numeroInterim > Math.min(numero + 2, 17) || numeroInterim < Math.max(numero - 2, 0)) ? numero : numeroInterim; }
    @Override public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide) { return (emplacementMaisonIndex = getEmplacementMaison(numero, placeValide, ((Travaux) j.actions[indexNumero].top()).getAction())) == -1 ? 0 : emplacementMaisonIndex; }
    @Override public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide) { return tour >= bisTour + j.joueurs[joueur].refusPermis ? placeValide.size() - 1 : 0; }
    @Override public int valoriseLotissement(Jeu j, int joueur) { return (indexActuelLotissements < upgradesLotissements.length) ? upgradesLotissements[indexActuelLotissements++] : 1; }
    @Override public int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide) { return (indexActuelBarrieres < emplacementsBarrieres.length) ? (!placeValide.contains(emplacementsBarrieres[indexActuelBarrieres++]) ? 0 : placeValide.indexOf(emplacementsBarrieres[indexActuelBarrieres - 1])) : (placeValide.size() >= 2 ? 1 : 0); }
    @Override public boolean validePlan(Jeu j, int joueur, int plan) { return true; }
    @Override public void resetStrat(){ tour = emplacementMaisonIndex = indexNumero = indexActuelBarrieres = indexActuelLotissements = numeroInterim = 0; }

    //Méthodes pour les choix
    //Méthode qui cherche l'index idéal dans placesValide
    public int getEmplacementMaison(int numero, ArrayList<Integer> placesValides, int action) {
        if (placesValides.isEmpty()) return -1;
        if (action == 0) {
            double ecart = 100;
            int index = 0;
            for (int emplacementsPiscine : emplacementsPiscines)
                if (placesValides.contains(emplacementsPiscine)) {
                    double t = Math.abs(basePositions[emplacementsPiscine / 100][emplacementsPiscine % 100] - numero);
                    if (t < ecart) {
                        ecart = t;
                        index = placesValides.indexOf(emplacementsPiscine);
                    }
                }
            if (ecart < 0.68) return index;
        }

        double ecart = Math.abs(basePositions[placesValides.getLast() / 100][placesValides.getLast() % 100] - numero);
        int indexEcart = placesValides.size() - 1;
        for (int j = placesValides.size() - 2 ; j >= 0 ; j--) {
            double t = Math.abs(basePositions[placesValides.get(j) / 100][placesValides.get(j) % 100] - numero);
            if (t < ecart) { ecart = t; indexEcart = j; }
        }
        return indexEcart;
    }

    public void indexChoixNumero(Jeu jeu, int joueur) {
        int[] numeros = {((Travaux) jeu.numeros[0].top()).getNumero(), ((Travaux) jeu.numeros[1].top()).getNumero(), ((Travaux) jeu.numeros[2].top()).getNumero()};
        int[] actions = {((Travaux) jeu.actions[0].top()).getAction(), ((Travaux) jeu.actions[1].top()).getAction(), ((Travaux) jeu.actions[2].top()).getAction()};
        double[] poids = {0, 0, 0};

        for (int i = 0 ; i < 3 ; i++) {
            ArrayList<Integer> placesValides = construirePossibilite(numeros[i], jeu.joueurs[joueur]);
            int emplacementIndex = getEmplacementMaison(numeros[i], placesValides, actions[i]);

            if (emplacementIndex >= 0) {
                poids[i] += 65 * (probabilites[numeros[i] - 1]);
                switch(actions[i]) {
                    case 0: if (contains(emplacementsPiscines, placesValides.get(emplacementIndex))) poids[i] += 14; break;
                    case 3: if (jeu.joueurs[joueur].ville.nbParcs[placesValides.get(emplacementIndex) / 100] < 3 + placesValides.get(emplacementIndex) / 100) poids[i] += 16; break;
                    case 4: poids[i] += 10; break;
                    case 1:
                        numeroInterim = numeros[i];
                        for (int j = -2 ; j <= 2 ; j++) {
                            if (numeros[i] + j >= 0) {
                                ArrayList<Integer> placesValidesInterim = construirePossibilite(numeros[i] + j, jeu.joueurs[joueur]);
                                int emplacementInterim = getEmplacementMaison(numeros[i] + j, placesValidesInterim, actions[i]);
                                double poidsActuel = 65 * probabilites[Math.max(0, Math.min(numeros[i] + j - 1, 14))];
                                if (emplacementInterim >= 0 && poidsActuel > poids[i]) {
                                    poids[i] = poidsActuel;
                                    numeroInterim = numeros[i] + j;
                                }
                            }
                        }
                        break;
                    case 5: poids[i] += 12; break;
                    case 2: if (tour >= bisTour + jeu.joueurs[joueur].refusPermis) poids[i] += 15; break;
                }
            }
        }

        int maxIndex = 0;
        for (int i = 1 ; i < 3 ; i++) if (poids[i] > poids[maxIndex]) maxIndex = i;
        tour++;
        indexNumero = maxIndex;
    }

    //Méthodes pour aider à faire les choix
    public ArrayList<Integer> construirePossibilite(int numero, Joueur joueur) {
        ArrayList<Integer> possibilite = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int min = joueur.ville.rues[i].taille - 1;
            while (min >= 0 && (joueur.ville.rues[i].maisons[min].numero == -1 || joueur.ville.rues[i].maisons[min].numero > numero)) min--;
            if (min < 0 || joueur.ville.rues[i].maisons[min].numero != numero) {
                min++;
                while (min < joueur.ville.rues[i].taille && joueur.ville.rues[i].maisons[min].numero == -1) {
                    possibilite.add(min + 100 * i); min++;
                }
            }
        }
        return possibilite;
    }

    public static boolean contains(int[] liste, int valeur) {
        for (int n : liste) if (n == valeur) return true;
        return false;
    }
}