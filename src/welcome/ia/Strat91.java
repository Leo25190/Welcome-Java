package welcome.ia;
import java.util.ArrayList;
import java.util.Arrays;
import welcome.*;
import welcome.utils.RandomSingleton;
import java.util.Collections;
import welcome.utils.Couleur;
/*

STRATEGIE:

(1)on place de manière opti les emplacements en essaynt de les rangers intelligemment 40 pts
(2) on place 3 barrieres à la fin des rues => augmenter la valeur des lotsissmeent de 6 et de 1 70 pts

fonctionne mieux sans   les interimaires;
                        les piscines;

*/

public class Strat91 extends Strat{
    // bot Bidon

    public Strat91(){
    }

    @Override
    public String nomVille(){
        return "Billy-Berclaut";
    }

    @Override
    public String nomJoueur(){
        return "DERKENNE, Hippolyte";
    }
    private int num_carte_choisi;
    // Initialisation du tableau idéale ou les emplacements sont distribués de manière normale suivant la rue

    private final double[][] plateau_ideale_1 = { // score moyen de 95.5811 points
            {3, 4, 6, 7, 8, 9, 10, 11, 12, 14},
            {3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14},
            {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}
    };


    // au cas ou
    private final int [][] plateau_initial = {
            { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}
    };

    private int best_place_pour_index_i = -1; // compris entre 000 et 211

    // Maximisation du nombre de points en calculant le rendement le plus opti des lotissement
    private int[] valeur_lotissement_prio = {6, 6, 6, 6, 1, 5 ,5, 5, 5};

    // placement des barrieres en logique avec le nbre de lotisement valorisé. En atteignant le plus vite possible 4 lotissements de 6
    private int[] index_barriereIdeale = {110, 109, 108, 107, 9, 8, 7, 6, 106, 206};   // choisir de maniere à faire des lotissements de 6 & 3 et les valoriser.
    // à utiliser
    private boolean isBestPiscineFound = false;
    // emplacement piscine
    private int[] tabPiscine = {211, 207, 202, 108, 104, 101, 8, 7, 3};
    // à decrementer en fonction de l'index i qui correspond à la rue ou un parc est mis. Afin d'atteindre plus vite les 18 puis 14 puis 10 pts
    private int[] nb_abres_par_rues = {3, 4, 5};
    // le choix optimal à prendre pour l'interimaire
    private int valeur_increment_interim;
    // pour confirmer si l'interimaire à bien été choisi
    private boolean isInterimaireChosen = false;

    @Override
    public int choixCombinaison(Jeu j, int joueur){
        int res=-1;
        num_carte_choisi = -1;

        int[] tabAction = new int[]{0,0,0};
        int[] tabNum = new int[]{0,0,0};

        ArrayList<Integer> array_emplacements = new ArrayList<>(); // Liste pour stocker les array_emplacements
        // creation de deux tableaux contenant les cartes visible de la pioche
        for(int i = 0; i<3; i++){
            Travaux tn =(Travaux) (j.numeros[i].top()); tabNum[i]= tn.numero;
            Travaux ta = (Travaux)(j.actions[i].top()); tabAction[i]= ta.getAction();
        }


        // PLACEMENT PARC
        /*
        Si la carte est un parc placable, alors la prendre et la placer
        Si
        */
        // Initialisation des indices des cartes parc pour chaque rue
        int parcRue3 = -1; // Indice de la carte parc pour la rue 3
        int parcRue2 = -1; // Indice de la carte parc pour la rue 2
        int parcRue1 = -1; // Indice de la carte parc pour la rue 1

        // Parcours des cartes disponibles
        for (int i = 0; i < 3; i++) {
            // Vérifier si la carte est un parc et s'il existe un emplacement idéal pour le parc
            if (tabAction[i] == 3 && emplacementIdeal(j, joueur, tabNum[i]) != -1) {
                // Récupérer le numéro de maison et de rue pour l'emplacement idéal de la carte
                int numMaison = emplacementIdeal(j, joueur, tabNum[i]) % 100; // Numéro de maison
                int rue = emplacementIdeal(j, joueur, tabNum[i]) / 100; // Numéro de rue

                // Prioriser le placement dans la rue 3 pour les numéros de maison inférieurs à 200
                if (rue == 2 && numMaison < 200 && parcRue3 == -1) {
                    parcRue3 = i;
                }
                // Ensuite, prioriser le placement dans la rue 2 pour les numéros de maison inférieurs à 100
                else if (rue == 1 && numMaison < 100 && parcRue2 == -1) {
                    parcRue2 = i;
                }
                // Enfin, placer les parcs dans la rue 1 pour les numéros de maison inférieurs à 100
                else if (rue == 0 && numMaison < 100 && parcRue1 == -1) {
                    parcRue1 = i;
                }
            }
        }

        // Sélectionner la carte parc en fonction de la priorité des rues
        if (parcRue3 != -1) {
            res = parcRue3;
            num_carte_choisi = res;
            return res;
        } else if (parcRue2 != -1) {
            res = parcRue2;
            num_carte_choisi = res;
            return res;
        } else if (parcRue1 != -1) {
            res = parcRue1;
            num_carte_choisi = res;
            return res;
        }

        // PLACEMENT BARRIERE
        /*
        Choisir la carte barriere "placable" avec la plus haute valeur
        */
        int max_card_value = 0; // Variable pour stocker la valeur maximale de la carte barrière
        int index_max_card_value = -1; // Indice de la carte avec la plus haute valeur
        int nbBarrieresPlacees = 0; // Variable pour compter le nombre de barrières placées
        boolean isBarriere = false; // Indicateur indiquant si une barrière a été trouvée

        // Parcours des cartes disponibles
        for(int i = 0; i < 3; i++) {
            // Vérifier si la carte est une barrière et si elle peut être placée
            if(tabAction[i] == 6 && emplacementIdeal(j, joueur, tabNum[i]) != -1) {
                isBarriere = true; // Indiquer qu'une barrière a été trouvée
                // Vérifier si la valeur de la carte actuelle est supérieure à la valeur maximale
                if(tabNum[i] >= max_card_value) {
                    max_card_value = tabNum[i]; // Mettre à jour la valeur maximale
                    index_max_card_value = i; // Mettre à jour l'indice de la carte avec la plus haute valeur
                }
                res = index_max_card_value; // Stocker l'indice de la carte avec la plus haute valeur

                // Incrémenter le compteur de barrières placées
                nbBarrieresPlacees++;

                // Vérifier si le nombre de barrières placées atteint la longueur de index_barriereIdeale
                if (nbBarrieresPlacees >= index_barriereIdeale.length) {
                    break; // Sortir de la boucle pour arrêter le placement de barrières
                }
            }
        }

        // Vérifier si une barrière a été trouvée
        if(isBarriere) {
            return res; // Retourner l'indice de la carte avec la plus haute valeur
        }


//         PLACEMENT PISCINE, à enlever pour atteindre les 90 points
        /*
        CHoisir la carte piscine placable, si son numero correspond EXACTEMENT à l'emplacement d'une piscine dans tabPiscine[]
        Alors choisir cette carte et la placer.
        */
//        for (int i = 0; i < 3; i++) {
//            if (tabAction[i] == 0 && emplacementIdeal(j, joueur, tabNum[i]) != -1) {// si mon emplaceement ideal pour la carte piscine est à l'emplacement piscine
//                res = i;
//                num_carte_choisi = res;
//                return res;
//            }
//        }


        //PLACEMENT AGENT D INTERIM
        /*
        Placer le numérode l'interimaire qui vaut +/-2;1 ou 0 de manière la plus opti
        Parcours des trois cartes
        pour chaque cartes interim est ceque emplacement interim à trouver une place sur le plateau
        si oui:
            on garde
        si non:
            on renvoie -1

        si plusieurs carte interim placable:
            on place celle dont la valeur est la plus grande ?
                stratégie ici à revoir
        */
//        for(int i = 0; i < 3; i++){
//            int max_interim_value = 0;
//            isInterimaireChosen = false;
//
//            if(tabAction[i] == 1 && emplacementInterim(j, joueur, tabNum[i]) != 404){
//                valeur_increment_interim = emplacementInterim(j, joueur, tabNum[i]); // renvoi le décrement de la carte interim (la plus interessante) le plus interessant
//                if( max_interim_value <= emplacementIdeal(j, joueur, tabNum[i] + valeur_increment_interim)){
//                    max_interim_value = emplacementIdeal(j, joueur, tabNum[i] + valeur_increment_interim);
//                    res = i;
//                }
//            }
//        }
//        if( isInterimaireChosen){
//            num_carte_choisi = res;
//            return res;
//        }
//


//        // PLACEMENT AGENT IMMO Ne fonctionne pas, à fixer
//        for(int i = 0; i < 3; i++){
//            if( tabAction[i]== 4 && emplacementIdeal(j, joueur, tabNum[i]) != -1){
//                res = num_carte_choisi = i;
//                return res;
//            }
//        }

        //recherche d'emplacement idéal si jamais rien trouvé avant.
        for(int i = 0; i < 3 ; i++){
            best_place_pour_index_i =  emplacementIdeal(j, joueur, tabNum[i]);
            array_emplacements.add(best_place_pour_index_i);
        }
        // Trouver l'emplacement optimal parmi les array_emplacements
        int maximumEmplacement = Collections.max(array_emplacements);
        if(maximumEmplacement != -1){
            res = array_emplacements.indexOf(maximumEmplacement);
        }


        if(res<0 || res>2){
            res=RandomSingleton.getInstance().nextInt(3);
            num_carte_choisi = res;
        }

        num_carte_choisi = array_emplacements.indexOf(maximumEmplacement);
        return res;
    }


    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        int res=-1;

        //A COMPLETER

        if(res<0 || res>placeValide.size()-1)
            res = RandomSingleton.getInstance().nextInt(placeValide.size());
        return 0;
    }

    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide) {
        int res = -1;
        Travaux ta = (Travaux)(j.actions[num_carte_choisi].top()); int action= ta.getAction();
        int position;
        /*
        Si c'est une piscine,
        renvoyer l'index de la place dans place valide qui est une piscine.
        */
//        if( action == 0)
//            for(int i = 0; i < tabPiscine.length; i++){
//                for(int rue = 0; rue < 3; rue ++){
//                    if(100*rue + numero == tabPiscine[i] && placeValide.contains(100*rue + numero)){
//                        return placeValide.indexOf(100*rue + numero);
//                    }
//                }
//            }
//
        position = emplacementIdeal(j, joueur, numero);
        res = placeValide.indexOf(position); // Utilise l'emplacement idéal calculé

        if (res < 0 || res > placeValide.size() - 1) {
            res = RandomSingleton.getInstance().nextInt(placeValide.size()); // Choix aléatoire si l'index est invalide
        }
        return res;
    }


    //Choisir le même numéro que celui de la carte quand l'action est un intérimaire
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        int res = -1;

//        ArrayList<Integer> placesValides = construirePossibilite(numero, j.joueurs[joueur] );


        if((res<(numero-2) || res>(numero+2)) || res<0){ //même formule que pour la condition finale
            res = numero + valeur_increment_interim; // calculé plus tot

        }
        else{
            res = numero;
        }


        if((res<(numero-2) || res>(numero+2)) || res<0)
            res= Math.max(0, RandomSingleton.getInstance().nextInt(5) + numero - 2) ;

//        System.out.println("######################### le numero à placer est :"+ res);

        return res;



    }

    /*
    on valorise les lotissements dans l'ordre selectionné au préalable par le tableau valeur_lotissements_prio
    Afin de favorisé les lotissements creer par les barrieres dans la méthode choixBarriere()
    */
    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        int res=-1;
        // Tant que valeur_lotissement_prio n'est pas vide, choisir la premiere case non choisie
        if (valeur_lotissement_prio.length > 0) {
            res = valeur_lotissement_prio[0];
            valeur_lotissement_prio = Arrays.copyOfRange(valeur_lotissement_prio, 1, valeur_lotissement_prio.length);
        } else { // Sinon, renvoyer l'index 0 de placeValide
            res = 0;}
        if(res<1 || res>6)
            res=RandomSingleton.getInstance().nextInt(6)+1;

        return res;
    }
    /*
    Met une barrière à une position suivant l'arraylist index_barriereIdeale
    On doit réinitialiser le tableau barriereIdeale à la fin du code afin que notre tableau de barriere se recrée
    A chaque fois qu'une carte barriere est piochée, on supprime.
    */
    @Override
    public int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide) {
        int res = -1; // doit etre compris entre 0 et 29
        int index = -1;

        // Tant que index_barriereIdeale n'est pas vide, choisir la dernière case non choisie
        if (index_barriereIdeale.length > 0) {
            index = index_barriereIdeale[index_barriereIdeale.length - 1];
//            System.out.println("Choix d'index:"+ index);

            res = placeValide.indexOf(index); //index de ou est index
            index_barriereIdeale = Arrays.copyOf(index_barriereIdeale, index_barriereIdeale.length - 1);
        } else { // Sinon, renvoyer l'index 0 de placeValide
            res = 0;  // nerien placer

        }

        // Si res est toujours égal à -1, choisir aléatoirement dans placeValide, ce qu'on veut eviter.
        if (res == -1) {
            res = RandomSingleton.getInstance().nextInt(placeValide.size());
        }

//        System.out.println("Choix d'emplacement final :"+index);
        return res;
    }


    //Valide toujours un plan dans le doute
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        boolean res = true;

        //A COMPLETER

        return res;
    }

    @Override
    public void resetStrat(){
        best_place_pour_index_i = -1;
        valeur_lotissement_prio = new int[]  {6, 6, 6, 6, 1, 5, 5, 5, 5}; //    NE PAS OUBLIER DE MODIFIER
        index_barriereIdeale = new int[] {110, 109, 108, 107, 9, 8, 7, 6, 106, 206};
        nb_abres_par_rues =  new int[] {3, 4, 5};
        isBestPiscineFound = false;
        isInterimaireChosen = false;
    };

    public int emplacementIdeal(Jeu j, int joueur, int numero) {
        int n_rue_ideale = -1;
        int n_maison_ideale = -1;
        double distanceMin = Integer.MAX_VALUE;
        ArrayList<Integer> placesValides = construirePossibilite(numero, j.joueurs[joueur] );
        for (int k = 2; k >= 0; k--) {
            for (int m = 0; m < plateau_ideale_1[k].length; m++) {
                if (j.joueurs[joueur].ville.rues[k].maisons[m].numero == -1) {
                    double distance = Math.abs(numero - plateau_ideale_1[k][m]);
                    // recherche de la distance minimale
                    if (distance < distanceMin && distance < 5 && placesValides.contains(100*k + m)) {// il faut verifier que l'emplacement idéal est ok
                        distanceMin = distance;
                        n_rue_ideale = k;
                        n_maison_ideale = m;
                    }
                }
            }
        }

        // Vérifier si l'écart est supérieur à deux maisons ou plus, à ajuster en fonction du score
        if (distanceMin < 5) { // a changer au cas ou
            return 100 * n_rue_ideale + n_maison_ideale; //L'emplacement idéale trouvé est à ces coordonnées
        } else {
            return -1; // Aucun emplacement idéal n'a été trouvé
        }
    }


    private ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        if(joueur.verbose){
            //System.out.println(joueur.ville);
            //System.out.println("Possibilités de placement: position dans la rue (numero du choix à entrer au clavier)");
        }
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList(); //List des possibilités Ã  construire
        for(int i=0; i<3; i++){//Pour chaque rue
            min=joueur.ville.rues[i].taille-1; //on part de la fin
            if(joueur.verbose){
                //System.out.print("Rue " + (i+1) + ":");
            }
            while(min>=0  && (joueur.ville.rues[i].maisons[min].numero==-1 || joueur.ville.rues[i].maisons[min].numero > numero))
                min--; // on décrement le min tant qu'on a pas trouvé un numéro <=
            if(min<0 || joueur.ville.rues[i].maisons[min].numero!=numero){

                min++;// On part de la case suivante
                while(min < joueur.ville.rues[i].taille && joueur.ville.rues[i].maisons[min].numero == -1){
                    possibilite.add((Integer)(min+ 100*i)); // on construit les possibilités tant qu'on a des cases vides
                    if(joueur.verbose){
                        //if(i==0)
                        //System.out.print("\t" + Couleur.CYAN + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        //else if(i==1)
                        //System.out.print("\t" + Couleur.JAUNE + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        //else
                        //System.out.print("\t" + Couleur.VERT + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                    }
                    min++;
                }
            }
            if(joueur.verbose){
                //System.out.println("");
            }
        }
        return possibilite;
    }

    public int emplacementIdealPiscine(Jeu j, int joueur, int numero){
        System.out.println("Dans emplacementIdealPiscine:");
        ArrayList<Integer> placesValides = construirePossibilite(numero, j.joueurs[joueur] );

        for (int k = 2; k >=0 ; k--) { // parcours des rues
            for (int m = 0; m < 10 + k; m++) { // parcours des positions dans les rues
                for(int n = 0; n< tabPiscine.length; n++){
                    if( tabPiscine[n] == 100*k + m && !j.joueurs[joueur].ville.rues[k].maisons[m].piscine){
                        isBestPiscineFound = true;
                        j.joueurs[joueur].ville.rues[k].maisons[m].construirePiscine();
                        j.joueurs[joueur].ville.rues[k].maisons[m].piscine =  true; //on indique que la piscine est occupée mtn.
                        return 100 * k + m;
                    }
                }
            }
        }
        isBestPiscineFound = false;
        return -1;
    }

    public int emplacementInterim(Jeu j, int joueur, int numero){
        int tabInterim[] = {-2,-1, 0, 1, 2};
        int numero_interim = 404; // ce que va renvoyer la fonction, -1 si rien trouver pour les 5 valeurs possible du tableau ci dessus
        int max_position_interim = 0; // prendre le maximum (?)
        isInterimaireChosen = false;
        for(int i = 0; i < tabInterim.length; i++){
            if( emplacementIdeal(j, joueur, numero + tabInterim[i]) != -1){ // la valeur+i de la carte est placable on prend le max de cette carte MAIS probleme pour les 17, tant pis
                if(emplacementIdeal(j, joueur, numero + tabInterim[i]) >= max_position_interim) {
                    max_position_interim = emplacementIdeal(j, joueur, numero + tabInterim[i]);
                    numero_interim = tabInterim[i];
                    isInterimaireChosen = true;
                }
            }
        }
        return numero_interim;
    }


}