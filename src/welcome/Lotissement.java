package welcome;

//Classe Lotissement
public class Lotissement implements Cloneable{
    public Rue rue; // Référence de la Rue correspondante
    public int debut; // Début du lotissement
    public int fin; // Fin du lotissement
    public int taille; // Taille du lotissement (fin-debut)
    public boolean complet; // Est-ce que le lotissement contient que des maisons numérotées 
    public boolean dispo; // Est-ce que le lotissement est encore dispo pour la validation d'un objectif
    
    //Constructeur
    public Lotissement(Rue _rue, int _debut, int _fin){
        rue=_rue;
        debut=_debut;
        fin=_fin;
        taille=fin-debut;
        complet=false;
        dispo=true;
    }
    
    public Object clone() throws CloneNotSupportedException {      
        Lotissement l=(Lotissement)super.clone();
        l.rue=null;
        l.debut=this.debut;
        l.fin=this.fin;
        l.taille=this.taille;
        l.complet=this.complet;
        l.dispo=this.dispo;
        return l;
    }
    
    //Permet de vérifier si un lotissement est complet (fixe l'attribut complet à vrai le cas échéant)
    public void check(){
        int i=debut;
        while(i<fin && rue.maisons[i].numero>=0)
            i++;
        if(i==fin)
           complet=true;
    }
    
    // Affichage des informations du lotissement
    @Override
    public String toString(){
        String res="";
            res+="Lotissement de la Rue n°" + rue.numero + " de taille " + taille + " [" + debut + "-" + fin + "]. Complet: " + complet + ", dispo: "+ dispo;
        return res;
    }
}
