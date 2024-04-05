package welcome;
import welcome.utils.Couleur;

public class Travaux extends Carte implements Cloneable{
    
    //Noms des actions
    public static String[] actions = new String[]{"Fabricant de piscine", "Agence d'intérim", 
    "Numéro Bis\t", "Paysagiste\t", "Agent Immobilier", "Géomètre\t"};
    
    //Couleurs associées aux travaux (peut être changé)
    public static String[] couleurs = new String[]{Couleur.CYAN, Couleur.JAUNE, Couleur.ROUGE, Couleur.VERT, Couleur.VIOLET, Couleur.BLEU};
    
    //Symboles associés aux travaux (peut être changé)
    public static String[] symboles = new String[]{"🏊", "🚧", "📮", "🌳", "💰", "💈"};
    //🏊
    public int numero; // Le numero de la carte travaux
    public int action; // L'action de la carte travaux
    
    //Constructeur
    public Travaux(int _numero, int _action){
        numero=_numero;
        action=_action;
    }
    
    
    public String getActionString(){
        return actions[action];
    }
    
    public String getCouleur(){
        return couleurs[action];
    }
    
    public String getSymbole(){
        return symboles[action];
    }
    
    public int getAction(){
        return action;
    }
    
    public int getNumero(){
        return numero;
    }
    
    @Override
    public String toString(){
        return "Carte Travaux: numéro " + numero + ", " + symboles[action] + " " + actions[action];
    }
    
    @Override
    public Travaux clone() throws CloneNotSupportedException{
        return (Travaux)super.clone();
    }
}
