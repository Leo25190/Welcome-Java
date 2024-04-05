package welcome;
import static java.lang.Integer.min;
import java.util.LinkedList;
import java.util.ListIterator;
import welcome.utils.Couleur;

public class Ville implements Cloneable{
    
    public String nom; // le nom de la ville
    public Rue[] rues; // les rues de la ville
    public LinkedList<Lotissement> lotissements; // les lotissements de la ville
    public boolean[][] barrieres; // les barrières de la ville
    public int[] nbParcs; // le nombre de parcs dans chaque rue
    public int[] maxParcs; //  le maximum de parc de chaque rue
    public int[] avancementPrixLotissement; // l'avancement sur les prix en fonction des tailles de lotissements
    public int[] maxAvancement; // l'avancement max pour chaque taille de lotissement
    public int nbPiscine; // le nombre de piscine construite dans la ville
    public int nbInterimaire; // le nombre d'intérimaire embauché par la ville
    public int nbBis; // le nombre de numéro bis de la ville
    
    //Constructeur
    public Ville(String _nom){
        nom=_nom;
        nbPiscine=0;
        nbInterimaire=0;
        nbBis=0;
        this.creerRues();
        this.creerLotissement();
        this.creerParcs();
        this.creerAvancementLotissement();
        this.creerBarrieres();
    }
    
    public void reset(){
        this.creerRues();
        this.creerLotissement();
        this.creerParcs();
        this.creerAvancementLotissement();
        this.creerBarrieres();
        nbPiscine=0;
        nbInterimaire=0;
        nbBis=0;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {      
        Ville ville= (Ville)super.clone();
        ville.nom=this.nom;
        ville.rues=new Rue[this.rues.length];
        for(int i=0; i<this.rues.length; i++)
            ville.rues[i]=(Rue)this.rues[i].clone();
        ville.lotissements=new LinkedList();
        for(int i=0; i<this.lotissements.size();i++){
            ville.lotissements.add((Lotissement)this.lotissements.get(i).clone());
        }
        int acc=0;
        for(int i=0; i<ville.lotissements.size();i++){
            switch (acc) {
                case 0:
                    ville.lotissements.get(i).rue=ville.rues[0];
                    if(ville.lotissements.get(i).fin==10)
                        acc++;
                    break;
                case 1:
                    ville.lotissements.get(i).rue=ville.rues[1];
                    if(ville.lotissements.get(i).fin==11)
                        acc++;
                    break;
                default:
                    ville.lotissements.get(i).rue=ville.rues[2];
                    break;
            }
        }
        ville.barrieres=new boolean[3][];
        ville.barrieres[0]=new boolean[11];
        ville.barrieres[1]=new boolean[12];
        ville.barrieres[2]=new boolean[13];
        for(int i=0; i<this.barrieres.length; i++){
            for (int j=0; j<this.barrieres[i].length; j++){
                ville.barrieres[i][j]=this.barrieres[i][j];
            }
        }
        ville.nbParcs=new int[this.nbParcs.length];
        System.arraycopy(this.nbParcs, 0, ville.nbParcs, 0, this.nbParcs.length);
        ville.maxParcs= new int[this.maxParcs.length];
        System.arraycopy(this.maxParcs, 0, ville.maxParcs, 0, this.maxParcs.length);
        ville.avancementPrixLotissement = new int[this.avancementPrixLotissement.length];
        System.arraycopy(this.avancementPrixLotissement, 0, ville.avancementPrixLotissement, 0, this.avancementPrixLotissement.length);
        ville.maxAvancement= new int[this.maxAvancement.length];
        System.arraycopy(this.maxAvancement, 0, ville.maxAvancement, 0, this.maxAvancement.length);
        ville.nbPiscine=this.nbPiscine;
        ville.nbInterimaire=this.nbInterimaire;
        ville.nbBis=this.nbBis;
        return ville;
    }
    
    //Initiatlisation du tableau des barrières
    private void creerBarrieres(){
        barrieres= new boolean[3][];
        barrieres[0]=new boolean[11];
        barrieres[1]=new boolean[12];
        barrieres[2]=new boolean[13];
        //On met 2 barrières aux extrémités des 3 rues
        for(int i=0; i<3;i++)
            for(int j=0; j<barrieres[i].length; j++)
                barrieres[i][j]= ((j==barrieres[i].length-1)|| j==0);
    }
    
    //Initialisation des parcs
    private void creerParcs(){
        nbParcs =  new int[3];
        maxParcs = new int[3];
        nbParcs[0]=0;
        nbParcs[1]=0;
        nbParcs[2]=0;
        maxParcs[0]=3;
        maxParcs[1]=4;
        maxParcs[2]=5;
    }
    
    //Initialisation des valeurs des lotissements
    private void creerAvancementLotissement(){
        avancementPrixLotissement = new int[]{0,0,0,0,0,0};
        maxAvancement = new int[]{1, 2, 3, 4, 4, 4};
    }
    
    //Construction d'un  parc dans une rue
    public void construireParc(int _rue){
        nbParcs[_rue]= min(maxParcs[_rue], nbParcs[_rue]+1);
    }
    
    //Création des rues
    private void creerRues(){
        rues = new Rue[3];
        rues[0]= new Rue(1, 10);
        rues[1]= new Rue(2, 11);
        rues[2]= new Rue(3, 12);    
        rues[0].maisons[2].creerUnEmplacementPiscine();
        rues[0].maisons[6].creerUnEmplacementPiscine();
        rues[0].maisons[7].creerUnEmplacementPiscine();
        rues[1].maisons[0].creerUnEmplacementPiscine();
        rues[1].maisons[3].creerUnEmplacementPiscine();
        rues[1].maisons[7].creerUnEmplacementPiscine();
        rues[2].maisons[1].creerUnEmplacementPiscine();
        rues[2].maisons[6].creerUnEmplacementPiscine();
        rues[2].maisons[10].creerUnEmplacementPiscine();
    }
    
    //Création des lotissemnts
    private void creerLotissement(){
        lotissements = new LinkedList<>();
        lotissements.addLast(new Lotissement(rues[0], 0, 10));
        lotissements.addLast(new Lotissement(rues[1], 0, 11));
        lotissements.addLast(new Lotissement(rues[2], 0, 12));
    }
    
    //Ajouter une barrière à la ville
    public void ajouterBarriere(int _rue, int _num){
        barrieres[_rue][_num]=true;
        ListIterator<Lotissement> it = lotissements.listIterator();
        boolean find=false;
        while (it.hasNext() && !find) {
            Lotissement l = it.next();
            if(l.rue.numero == (_rue+1) && l.fin>_num){
                find=true;
                it.add(new Lotissement(rues[_rue], _num, l.fin));
                l.fin=_num;
                l.taille=l.fin-l.debut;
            }
        }
    }
    
    //Affichage de la ville
    @Override
    public String toString(){
        boolean b; // Utile pour les lotisssements non dispo
        String barriere=Couleur.FOND_BLEU + Couleur.BLANC + "+" + Couleur.RESET;
        //String barriere="🚧";
        
        String res=Couleur.FOND_VIOLET + Couleur.BLANC + "###  " + this.nom + "  ###" + Couleur.RESET;
        res+=Couleur.CYAN + "\tPiscine: " + nbPiscine + Couleur.JAUNE + "\tIntérimaire: " + nbInterimaire + Couleur.ROUGE + "\tBis: " + nbBis + Couleur.RESET + "\n";
        /*
        lotissements.forEach(element -> {
            System.out.println(element);
        });
        */
        for(int i=0; i<rues.length; i++){
            res+= Couleur.FOND_BLANC + Couleur.ROUGE + "--- Rue "+ (i+1) + " ---\t" + Couleur.VERT + "parcs:" + Couleur.RESET;
            for(int j=0; j<=maxParcs[i]; j++){
                if(nbParcs[i]>j)
                    res+=(Couleur.FOND_BLANC + " X");
                else if(nbParcs[i]==j)
                    res+=(Couleur.FOND_BLANC + " " + Couleur.VERT + Jeu.pointParcs[i][j] + Couleur.RESET);
                else
                    res+=(Couleur.FOND_BLANC + " " + Jeu.pointParcs[i][j]);
            }
            res+="\n";
            res+=barriere;
            for(int j=0; j<rues[i].taille; j++){
                if(rues[i].maisons[j].piscine)
                    res+="   " + Couleur.CYAN + (char)(213) + Couleur.RESET + "   ";
                else if(rues[i].maisons[j].aUnEmplacementPiscine() && rues[i].maisons[j].numero<0)
                    res+="   O   ";
                else if(rues[i].maisons[j].aUnEmplacementPiscine() && rues[i].maisons[j].numero>=0)
                    res+="   " + (char)(216) + "   ";
                else
                    res+="       ";
                if(barrieres[i][j+1])
                    res+=barriere;
                else
                    res+=Couleur.BLANC + "|" + Couleur.RESET;
            }
            res+="\n"+barriere;
            for(int j=0; j<rues[i].taille; j++){
                if(rues[i].maisons[j].numero <0 || rues[i].maisons[j].numero>9){
                    if(rues[i].maisons[j].numero <0)
                        res+=Couleur.BLANC;
                    else
                        res+=Couleur.ROUGE;
                    if(rues[i].maisons[j].bis)
                        res+="  " + rues[i].maisons[j].numero + Couleur.NOIR + "b  ";
                    else
                        res+="  " + rues[i].maisons[j].numero + Couleur.NOIR + "   ";
                }
                else{
                    if(rues[i].maisons[j].numero <0)
                        res+=Couleur.BLANC;
                    else
                        res+=Couleur.ROUGE;
                    if(rues[i].maisons[j].bis)
                        res+="   " + rues[i].maisons[j].numero + Couleur.NOIR + "b  ";
                    else
                        res+="   " + rues[i].maisons[j].numero + Couleur.NOIR + "   ";
                }
                if(barrieres[i][j+1])
                    res+=barriere;
                else
                    res+=Couleur.BLANC + "|" + Couleur.RESET;
            }
            if(i==0)
                res+="\n---------------------------------------------------------------------------------";
            else if (i==1)
                res+="\n-----------------------------------------------------------------------------------------";
            else if (i==2)
                res+="\n-------------------------------------------------------------------------------------------------";
            res+="\n" + barriere;
            for(int j=0; j<rues[i].taille; j++){
                b=estDansUnLotissementNonDispo(i+1,j);
                if(j<9 || b){
                    if(i==0){
                        if(!b)
                            res+="   "+ Couleur.CYAN +( j+1) + Couleur.RESET + "   ";
                        else
                            res+="   X" + Couleur.RESET + "   ";
                    }
                    else if(i==1){
                        if(!b)
                            res+="   " + Couleur.JAUNE +( j+1) + Couleur.RESET + "   ";
                        else
                            res+="   X" + Couleur.RESET + "   ";
                    }
                    else{
                        if(!b)
                            res+="   " + Couleur.VERT +( j+1) + Couleur.RESET + "   ";
                        else
                            res+="   X" + Couleur.RESET + "   ";
                    }
                    if(barrieres[i][j+1])
                    res+=barriere;
                else
                    res+=Couleur.BLANC + "|" + Couleur.RESET;
                }
                else{
                    if(i==0)
                        res+="  " + Couleur.CYAN +( j+1) + Couleur.RESET + "   ";
                    else if(i==1)
                        res+="  " + Couleur.JAUNE +( j+1) + Couleur.RESET + "   ";
                    else
                        res+="  " + Couleur.VERT +( j+1) + Couleur.RESET + "   ";
                    if(barrieres[i][j+1])
                    res+=barriere;
                else
                    res+=Couleur.BLANC + "|" + Couleur.RESET;
                }
            }
            res+="\n\n";        
        }
        
        res+= Couleur.FOND_BLANC + Couleur.VIOLET + "Valeurs de vos lotissements:\n";
        res+= Couleur.RESET + Couleur.VIOLET + "Taille:";
        for(int i=0; i<6; i++)
            res+="\t" + (i+1);
        res+=Couleur.VIOLET + "\nValeur:";
        for(int i=0; i<6; i++){
            if(avancementPrixLotissement[i] == maxAvancement[i])
                res+="\t" + Couleur.NOIR + Jeu.valeurLotissements[i][avancementPrixLotissement[i]];
            else
                res+="\t" + Couleur.VIOLET + Jeu.valeurLotissements[i][avancementPrixLotissement[i]];
        }
        res+=Couleur.RESET + "\n";
        
        
        
        return res;
    }
    
    boolean estDansUnLotissementNonDispo(int rue, int num){
        boolean res=false;
        Lotissement l;
        for (int i=0; i<lotissements.size(); i++){
            l=lotissements.get(i);
            if(!l.dispo && l.rue.numero==rue && num>=l.debut && num<l.fin)
                res=true;
        }
        
        return res;
    }
    
}
