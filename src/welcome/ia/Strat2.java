package welcome.ia;
import java.util.ArrayList;
import welcome.Jeu;
import welcome.Travaux;
import welcome.utils.RandomSingleton;
import welcome.Rue;
import welcome.Joueur;
import welcome.utils.Couleur;

// strat 93 points, lotissements de 6 et 1


public class Strat2 extends Strat {
    public int action;
    public int numero;
    public int barriere;
    public int parcs;
    public int tour;
    ArrayList<Integer> valotissement = new ArrayList<>();

    public Strat2(){
        this.parcs=0;
        this.action=0;
        this.numero=0;
        this.barriere=0;
        this.tour=0;
        valotissement.add(4);
        valotissement.add(6);
        valotissement.add(1);
    }

    private ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        if(joueur.verbose){
            //System.out.println(joueur.ville);
            System.out.println("PossibilitÃ©s de placement: position dans la rue (numero du choix Ã  entrer au clavier)");
        }
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList(); //List des possibilitÃ©s ÃƒÂ  construire
        for(int i=0; i<3; i++){//Pour chaque rue
            min=joueur.ville.rues[i].taille-1; //on part de la fin
            if(joueur.verbose){
                System.out.print("Rue " + (i+1) + ":");
            }
            while(min>=0  && (joueur.ville.rues[i].maisons[min].numero==-1 || joueur.ville.rues[i].maisons[min].numero > numero))
                min--; // on dÃ©crement le min tant qu'on a pas trouvÃ© un numÃ©ro <=
            if(min<0 || joueur.ville.rues[i].maisons[min].numero!=numero){

                min++;// On part de la case suivante
                while(min < joueur.ville.rues[i].taille && joueur.ville.rues[i].maisons[min].numero == -1){
                    possibilite.add((Integer)(min+ 100*i)); // on construit les possibilitÃ©s tant qu'on a des cases vides
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

    @Override
    public String nomVille(){
        return "ZemelCity";
    }

    @Override
    public String nomJoueur(){
        return "Duby";
    }



    public final int[][] Ideales1 = {{1,3,4,5,6,7,8,9,11,13},
            {1,3,5,7,8,9,10,11,12,13,14},
            {3,5,6,7,8,9,10,11,12,13,14,15}};

    public final int[][] Ideales2 = {{2,3,4,5,6,7,8,10,12,14},
            {2,4,6,7,8,9,10,11,12,13,15},
            {4,5,6,7,8,9,10,11,12,13,14,16}};

    public final int[][] Ideales3 = {{0,3,4,5,6,7,8,9,11,15},
            {2,4,6,7,8,9,10,11,12,13,16},
            {2,5,6,7,8,9,10,11,12,13,14,17}};


    //actions et indices correspondants
    // piscine, interim, bis, parc, agent imm, barriÃ¨re
    public boolean numeroExisteDeja(int num, Rue r){
        boolean b = false;
        for(int i=0; i<r.taille;i++){
            if(r.maisons[i].numero==num){
                b=true;
                break;
            }
        }
        return b;
    }

    public boolean canPutaPool(int num, Jeu j){
        boolean b= false;
        ArrayList<Integer> p= construirePossibilite(num, j.joueurs[0]);
        // on teste dans la 3Ã¨me rue avec les positions idÃ©ales
        if(num==5 && p.contains(201) && numeroExisteDeja(num, j.joueurs[0].ville.rues[2]) == false){
            b=true;
            return b;
        }
        else if(num == 10 && p.contains(206) && numeroExisteDeja(num, j.joueurs[0].ville.rues[2]) == false){
            b=true;
            return b;
        }
        else if(num == 14 && p.contains(210) && numeroExisteDeja(num, j.joueurs[0].ville.rues[2]) == false){
            b=true;
            return b;
        }
        //on teste dans la 2Ã¨me rue avec les positions idÃ©ales

        else if(num==1 ||num==2 && p.contains(100) && numeroExisteDeja(num, j.joueurs[0].ville.rues[1]) == false){
            b=true;
            return b;
        }
        else if(num == 7 && p.contains(103) && numeroExisteDeja(num, j.joueurs[0].ville.rues[1]) == false){
            b=true;
            return b;
        }
        else if(num == 11 && p.contains(107) && numeroExisteDeja(num, j.joueurs[0].ville.rues[1]) == false){
            b=true;
            return b;
        }
        //on teste dans la 1Ã¨re rue avec les positions idÃ©ales

        else if(num==4 && p.contains(2) && numeroExisteDeja(num, j.joueurs[0].ville.rues[0]) == false){
            b=true;
            return b;
        }
        else if(num == 8 && p.contains(6) && numeroExisteDeja(num, j.joueurs[0].ville.rues[0]) == false){
            b=true;
            return b;
        }
        else if(num == 9 || num==10 && p.contains(7) && numeroExisteDeja(num, j.joueurs[0].ville.rues[0]) == false){
            b=true;
            return b;
        }

        return b;
    }

    public int FindPlacePool(int num, Jeu j,ArrayList<Integer> placeValide){
        int b=0;
        //on teste 3Ã¨me rue
        if(num==5 && placeValide.contains(201) && numeroExisteDeja(num, j.joueurs[0].ville.rues[2]) == false){
            b= placeValide.indexOf(201 );
            return b;
        }
        else if(num == 10 && placeValide.contains(206) && numeroExisteDeja(num, j.joueurs[0].ville.rues[2]) == false){
            b= placeValide.indexOf(206);
            return b;
        }
        else if(num == 14 && placeValide.contains(210) && numeroExisteDeja(num, j.joueurs[0].ville.rues[2]) == false){
            b= placeValide.indexOf(210);
            return b;
        }
        //on teste dans la 2Ã¨me rue avec les positions idÃ©ales
        else if(num==1||num==2 && placeValide.contains(100) && numeroExisteDeja(num, j.joueurs[0].ville.rues[1]) == false){
            b= placeValide.indexOf(100);
            return b;
        }
        else if(num == 7 && placeValide.contains(103) && numeroExisteDeja(num, j.joueurs[0].ville.rues[1]) == false){
            b= placeValide.indexOf(103);
            return b;
        }
        else if(num == 11 && placeValide.contains(107) && numeroExisteDeja(num, j.joueurs[0].ville.rues[1]) == false){
            b= placeValide.indexOf(107);
            return b;
        }
        //on teste dans la 1Ã¨re rue avec les positions idÃ©ales
        else if(num==4 && placeValide.contains(2) && numeroExisteDeja(num, j.joueurs[0].ville.rues[0]) == false){
            b= placeValide.indexOf(2);
            return b;
        }
        else if(num == 8 && placeValide.contains(6) && numeroExisteDeja(num, j.joueurs[0].ville.rues[0]) == false){
            b= placeValide.indexOf(6);
            return b;
        }
        else if(num == 9 || num==10 && placeValide.contains(207) && numeroExisteDeja(num, j.joueurs[0].ville.rues[0]) == false){
            b= placeValide.indexOf(7);
            return b;
        }
        return b;
    }

    public boolean canPutIdealy(int num, Jeu j){
        boolean b = false;
        ArrayList<Integer> placeValide = construirePossibilite(num, j.joueurs[0]);
        for(int i=2;i>=0;i--){
            for(int k=0; k<j.joueurs[0].ville.rues[i].taille;k++){
                if(num==Ideales1[i][k] && placeValide.contains(100*i +k) ){
                    b=true;
                    return b;
                }
                else if(num==Ideales2[i][k] && placeValide.contains(100*i +k) ){
                    b=true;
                    return b;
                }
                else if(num==Ideales3[i][k] && placeValide.contains(100*i +k) ){
                    b=true;
                    return b;
                }

            }}
        return b;
    }
    public boolean canPut(int num, Jeu j){
        boolean b = false;
        ArrayList<Integer> placeValide = construirePossibilite(num, j.joueurs[0]);
        for(int i=2;i>=0;i--){
            for(int k=1; k<j.joueurs[0].ville.rues[i].taille-1;k++){
                if(num<j.joueurs[0].ville.rues[i].maisons[k+1].numero && placeValide.contains(100*i + k) && num>j.joueurs[0].ville.rues[i].maisons[k-1].numero){
                    b = true;
                    return b;
                }
            }
        }
        return b;
    }


    public static int trouverIndiceMaximum(ArrayList<Integer> cartes) {
        int max = Integer.MIN_VALUE;
        int indiceMax = -1;

        for (int i = 0; i < cartes.size(); i++) {
            int numero = cartes.get(i);
            if (numero > max) {
                max = numero;
                indiceMax = i;
            }
        }

        return indiceMax;
    }


    //Choisir parmi les 3 numÃ©ros dispos
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        this.tour++;

        ArrayList<Integer> nums = new ArrayList<>();
        for(int i=0;i<3;i++){
            nums.add(((Travaux)j.numeros[i].top()).getNumero());
        }

        int res = trouverIndiceMaximum(nums); // si aucune position idÃ©ale on choisit la carte la pluis haute


        if(tour<=22){
            for(int i=0;i<3;i++){ // on teste parc
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==3 && canPutIdealy(num, j) ){
                    parcs++;
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ // on teste barriÃ¨re
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==5 && canPutIdealy(num, j)&& barriere<10 ){
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ //  on teste agent imm
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==4  && canPutIdealy(num, j) ){
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ //  on teste piscine
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==0 && canPutIdealy(num, j) ){
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ //  on teste interim
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==1  && canPutIdealy(num, j) ){
                    res=i;
                    return res;
                }
            }
        }
        else if(tour<=27){
            for(int i=0;i<3;i++){ //  on teste agent imm
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==4 && canPutIdealy(num, j) ){
                    res=i;
                    return res;
                }
            }

            for(int i=0;i<3;i++){ // on teste barriÃ¨re
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==5  && canPutIdealy(num, j) && barriere<10 ){
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ // on teste parc
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==3 &&  canPutIdealy(num, j) ){
                    parcs++;
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ //  on teste interim
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==1  && canPutIdealy(num, j) ){
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ //  on teste piscine
                int num=((Travaux)j.numeros[i].top()).getNumero();
                int act = ((Travaux)j.actions[i].top()).getAction();
                if(act==0 && canPutIdealy(num, j) ){
                    res=i;
                    return res;
                }
            }

        }
        else{
            for(int i=0;i<3;i++){
                int num=((Travaux)j.numeros[i].top()).getNumero();
                if( canPutIdealy(num, j) || canPut(num, j) ){
                    res=i;
                    return res;
                }
            }
        }


        this.numero= ((Travaux)j.actions[res].top()).getNumero();
        this.action = ((Travaux)j.actions[res].top()).getAction();
        return res;
    }





    //Choisir de placer un numÃ©ro bis
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        int res=-1;

        //A COMPLETER

        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }




    //Choisir  parmi les emplacements dispos
    @Override
    // on choisit juste en fonctions des numÃ©ros et des positions idÃ©ales prÃ©dÃ©finies
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        int res=RandomSingleton.getInstance().nextInt(placeValide.size()); // si aucune position idÃ©ale on choisit de mettre tout Ã  droite

        for(int i=2;i>=0;i--){
            for(int k=0; k<j.joueurs[0].ville.rues[i].taille;k++){
                if(numero==Ideales1[i][k] && placeValide.contains(100*i +k) && numeroExisteDeja(numero, j.joueurs[0].ville.rues[i]) == false ){
                    return placeValide.indexOf(100*i+k);
                }
                else if(numero==Ideales2[i][k] && placeValide.contains(100*i +k) && numeroExisteDeja(numero, j.joueurs[0].ville.rues[i]) == false ){
                    return placeValide.indexOf(100*i+k);
                }
                else if(numero==Ideales3[i][k] && placeValide.contains(100*i +k) && numeroExisteDeja(numero, j.joueurs[0].ville.rues[i]) == false ){
                    return placeValide.indexOf(100*i+k);
                }
            }
        }

        for(int i=2;i>=0;i--){
            for(int k=1; k<j.joueurs[0].ville.rues[i].taille -1;k++){
                if(numero<j.joueurs[0].ville.rues[i].maisons[k+1].numero && placeValide.contains(100*i + k) && numero>j.joueurs[0].ville.rues[i].maisons[k-1].numero){
                    return placeValide.indexOf(100*i+k);
                }
            }
        }
        return res;
    }



    //Choisir le mÃªme numÃ©ro que celui de la carte quand l'action est un intÃ©rimaire
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        int res=-1;

        //A COMPLETER

        if((res<(numero-2) || res>(numero+2)) || res<0)
            res=Math.max(0, RandomSingleton.getInstance().nextInt(5) + numero - 2) ;
        return numero;
    }

    //Valorise alÃ©atoirement une taille de lotissements (proba plus forte si plus d'avancements possibles)
    @Override
    public int valoriseLotissement(Jeu j, int joueur){       // on valorise les 6 tant qu'ils sont pas remplis
        if(valotissement.get(2)!=3){ // tant qu'on max pas les 6
            int nv = valotissement.get(2)+1;
            valotissement.set(2,nv);
            return 1;
        }
        else if(valotissement.get(1)!=12){ // ensuite une fois qu'on a maxÃ© les 6 on max les 1
            int nv_ = valotissement.get(1)+1;
            valotissement.set(1,nv_);
            return 6;
        }
        else return 4;

    }

    //Met une barriÃ¨re Ã  une position alÃ©atoire
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        int res=0;
        if(barriere==0 && placeValide.contains(206)){
            barriere++;
            return placeValide.indexOf(206);
        }
        else if(barriere==1 && placeValide.contains(106)){
            barriere++;
            return placeValide.indexOf(106);
        }
        else if(barriere==2 && placeValide.contains(4)){
            barriere++;
            return placeValide.indexOf(4);
        }
        else if(barriere==3 && placeValide.contains(107)){
            barriere++;
            return placeValide.indexOf(107);
        }
        else if(barriere==4 && placeValide.contains(108)){
            barriere++;
            return placeValide.indexOf(108);
        }
        else if(barriere==5 && placeValide.contains(109)){
            barriere++;
            return placeValide.indexOf(109);
        }
        else if(barriere==6 && placeValide.contains(110)){
            barriere++;
            return placeValide.indexOf(110);
        }

        else if(barriere==7 && placeValide.contains(1)){
            barriere++;
            return placeValide.indexOf(1);
        }
        else if(barriere==8 && placeValide.contains(3)){
            barriere++;
            return placeValide.indexOf(3);
        }
        else if(barriere==9 && placeValide.contains(2)){
            barriere++;
            return placeValide.indexOf(2);
        }


        return res;
    }

    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        boolean res = true;

        //A COMPLETER

        return res;
    }

    @Override
    public void resetStrat(){
        this.action=0;
        this.numero=0;
        this.barriere=0;
        this.parcs=0;
        this.tour=0;
        valotissement = new ArrayList<>();
        valotissement.add(4);
        valotissement.add(6);
        valotissement.add(1);
    };

}