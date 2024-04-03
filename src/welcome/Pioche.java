package welcome;

import welcome.utils.RandomSingleton;
import java.util.ArrayList;

public class Pioche implements Cloneable{
    private ArrayList<Carte> cartes; //un paquet de carte
    
    @Override
    public Object clone() throws CloneNotSupportedException {      
        Pioche pioche= (Pioche)super.clone();
        pioche.cartes=new ArrayList<>();
        pioche.cartes.addAll(this.cartes);
        return pioche;
    }
    
    //Constructeur
    public Pioche(){
        cartes=new ArrayList();
    }
    
    //Ajout d'une carte au dessus de la pioche (dernière élément de la liste)
    public void ajouter(Carte c){
        cartes.add(c);
    }
    
    //Obtenir la taille de la pioche (nombre de cartes)
    public int getTaille(){
        return cartes.size();
    }
    
    //vider la pioche
    public void vider(){
        cartes.clear();
    }
    
    //Melange de la pioche
    //Melange de Fisher-Yates
    public void melanger(){
        Carte tmp;
        for(int i=cartes.size()-1; i>0 ; i--){
            int j=RandomSingleton.getInstance().nextInt(i);
            tmp= cartes.get(i);
            cartes.set(i, cartes.get(j));
            cartes.set(j, tmp);
        }
    }
    
    /*
     * Permet de piocher la carte du haut de la pioche (la carte n'y sera plus)
     * Certe dernière sera donc enlever de l'ArrayList
    */
    public Carte piocher(){
        Carte res=null;
        if(!cartes.isEmpty()){
            res=cartes.get(cartes.size()-1);
            cartes.remove(cartes.get(cartes.size()-1));
        }
        return res;
    }
    
    /*
     * Permet de voir la carte du haut de la pioche (la carte y reste)
    */
    public Carte top(){
        Carte res=null;
        if(!cartes.isEmpty()){
            res=cartes.get(cartes.size()-1);
        }
        return res;
    }
    
    //Permet d'afficher l'ensemble des cartes d'une pioche
    //Utile pour débug
    @Override
    public String toString(){
        String res="";
        for(Carte elem: cartes)
            res+=elem.toString()+"\n";
        return res;
    }
}
