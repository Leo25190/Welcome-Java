/*
########################################################
-------------------STRATEGIE 241------------------------

Cette stratégie se base sur un plateau ideal, et cherche à placer les numéros en respectant ce plan.
Lotissements : 4x6 et 9x1
Score moyen : 95
########################################################
 */
package welcome.ia;

import welcome.*;
import welcome.utils.RandomSingleton;

import java.util.ArrayList;
import java.util.Collections;

public class Strat241 extends Strat{
    // bot de la mort qui tue
    final boolean affichage_decisions = false;
    int[] nombre_parcs; //Compte le nombre de parcs par ligne
    int nombre_agents;  //Compte le nombre d'agents immobilisers utilisés
    int nombre_barrieres;   //Compte le nombre de barrières placées
    static int nombre_bis; //compte les bis

    final static double[][] plateau_ideal = new double[][] {   //Création d'un plateau idéal
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
        {3.4, 4.4, 5.4, 6.4, 7.4, 8.4, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6}
    };
    final static double max_ecart = 2.5;  //Pour les fonctions de recherche de minimum pour la dernière rue
    int[] pioche_choisie; // [0] = action, [1] = numero
    int emplacement_choisi; //Emplacement préférable
    int emplacement_gap;    //Emplacement d'un trou entre deux nombres si on en trouve un
    static int valeur_interimaire; //Valeur ajoutée au numéro dans le cas d'un intérimaire

    final static int[][] emplacement_piscine_optimale = new int[][] {   //Les emplacements ou on veut placer des piscines sur les deux premières rues
            {2, 6, 7},
            {0, 3, 7}
    };
    final static int[][] numero_piscine_optimale = new int[][] {    //Les numéros qu'on veut pour les piscines sur les deux premières rues (correspondantes à l'emplacement optimal)
            {3, 7, 8},
            {5, 8, 12}
    };
    final static int[] nombre_parcs_max = new int[] {3, 4, 5};  //Le nombre max de parcs par rue
    final static int[] valorisations_lotissement_optimales = new int[] {6, 6, 6, 6, 1, 5, 5, 5, 5, 2, 2, 3, 3, 3, 4, 4, 4, 4, 0}; //L'ordre de valorisation des lotissements, ici d'abord les 6 puis les 1
    final static int nombre_agents_necessaires = 5; //Le nombre d'agents immobiliers nécessaires pour mener à bien la stratégie
    final static int[] choix_barriere_optimale = new int[] {206, 106, 6, 110, 109, 108, 107, 9, 8, 7, 0}; //Les choix de placement de barrières dans l'ordre, ici pour former des lotissements 4x6 et 9x1
    final static int nombre_bis_max = 3;

    public Strat241() {
        this.nombre_parcs = new int[3];
        this.nombre_agents = 0;
        this.nombre_barrieres = 0;
        this.nombre_bis = 0;

        this.pioche_choisie = new int[2];
    }
    
    @Override
    public String nomVille(){
        return "Los Angeles";
    }
    
    @Override
    public String nomJoueur(){
        return "JulesLaBulle1";
    }
    
    //Choisit la meilleure pioche
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        int res = -1;
        emplacement_choisi = -1;
        emplacement_gap = -1;
        valeur_interimaire = 0;

        ArrayList<ArrayList> possibilites_par_pioche = new ArrayList<>();
        ArrayList<Integer> action = new ArrayList<>();
        ArrayList<Integer> numero = new ArrayList<>();

        for(int pioche_idx = 0; pioche_idx < 3; pioche_idx++){ //Construction des possibilités et des tableaux action et numero

            possibilites_par_pioche.add(construirePossibilite(((Travaux)j.numeros[pioche_idx].top()).getNumero(), j.joueurs[joueur])); //On a toutes les possibilités pour chaque pioche

            action.add(((Travaux)j.actions[pioche_idx].top()).getAction());
            numero.add(((Travaux)j.numeros[pioche_idx].top()).getNumero());
        }

        //On parcourt les pioches pour chaque carte action qui nous interresse selon l'ordre de priorité suivant
        boolean bestPiocheFound = false;

        //BIS TODO -> Ne fonctionne pas bien, fait perdre 3pts
        /*
        //BIS POUR COMBLER LES GAPS
        for(int pioche_idx = 0; pioche_idx < 3 && !bestPiocheFound; pioche_idx++){
            if(action.get(pioche_idx) == 3 &&  isGap(j, joueur)>= 0 && meilleurEmplacementDefault(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur)>=0 && nombre_bis <= nombre_bis_max){
                res = pioche_idx;
                bestPiocheFound = true;

                emplacement_choisi = meilleurEmplacementDefault(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur);
                emplacement_gap = isGap(j, joueur);
                nombre_bis++;

                if(affichage_decisions)
                    System.out.println("################################## BIS " + emplacement_choisi + " // GAP " + emplacement_gap);


            }
        }
         */

        //PISCINES
        for(int pioche_idx = 0; pioche_idx < 3 && !bestPiocheFound; pioche_idx++){
            if(action.get(pioche_idx) == 0 && meilleurEmplacementPiscine(numero.get(pioche_idx), possibilites_par_pioche.get(pioche_idx), j, joueur) >= 0){ //Si on trouve une piscine parfaitement placable, on la place.
                res = pioche_idx;
                bestPiocheFound = true;

                emplacement_choisi = meilleurEmplacementPiscine(numero.get(pioche_idx), possibilites_par_pioche.get(pioche_idx), j, joueur);

                if(affichage_decisions)
                    System.out.println("################################## PISCINE " + emplacement_choisi);
            }
        }

        //PARCS
        for(int pioche_idx = 0; pioche_idx < 3 && !bestPiocheFound; pioche_idx++){
            if(action.get(pioche_idx) == 3 && meilleurEmplacementParc(nombre_parcs, possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur) >= 0){  //Si on trouve un parc parfaitement placable, on le place.
                res = pioche_idx;
                bestPiocheFound = true;

                emplacement_choisi = meilleurEmplacementParc(nombre_parcs, possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur);
                nombre_parcs[emplacement_choisi/100]++; //On ajoute le parc au compte sur la bonne ligne

                if(affichage_decisions)
                    System.out.println("################################## PARC " + emplacement_choisi);

            }
        }

        //BARRIERES
        for(int pioche_idx = 0; pioche_idx < 3 && !bestPiocheFound; pioche_idx++){
            if(action.get(pioche_idx) == 5 && meilleurEmplacementDefault(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur) >= 0 && nombre_barrieres < choix_barriere_optimale.length-1){
                res = pioche_idx;
                bestPiocheFound = true;

                emplacement_choisi = meilleurEmplacementDefault(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur);

                if(affichage_decisions)
                    System.out.println("################################## BARRIERE " + emplacement_choisi);

            }
        }

        //AGENTS IMMOBILIERS
        for(int pioche_idx = 0; pioche_idx < 3 && !bestPiocheFound; pioche_idx++){
            if(action.get(pioche_idx) == 4 && meilleurEmplacementDefault(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur) >= 0 && nombre_agents < nombre_agents_necessaires){
                res = pioche_idx;
                bestPiocheFound = true;

                emplacement_choisi = meilleurEmplacementDefault(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur);

                if(affichage_decisions)
                    System.out.println("################################## AGENT " + emplacement_choisi);

            }
        }

        //CAS PAR DEFAUT
        //Si on ne trouve pas de carte action utilisable, on cherche le meilleur numero à placer
        if(!bestPiocheFound){
            int[] meilleurs_emplacements_trouves = new int[3];
            int max_emplacement = -1;
            int max_emplacement_idx = -1;

            for(int i = 0; i < 3; i++){     //Trouve le meilleur emplacement pour chaque pioche
                meilleurs_emplacements_trouves[i] = meilleurEmplacementDefault(possibilites_par_pioche.get(i), numero.get(i), j, joueur);
            }

            for(int i = 0; i < meilleurs_emplacements_trouves.length; i++){     //Recherche le maximum des emplacements trouvés afin de prioriser la rue du bas
                if(max_emplacement < meilleurs_emplacements_trouves[i]){
                    max_emplacement = meilleurs_emplacements_trouves[i];
                    max_emplacement_idx = i;
                }
            }

            if(max_emplacement != -1){  //Si le maximum est différent de -1, alors on a trouvé au moins un emplacement interressant
                res = max_emplacement_idx;
                emplacement_choisi = max_emplacement;
                bestPiocheFound = true;

                if(affichage_decisions)
                    System.out.println("################################## DEFAULT " + emplacement_choisi);
            }

        }

        //INTERIMAIRES
        //Dans le cas ou on ne trouve rien, on essaie pour chaque valeur des cartes interimaires
        for(int pioche_idx = 0; pioche_idx < 3 && !bestPiocheFound; pioche_idx++){
            if(action.get(pioche_idx) == 1 && meilleurEmplacementInterimaire(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur) >= 0){
                res = pioche_idx;
                bestPiocheFound = true;

                emplacement_choisi = meilleurEmplacementInterimaire(possibilites_par_pioche.get(pioche_idx), numero.get(pioche_idx), j, joueur);

                if(affichage_decisions)
                    System.out.println("################################## INTERIMAIRE " + emplacement_choisi + " // ECART : " + valeur_interimaire + " // NUMERO : " + numero.get(pioche_idx));

            }
        }

        //Si rien n'est trouvé malgré tout -> Refus de Permis ou plateau rempli
        if(res<0 || res>2) {
            res = RandomSingleton.getInstance().nextInt(3);

            if(affichage_decisions)
                System.out.println("################################## RAMDOM " + res);
        }

        pioche_choisie[0] = ((Travaux)j.numeros[res].top()).getNumero();
        pioche_choisie[1] = ((Travaux)j.actions[res].top()).getAction();

        return res;
    }

    //Place un numéro bis si il y a un trou
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        int res=0;
        
        if(emplacement_gap != -1) {     //Emplacement déjà choisi
            res = placeValide.indexOf(emplacement_gap);
            emplacement_gap = -1;
        }

        return res;
    }
    
    //Valide l'emplacement choisit dans choixCombinaison
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        int res = -1;
        if(emplacement_choisi != -1){
            res = placeValide.indexOf(emplacement_choisi); //on récupère l'index de l'emplacement chosi dans placeValide
        }
        else{
            res = meilleurEmplacementDefault(placeValide, numero, j, joueur);   //Si il n'existe pas, dans le doute on réessaie de trouver un emplacement
        }

        if(res<0 || res>placeValide.size()-1)   //-> Refus de permis
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }
    
    //Choisit le nombre pour les intérimaires
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        int res= numero + valeur_interimaire;
        return res;
    }
    
    //Valorise en priorité les 6 puis les 1
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        int res = valorisations_lotissement_optimales[nombre_agents];
        if(nombre_agents < valorisations_lotissement_optimales.length-1){
            nombre_agents++;
        }
        return res;
    }
    
    //Forme des lotissements : 4x6 et 9x1
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        int res = placeValide.indexOf(choix_barriere_optimale[nombre_barrieres]);
        if(nombre_barrieres < choix_barriere_optimale.length-1)
            nombre_barrieres++;

        if(res < 0 || res > placeValide.size()-1)   //Par sécurité
            res = 0;
        return res;
    }
    
    //Valide toujours un plan si possible
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        return true;
    }
    
    @Override
    public void resetStrat(){
        nombre_agents = 0;
        nombre_barrieres = 0;
        for(int i = 0; i < nombre_parcs.length; i++){nombre_parcs[i] = 0;}
    };


    // --------- MES FONCTIONS ----------

    //Récupéré de Jeu.java pour générer les possibilités pour chaque pioche
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

    //Trouve le meilleur emplacement de piscine -> ce n'est pas la priorité, mais si ca tombe bien, on prend
    public static int meilleurEmplacementPiscine(int numero, ArrayList<Integer> placeValide, Jeu j, int joueur) {
        // Test rue 2
        int idx = findClosestIndexAvailable(numero, plateau_ideal[2], j, joueur);
        if (idx != -1 && Math.abs(numero - plateau_ideal[2][idx]) <= max_ecart && j.joueurs[joueur].ville.rues[2].maisons[idx].emplacementPiscine) {    //Si on trouve un indice, que l'écart est inférieur à 1, et qu'il y a un emplacement piscine
            if(placeValide.contains(idx + 200))
                return idx + 200;
        }

        // Autres rues
        boolean found = false;
        for (int i = 1; i >= 0; i--) { // Parcours les deux premières rues
            if(!isFull(j.joueurs[joueur].ville.rues[i])){   //Si la rue n'est pas pleine
                for (int k = 0; k < emplacement_piscine_optimale[i].length; k++) { // Parcours les emplacements de piscine optimaux pour chaque rue
                    int emplacement = emplacement_piscine_optimale[i][k];
                    int numeroPiscine = numero_piscine_optimale[i][k];
                    if (numero == numeroPiscine && j.joueurs[joueur].ville.rues[i].maisons[emplacement].numero == -1) {
                        if(placeValide.contains(100 * i + emplacement)) {
                            found = true;
                            return 100 * i + emplacement; // Retourne l'emplacement optimal si la maison est disponible et le numéro correspond
                        }
                    }
                }
            }
        }
        return -1; // Aucun emplacement optimal trouvé
    }

    //Trouve le meilleur emplacement de parcs en fonction du nombre de parcs posés par rue
    public static int meilleurEmplacementParc(int[] nombre_parcs, ArrayList<Integer> placeValide, int numero, Jeu j, int joueur){
        //Rue 2
        int idx = findClosestIndexAvailable(numero, plateau_ideal[2], j, joueur);
        if(idx != -1 && Math.abs(numero - plateau_ideal[2][idx]) <= max_ecart && nombre_parcs[2] < nombre_parcs_max[2]){ //Si il n'y a pas encore 5 parcs et qu'on trouve un index
            if(placeValide.contains(idx + 200))
                return idx + 200;
        }

        //Autres rues
        boolean found = false;
        for(int i = 1; i >= 0; i--){ //Parcours les deux rues
            if(!isFull(j.joueurs[joueur].ville.rues[i])){   //Si la rue n'est pas pleine
                for(int k = 0; k < plateau_ideal[i].length && !found; k++){
                    int numeroParc = (int)plateau_ideal[i][k];
                    if(numeroParc == numero && j.joueurs[joueur].ville.rues[i].maisons[k].numero == -1 && nombre_parcs[i] < nombre_parcs_max[i]){ //Si le numero correspond au plateau ideal, que l'emplacement est dispo et qu'il manque encore un parc
                        if(placeValide.contains(100 * i + k)) {
                            found = true;
                            return 100 * i + k;
                        }
                    }
                }
            }
        }
        return -1; //Aucun emplacement trouvé
    }

    //Cherche un emplacement pour toutes les valeurs possibles de la carte intérimaire
    public static int meilleurEmplacementInterimaire(ArrayList<Integer> placeValide, int numero, Jeu j, int joueur){
        ArrayList<Integer> emplacements_trouves = new ArrayList<>();
        ArrayList<Integer> ecarts_necessaires = new ArrayList<>();
        int erreurs_trouvees = 0;

        for(int i = -2; i <= 2; i++){   //Pour toutes les valeurs que peut prendre la carte intérimaire
            emplacements_trouves.add(meilleurEmplacementDefault(placeValide, numero+i, j, joueur)); //Trouver le meilleur emplacelement
            ecarts_necessaires.add(i);
        }

        for (int i = 0; i < emplacements_trouves.size(); i++) {     // Supprime les -1 de emplacements_trouves et les éléments correspondants de ecarts_necessaires
            if (emplacements_trouves.get(i) == -1) {
                emplacements_trouves.remove(i); // Supprimer le -1
                ecarts_necessaires.remove(i); // Supprimer l'élément correspondant de ecarts_necessaires
                i--; // Décrémenter i pour compenser la suppression et vérifier le nouvel élément à la même position
            }
        }

        if(!emplacements_trouves.isEmpty()){
            int meilleur_emplacement = Collections.max(emplacements_trouves);   //Récupère le max des emplacements trouvés afin de prioriser la rue du bas

            valeur_interimaire = ecarts_necessaires.get(emplacements_trouves.indexOf(meilleur_emplacement));    //Récupère l'écart avec le nombre originel de la carte nécessaire pour l'emplacement
            return meilleur_emplacement;
        }
        else{
            return -1;
        }
    }

    //Trouve le meilleur emplacement pour un numéro
    public static int meilleurEmplacementDefault(ArrayList<Integer> placeValide, int numero, Jeu j, int joueur){
        // Rue 2
        int idxRue2 = findClosestIndexAvailable(numero, plateau_ideal[2], j, joueur);
        if(idxRue2 != -1 && Math.abs(numero - plateau_ideal[2][idxRue2]) <= max_ecart) {
            if(placeValide.contains(idxRue2 + 200)) {
                return idxRue2 + 200;
            }
        }

        // Autres rues
        boolean found = false;
        for(int i = 1; i >= 0; i--) {
            for(int k = 0; k < plateau_ideal[i].length && !found; k++) {
                int numeroIdeal = (int)plateau_ideal[i][k];
                if(numeroIdeal == numero && j.joueurs[joueur].ville.rues[i].maisons[k].numero == -1) {
                    if(placeValide.contains(100 * i + k)) {
                        found = true;
                        return 100 * i + k;
                    }
                }
            }
        }
        return -1; // Aucun emplacement trouvé
    }

    //Cherche l'indice dans la rue 2 qui minimise l'écart entre le numero donné et le plateau ideal
    public static int findClosestIndexAvailable(int numero, double[] rue, Jeu j, int joueur) {
        int idx = -1;
        if (!isFull(j.joueurs[joueur].ville.rues[2])) {
            double min = Double.MAX_VALUE; // Initialiser min avec une valeur grande
            for (int i = 0; i < rue.length; i++) {
                if (j.joueurs[joueur].ville.rues[2].maisons[i].numero == -1) {
                    double diff = Math.abs(numero - rue[i]);
                    if (diff < min) {
                        min = diff;
                        idx = i;
                    }
                }
            }
        }
        return idx;
    }

    //Check si une rue est pleine
    public static boolean isFull(Rue rue){ //indique si une rue est pleine
        boolean full = true;
        for(int i = 0; i < rue.taille; i++){
            if(rue.maisons[i].estVide()){
                full = false;
            }
        }

        return full;
    }

    //Cherche un trou entre deux nombres
    public static int isGap(Jeu j, int joueur){
        for(int rue_idx = 2; rue_idx >= 0; rue_idx--){
            for(int i = 1; i < j.joueurs[joueur].ville.rues[rue_idx].taille-1; i++){
                int num_pre = j.joueurs[joueur].ville.rues[rue_idx].maisons[i-1].numero; //numéro maison précédente
                int num_post = j.joueurs[joueur].ville.rues[rue_idx].maisons[i+1].numero; //numéro maison suivante
                int num_act = j.joueurs[joueur].ville.rues[rue_idx].maisons[i].numero; //numéro maison actuelle
                if(num_act == -1 && num_pre != -1 && num_post != -1) //Si les deux maisons adjacentes sont occupées, que la maison est dispo
                    return 100*rue_idx + i;
            }
        }
        return -1;
    }
}
