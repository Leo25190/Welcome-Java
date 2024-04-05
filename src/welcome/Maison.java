package welcome;

public class Maison implements Cloneable{

    public int rue; // la rue 1, 2  ou 3
    public int position; //position dans la rue de 0 à 9, 10 ou 11 en fonction de la rue
    public int numero; // le numéro affecté à la maison [0 - 17] ou -1 quand pas encore de numéro 
    public boolean emplacementPiscine; // Est-ce que la maison a un emplacement pour construire une piscine
    public boolean piscine; //Est-ce qu'une piscine est construite ou non
    public boolean bis; //Est-ce qu'il y une 2e maison avec le même numéro   

    //Constructeur
    public Maison(int _rue, int _position){
        rue=_rue;
        position=_position;
        numero=-1;
        emplacementPiscine=false;
        piscine=false;
        bis=false;
    }
    
    public Object clone() throws CloneNotSupportedException {      
        Maison maison=(Maison)super.clone();
        maison.rue=this.rue;
        maison.position=this.position;
        maison.numero=this.numero;
        maison.emplacementPiscine=this.emplacementPiscine;
        maison.piscine=this.piscine;
        maison.bis=this.bis;
        return maison;
    }
    
    
    public void construirePiscine(){
        piscine=true;
    }
    
    public void creerUnEmplacementPiscine(){
        emplacementPiscine=true;
    }
    
    public boolean aUnEmplacementPiscine(){
        return emplacementPiscine;
    }
    
    public void setNumero(int _numero){
        numero=_numero;
    }
    
    //Test si il y a déjà un numéro ou non sur la maison
    public boolean estVide(){
        return numero<0;
    }   
}
