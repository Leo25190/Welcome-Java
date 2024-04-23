package welcome.ia;
import java.util.ArrayList;
import java.util.Arrays;

import welcome.Jeu;
import welcome.utils.RandomSingleton;
import welcome.*;


public class Strat137 extends Strat{

    public Strat137(){
    }

    @Override
    public String nomVille(){
        return "Pernezer";
    }

    @Override
    public String nomJoueur(){
        return "Antoine Grignon";
    }

    private int numeroCarteChoisi;
    private int nombreAgentImmo;
    private  int nombreBarriereGeometre;
    private  int maisonPlaceApresRefus;
    private int indiceInterim;

    // Plateau pour placer les maisons de facon bien définis
    private static int [][] plateauIdeale = {
            {1,2,3,4,5,6,7,8,9,10},{5,6,7,8,9,10,11,12,13,14,15},{3,4,5,6,7,8,9,10,11,12,13,14},
    };
    private static int [] piscineIdeale = {3,7,8,101,104,108,202,207,211};


    // Faire un plateau idéal piscine pour les placer uniquement si elles sont à un emplacement piscine et si cette emplacement est dans plateauIdeale ????

    // Placement barriere selon place valide pour faire arbitrairement
    private static int [] barrieresIdeale = {206,106,6,7,8,9};
    // Valorisation lotissement ( on vise à valoriser au max les 6 à cause du placement des barrieres
    private static int [] valorisationIdeale = {6,6,6,6,1,5,5,5,5};


    @Override
    public int choixCombinaison(Jeu j, int joueur){
        int res=-1;

        // si la carte est un parc
        for (int i = 0; i < 3; i++) {
            Travaux carte_action = (Travaux) (j.actions[i].top());
            Travaux carte_numero = (Travaux) (j.numeros[i].top());

            if (carte_action.getAction() == 3 && trouverEmplacement(j, carte_numero.getNumero()) != -1) {
                for (int r = 2; r >= 0; r--) {
                    // si elle est dans tableau ideal et que le nombre de parc est inferieur à 5 rue 3 4 rue 2 3 rue 1 on prend cette carte
                    if ( j.joueurs[joueur].ville.nbParcs[r] < r + 4 ) {
                        numeroCarteChoisi = i;
                        //System.out.println("#########################choisi car parc dans plateau ideal ######################################");
                        res = i;
                        return res;
                    }
                }
            }
        }

        // si la carte est un Agent Immobilier
        for (int i = 0; i < 3; i++) {
            Travaux carte_action = (Travaux) (j.actions[i].top());
            Travaux carte_numero = (Travaux) (j.numeros[i].top());

            if (carte_action.getAction() == 4 && trouverEmplacement(j, carte_numero.getNumero()) != -1 && nombreAgentImmo<6) {
                // Si cette carte à un endroit ou etre placé sur mon plateauIdeale et que mon nombre d'agent immobilier est inférieur à 6 (totalement arbitraire)
                numeroCarteChoisi = i;
                //System.out.println("#########################choisi car numero agent immo dans plateau ideal et besoin de lotissement ######################################");
                nombreAgentImmo ++;
                res =i;
                return res;
            }
        }

        // si la carte est un Géometre ( barriere )
        for (int i = 0; i < 3; i++) {
            Travaux carte_action = (Travaux) (j.actions[i].top());
            Travaux carte_numero = (Travaux) (j.numeros[i].top());

            if (carte_action.getAction() == 5 && trouverEmplacement(j, carte_numero.getNumero()) != -1 && nombreBarriereGeometre<6) {
                // Si cette carte à un endroit ou etre placé sur mon plateauIdeale et que je n'ai pas encore mis 6 barrieres ( arbitraire mais défini par rapport à mon choix de barriere opti )
                numeroCarteChoisi = i;
                //System.out.println("#########################choisi car numero agent immo dans plateau ideal et besoin de lotissement ######################################");
                nombreBarriereGeometre ++;
                res= i;
                return res;
            }
        }

        // Si c'est une piscine
        for (int i = 0; i < 3; i++) {
            Travaux carte_action = (Travaux) (j.actions[i].top());
            Travaux carte_numero = (Travaux) (j.numeros[i].top());

            if (carte_action.getAction() == 0) {
                if (trouverEmplacement(j, carte_numero.getNumero()) != -1) {
                    numeroCarteChoisi = i;
                    res = i;
                    //System.out.println("#########################choisi car piscine libre dans plateau ideal ######################################");
                    return res;
                }
            }
        }

        // si la carte est un Intérim
        for (int i = 0; i < 3; i++) {
            Travaux carte_action = (Travaux) (j.actions[i].top());
            Travaux carte_numero = (Travaux) (j.numeros[i].top());

            if (carte_action.getAction() == 1 ) {
                // Si intérim mais pas dispo dans plateauIdeale essayer de balayer via les indices de l'intérim

                for(int interim = 0; interim <5 ; interim++){
                    if(trouverEmplacement(j, carte_numero.getNumero()+interim-2) != -1){
                        numeroCarteChoisi = i;
                        indiceInterim = carte_numero.getNumero()+interim-2;
                        //System.out.println("#########################choisi car interim dans plateau ideal ######################################");
                        res = i;
                        return res;
                    }
                }
            }
        }

        // regarde si le numéro de la carte se trouve sur mon plateau idéal si oui je choisis cette carte
        for (int i = 0; i < 3; i++) {
            Travaux carte_action = (Travaux) (j.actions[i].top());
            Travaux carte_numero = (Travaux) (j.numeros[i].top());

            if (trouverEmplacement(j, carte_numero.getNumero()) != -1) {
                numeroCarteChoisi = i;
                //System.out.println("#########################choisi grace à emplacement ideal ######################################");
                res = i;
                return res;
            }
        }

        // Si rien n'est trouvé
        for (int i = 0; i < 3; i++) {
            Travaux carte_action = (Travaux) (j.actions[i].top());
            Travaux carte_numero = (Travaux) (j.numeros[i].top());

            // remplis les trous du plateau
            if (trouverEmplacement(j, carte_numero.getNumero()) == -1 ) {
                for (int r = 2; r >= 0; r--) {
                    for (int m = 0; m < j.joueurs[joueur].ville.rues[r].taille; m++) {
                        if (m == 0 && j.joueurs[joueur].ville.rues[r].maisons[m].estVide() && carte_numero.getNumero() < j.joueurs[joueur].ville.rues[r].maisons[m + 1].numero) {
                            maisonPlaceApresRefus = 100 * r + m;
                            numeroCarteChoisi = i;
                            res = i;
                        }
                        else if (m > 0 && m < j.joueurs[joueur].ville.rues[r].taille - 1 && j.joueurs[joueur].ville.rues[r].maisons[m].estVide() && carte_numero.getNumero() > j.joueurs[joueur].ville.rues[r].maisons[m - 1].numero && carte_numero.getNumero() < j.joueurs[joueur].ville.rues[r].maisons[m + 1].numero) {
                            maisonPlaceApresRefus = 100 * r + m;
                            numeroCarteChoisi = i;
                            res = i;
                        }
                        else if (m == j.joueurs[joueur].ville.rues[r].taille - 1 && j.joueurs[joueur].ville.rues[r].maisons[m].estVide() && carte_numero.getNumero() > j.joueurs[joueur].ville.rues[r].maisons[m - 1].numero) {
                            maisonPlaceApresRefus = 100 * r + m;
                            numeroCarteChoisi = i;
                            res = i;
                        }
                    }
                }
            }

            if (res == -1){
                res=RandomSingleton.getInstance().nextInt(3);
                numeroCarteChoisi=res;
                //System.out.println("#########################choisi random ######################################");
            }
        }
        return res;
    }

    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        int res=-1;
        //A COMPLETER

        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return 0;
    }

    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide) {
        int res = -1;

        if(maisonPlaceApresRefus!=-1){
            res = placeValide.indexOf(maisonPlaceApresRefus);
        }

        if (trouverEmplacement(j, numero) != -1) {
            res = placeValide.indexOf(trouverEmplacement(j,numero));
            //System.out.println("#########################placé grâce à l'emplacement idéal######################################");
        }

        // Choix aléatoire
        if (res == -1) {
            res = RandomSingleton.getInstance().nextInt(placeValide.size());
            //System.out.println("########################RANDOM ######################################");
        }
        return res;
    }

    @Override
    public int choixNumero(Jeu j, int joueur, int numero) {
        int res = -1;

        if (indiceInterim >= numero - 2 && indiceInterim <= numero + 2) {
            res = indiceInterim;
            //System.out.println("#########################choisi interim ######################################");
        }
        else {
            res = Math.max(0, RandomSingleton.getInstance().nextInt(5) + numero - 2);
        }
        return res;
    }

    @Override
    public int valoriseLotissement(Jeu j, int joueur){
        int res=-1;

        if(valorisationIdeale.length>0){
            res=valorisationIdeale[0];
            valorisationIdeale= Arrays.copyOfRange(valorisationIdeale,1,valorisationIdeale.length);
            //System.out.println("########################VALORISE GRACE A TABLEAU VALORISATION IDEALE ######################################");

        }
        else {
            res =res=RandomSingleton.getInstance().nextInt(6)+1;
            return res;
        }

        return res;

    }

    @Override
    public int choixBarriere(Jeu j, int joueur, ArrayList<Integer> placeValide) {
        int res = -1;

        // Parcourez les indices des barrières idéales
        for (nombreBarriereGeometre = 0; nombreBarriereGeometre < barrieresIdeale.length; nombreBarriereGeometre++) {
            res = placeValide.indexOf(barrieresIdeale[nombreBarriereGeometre]);

            // Si une place valide est trouvée, retournez-la
            if (res != -1) {
                return res;
            }
        }

        // Si aucune place valide n'est trouvée, retourne 0
        if (res == -1) {
            res = 0;
        }

        return res;
    }

    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        boolean res = true;
        return res;
    }

    @Override
    public void resetStrat(){
        numeroCarteChoisi =-1;
        nombreBarriereGeometre =0;
        nombreAgentImmo =0;
        valorisationIdeale = new int [] {6,6,6,6,1,5,5,5,5};
        barrieresIdeale = new int [] {206,106,6,7,8,9};
        maisonPlaceApresRefus=-1;
        indiceInterim=0;

    };

    public static int trouverEmplacement (Jeu j ,int numeroCarte){
        int emplacement = -1;
        int distance = -1;
        int distanceMinimal = Integer.MAX_VALUE;
        for(int r=2;r>=0;r--){
            for(int n = 0 ; n < plateauIdeale[r].length ; n++){
                if(plateauIdeale[r][n] == numeroCarte && j.joueurs[0].ville.rues[r].maisons[n].estVide()==true){
                    distance = Math.abs(numeroCarte-plateauIdeale[r][n]);
                    if(distance<distanceMinimal){
                        distanceMinimal=distance;
                    }
                    if(distanceMinimal==0){
                        emplacement = 100*r+n;
                        return emplacement;
                    }
                }
            }
        }
        return -1;
    }
}