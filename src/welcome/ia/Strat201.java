package welcome.ia;
import java.util.ArrayList;
import welcome.*;
import welcome.utils.Couleur;
import welcome.utils.RandomSingleton;

public class Strat201 extends Strat{
    // bot Bidon
    
    public Strat201(){
    }
    
    @Override
    public String nomVille(){
        return "";
    }
    
    @Override
    public String nomJoueur(){
        return "MARIOT,Antoine";
    }
    public final int[][]plateauideal={{1,2,3,4,5,6,7,8,9,10},
                                      {2,3,4,6,8,10,11,12,13,14,15},
                                      {1,2,3,5,6,7,8,9,10,11,13,15}};
    
    public final int[]plateaubarrière={5,14,25};
    public final int[]plateaupiscine={6,7,107,210};
    
    
    public boolean parc;
    public boolean piscine;
    public boolean barrière;
    public boolean agentimmo;
    //Choisir au hasard parmi les 3 numéros dispos
    @Override
    public int choixCombinaison(Jeu j, int joueur){
        int []tabActions=new int[3];
        int []tabNumeros=new int[3];
        for(int i=0;i<3;i++){
            Travaux t = (Travaux)(j.actions[i].top());
            Travaux n =(Travaux)j.numeros[i].top();
            tabActions[i]=t.getAction();
            tabNumeros[i]=n.getAction();
        }
        for(int i=0;i<3;i++){
            ArrayList<Integer> possibilités= construirePossibilite(tabNumeros[i], j.joueurs[joueur]);
            if(tabActions[i]==3){
                if(j.joueurs[joueur].ville.nbParcs[2]<5 || j.joueurs[joueur].ville.nbParcs[1]<4 || j.joueurs[joueur].ville.nbParcs[2]<3 ){
                    parc=true;
                    return i;  
                }
            }
            
            else if(tabActions[i]==5){
                if(j.joueurs[joueur].ville.barrieres[0][5]==false || j.joueurs[joueur].ville.barrieres[1][5]==false || j.joueurs[joueur].ville.barrieres[2][6]==false ){
                    barrière=true;
                    return i;
                    
                }
            }
            else if(tabActions[i]==4){
                if(j.joueurs[joueur].ville.avancementPrixLotissement[5]!=4 || j.joueurs[joueur].ville.avancementPrixLotissement[4]!=4 ){
                    agentimmo=true;
                    return i;
                }
            }
        }
        
        //avancementPrixLotissement = new int[]{0,0,0,0,0,0};
        //maxAvancement = new int[]{1, 2, 3, 4, 4, 4};
        for(int i=0; i<3; i++){//Pour chaque carte 
             
            ArrayList<Integer> possibilités= construirePossibilite(tabNumeros[i], j.joueurs[joueur]);
            for(int x=0;x<possibilités.size();x++){
                for(int y=0;y<plateauideal[2].length;y++){
                    if(plateauideal[2][y]==tabNumeros[i]&& (possibilités.get(x)-200)==y){
                        return i;
                    }
                }
                for(int y=0;y<plateauideal[1].length;y++){
                    if(plateauideal[1][y]==tabNumeros[i] && (possibilités.get(x)-100)==y){
                        return i;
                    }
                }
                for(int y=0;y<plateauideal[0].length;y++){
                    if(plateauideal[0][y]==tabNumeros[i] && (possibilités.get(x))==y){
                        return i;
                    }
                }
            }
        }

                
                     
           
         return RandomSingleton.getInstance().nextInt(3);

        }
        
    
    //Choisir de placer un numéro bis
    @Override
    public int choixBis(Jeu j, int joueur, ArrayList<Integer> placeValide){
        int res=-1;
        
        
        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }
    
    //Choisir au hasard parmi les emplacements dispos
    @Override
    public int choixEmplacement(Jeu j, int joueur, int numero, ArrayList<Integer> placeValide){
        int res=-1;
        if(parc || barrière || agentimmo){
           for(int x=0;x<placeValide.size();x++){
            if(placeValide.get(x)>=200){
                for(int y=0;y<plateauideal[2].length;y++){
                    if(plateauideal[2][y]==numero && (placeValide.get(x)-200)==y){
                        parc=false;
                        barrière=false;
                        agentimmo=false;
                        return x;
                    }
                } 
            }
            else if(placeValide.get(x)<200 && placeValide.get(x)>10){
                for(int y=0;y<plateauideal[1].length;y++){
                    if(plateauideal[1][y]==numero && (placeValide.get(x)-100)==y){
                        parc=false;
                        barrière=false;
                        agentimmo=false;
                        return x;
                    }
                } 
            }
            else {
                for(int y=0;y<plateauideal[0].length;y++){
                    if(plateauideal[0][y]==numero && (placeValide.get(x))==y){
                        parc=false;
                        barrière=false;
                        agentimmo=false;
                        return x;
                    }
                }
            }
        } 
        }
        
        
        
        for(int x=0;x<placeValide.size();x++){
            if(placeValide.get(x)>=200){
                for(int y=0;y<plateauideal[2].length;y++){
                    if(plateauideal[2][y]==numero && (placeValide.get(x)-200)==y){
                        return x;
                    }
                } 
            }
            else if(placeValide.get(x)<200 && placeValide.get(x)>10){
                for(int y=0;y<plateauideal[1].length;y++){
                    if(plateauideal[1][y]==numero && (placeValide.get(x)-100)==y){
                        return x;
                    }
                } 
            }
            else {
                for(int y=0;y<plateauideal[0].length;y++){
                    if(plateauideal[0][y]==numero && (placeValide.get(x))==y){
                        return x;
                    }
                }
            }
        }
        
        
         
            
        
        
        
        
        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }
    
    //Choisir le même numéro que celui de la carte quand l'action est un intérimaire
    @Override
    public int choixNumero(Jeu j, int joueur, int numero){
        int res=-1;
        
        
        //A COMPLETER
        
        if((res<(numero-2) || res>(numero+2)) || res<0)
            res=Math.max(0, RandomSingleton.getInstance().nextInt(5) + numero - 2) ;
        return res;
    }
    
    //Valorise aléatoirement une taille de lotissements (proba plus forte si plus d'avancements possibles)
    @Override
    public int valoriseLotissement(Jeu j, int joueur){        
        int res=-1;
        if(j.joueurs[joueur].ville.avancementPrixLotissement[5]!=4){
            return 6;
            }
        else if(j.joueurs[joueur].ville.avancementPrixLotissement[4]!=4){
            return 5;
        }
        
        if(res<1 || res>6)
            res=RandomSingleton.getInstance().nextInt(6)+1;
        return res;
    }
    
    //Met une barrière à une position aléatoire
    @Override
    public int choixBarriere(Jeu j, int joueur,  ArrayList<Integer> placeValide){
        int res=-1;
        for(int i=0;i<placeValide.size();i++){
            if(placeValide.get(i)==5 || placeValide.get(i)==105 ||placeValide.get(i)==206 )
                return i;
        }
        
        if(res<0 || res>placeValide.size()-1)
            res=RandomSingleton.getInstance().nextInt(placeValide.size());
        return res;
    }
    
    //Valide toujours un plan
    @Override
    public boolean validePlan(Jeu j, int joueur, int plan) {
        boolean res = true;
        
        //A COMPLETER
        
        return res;
    }
    
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
    @Override
    public void resetStrat(){
         parc=false;
         piscine=false;
         barrière=false;
         agentimmo=false;
    };

}
