package welcome;

import welcome.ia.Strategie;

//Classe abstraite représentant un joueur (doit implémenter Stratégie)
public abstract class Joueur implements Strategie, Cloneable{

    public String nom; //nom du joueur
    public Ville ville; //ville du joueur 
    public boolean verbose; //mode verbose
    public int refusPermis; // nombre de refus de permis (au 3e la partie se termine)
    public int[] objectifs; // les points d'objectifs marqués par le joueur
    
    //Constructeur
    public Joueur(String _nom, String _nomVille){
        //Limitation des noms à 20 caractères
        if(_nomVille.length()>20)
            _nomVille=_nomVille.substring(0, 19);
        if(_nom.length()>20)
            _nom=_nom.substring(0, 19);
        nom=_nom;
        ville= new Ville(_nomVille);
        refusPermis=0;
        objectifs = new int[]{0,0,0};
    }
    
    public void reset(){
        refusPermis=0;
        objectifs = new int[]{0,0,0};
        ville.reset();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {      
        Joueur joueur= (Joueur)super.clone();
        joueur.nom=this.nom;
        joueur.ville=(Ville)this.ville.clone();
        joueur.verbose=this.verbose;
        joueur.refusPermis=this.refusPermis;
        joueur.objectifs=new int[this.objectifs.length];
        System.arraycopy(this.objectifs, 0, joueur.objectifs, 0, this.objectifs.length);
        return joueur;
    }
    
    //permet d'activer le mode versbose
    public void setVerbose(boolean v){
        verbose=v;
    }
    
    public String toString(){
        String res=nom;
        res+= ", refus de permis " + refusPermis + ", objectifs:";
        for(int i=0; i<objectifs.length; i++)
            res+= " " + objectifs[i];
        res+="\n";
        res+=ville;
        return res;
    }
    
}
