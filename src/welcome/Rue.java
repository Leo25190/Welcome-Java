package welcome;
public class Rue implements Cloneable{
    
    public int numero; // Numéro de la rue: 1, 2 ou 3
    public int taille; // taille de la rue: 10, 11 ou 12
    public Maison[] maisons; // les maisons de la rue
    
    //Constructeur
    public Rue(int _numero, int _taille){
        numero=_numero;
        taille=_taille;
        //création des maisons
        maisons=new Maison[taille];
        for(int i=0; i<maisons.length; i++)
            maisons[i]= new Maison(numero, i);
    }
    
    public Object clone() throws CloneNotSupportedException {      
        Rue rue=(Rue)super.clone();
        rue.numero=this.numero;
        rue.taille=this.taille;
        rue.maisons= new Maison[this.maisons.length];
        for (int i=0; i<this.maisons.length; i++)
            rue.maisons[i]=(Maison)this.maisons[i].clone();
        return rue;
    }
}
