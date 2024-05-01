package welcome.ia;
import java.util.*;
import welcome.Jeu;
import welcome.Travaux;
import welcome.utils.RandomSingleton;
import welcome.Joueur;
import welcome.utils.Couleur;

//Maxime Dubar
// strat 100 points, lotissements de 6 et 1


public class Strat101 extends Strat {
    
    int barriere;
    int tour;
    int valotissement;

    public Strat101(){
        this.barriere=0;
        this.tour=0;
        this.valotissement=0;
    }
    
   

    @Override
    public String nomVille(){
        return "ZemelCity";
    }
    
    @Override
    public String nomJoueur(){
        return "DUBAR Maxime ";
    }
    


   public final int[][] Ideales1 = {{1,3,4,5,6,7,8,9,11,13},
                                    {1,3,5,7,8,9,10,11,12,13,14},
                                     {1,3,5,6,7,8,9,10,11,12,13,14}};
                                            
    public final int[][] Ideales2 = {{2,3,4,5,6,7,8,10,12,14},
                                    {2,4,6,7,8,9,10,11,12,13,15},
                                    {2,4,5,6,7,8,9,10,11,12,13,15}};
                                            
    public final int[][] Ideales3 = {{0,3,4,5,6,7,8,9,11,15},
                                    {2,4,6,7,8,9,10,11,12,13,16},
                                    {2,4,5,6,7,8,9,10,11,12,13,15}};

    
    public boolean canPutIdealy(int num, Jeu j, int joueur){
        boolean b = false;
        ArrayList<Integer> placeValide = construirePossibilite(num, j.joueurs[joueur]);
        for(int i=2;i>=0;i--){
            for(int k=0; k<j.joueurs[joueur].ville.rues[i].taille;k++){
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
    public boolean canPut(int num, Jeu j, int joueur){
        boolean b = false;
        ArrayList<Integer> placeValide = construirePossibilite(num, j.joueurs[joueur]);
        for(int i=2;i>=0;i--){
            for(int k=0; k<j.joueurs[joueur].ville.rues[i].taille-1;k++){
                if(j.joueurs[joueur].ville.rues[i].maisons[k].numero== num-1 && placeValide.contains(100*i +k+1)){
                    b = true;
                    return b;
                }
            }
        }
        for(int i=2;i>=0;i--){
            for(int k=1; k<j.joueurs[joueur].ville.rues[i].taille;k++){
                if(j.joueurs[joueur].ville.rues[i].maisons[k].numero== num+1 && placeValide.contains(100*i +k-1)){
                    b = true;
                    return b;
                }
            }
        }

        return b;
    }
  

 

    //Choisir parmi les 3 numéros dispos
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        this.tour++;

        int res = RandomSingleton.getInstance().nextInt(3);

        // on place dans un tableau les duos actions_numeros
       int [][] num_act = new int[][]{
        {((Travaux)j.numeros[0].top()).getNumero(), ((Travaux)j.actions[0].top()).getAction()},
        {((Travaux)j.numeros[1].top()).getNumero(), ((Travaux)j.actions[1].top()).getAction()},
        {((Travaux)j.numeros[2].top()).getNumero(), ((Travaux)j.actions[2].top()).getAction()}
    };

        
        if(tour<=22){
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==3 && canPutIdealy(num, j, joueur) ){
                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==5 && canPutIdealy(num, j, joueur)&& barriere<10 ){
                    res=i;
                    return res;
                }       
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==4  && canPutIdealy(num, j, joueur) ){
                    res=i;
                    return res;
                }       
            }
            
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==1  && canPutIdealy(num, j, joueur) ){
                    res=i;
                    return res;
                }       
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==0 && canPutIdealy(num, j, joueur) ){
                        res=i;   
                        return res;
                }
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
        
                if( canPutIdealy(num, j, joueur) || canPut(num, j, joueur) ){
                    res=i;
                    return res;
                }  
            }
        }
        else {
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==4 && canPutIdealy(num, j, joueur) ){
                    res=i;
                    return res;
                }       
            }
            
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==5  && canPutIdealy(num, j, joueur) && barriere<10 ){
                    res=i;
                    return res;
                }       
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==3 &&  canPutIdealy(num, j, joueur) ){

                    res=i;
                    return res;
                }
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==1  && canPutIdealy(num, j, joueur) ){
                    res=i;
                    return res;
                }       
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
                int act = num_act[i][1];
                if(act==0 && canPutIdealy(num, j, joueur) ){
                        res=i;   
                        return res;
                }
            }
            for(int i=0;i<3;i++){ 
                int num= num_act[i][0];
        
                if( canPutIdealy(num, j, joueur) || canPut(num, j,  joueur) ){
                    res=i;
                    return res;
                }  
            } 
            
        } 
        return res;
    }       
     
        
        

    
    //Choisir de placer un numéro bis
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        return 0;
    }
    
    
    
   
    //Choisir  parmi les emplacements dispos
    @Override
    // on choisit juste en fonctions des numéros
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        int res=RandomSingleton.getInstance().nextInt(placeValide.size()); 
        
        //si on peut placer idéalement
        for(int i=2;i>=0;i--){
            for(int k=0; k<j.joueurs[joueur].ville.rues[i].taille;k++){
                if(placeValide.contains(100*i +k)){
                    if(numero==Ideales1[i][k]  || numero==Ideales2[i][k] || numero==Ideales3[i][k] ){
                        return placeValide.indexOf(100*i+k); 
                    }  
                    
                }
                
            }
        }
        // sinon si on peut placer quand même juste après
        for(int i=2;i>=0;i--){
            for(int k=0; k<j.joueurs[joueur].ville.rues[i].taille -1;k++){
                if(j.joueurs[joueur].ville.rues[i].maisons[k].numero== numero-1 && placeValide.contains(100*i +k+1)){
                    return placeValide.indexOf(100*i+k+1);
                }
            }
        }
         // sinon si on peut placer quand même juste avant 
        for(int i=2;i>=0;i--){
            for(int k=1; k<j.joueurs[joueur].ville.rues[i].taille ;k++){
                if(j.joueurs[joueur].ville.rues[i].maisons[k].numero== numero+1 && placeValide.contains(100*i +k-1)){
                    return placeValide.indexOf(100*i+k-1);
                }
            }
        }
    return res;
}
    

    
    //Choisir le même numéro que celui de la carte quand l'action est un intérimaire
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        return numero;
    }
    

    @Override
    public int valoriseLotissement(Jeu j, int joueur){      
        if(valotissement<=0){
            valotissement++;
            return 1;
        }
        else{
            valotissement++;
            return 6;
        }
    }
    
    
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
        return true;
    }
    
    @Override
    public void resetStrat(){
        this.barriere=0;
        this.tour=0;
        this.valotissement=0;
    };


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
