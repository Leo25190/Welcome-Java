package welcome;

//Classe abstraite pour gérer les différentes cartes du jeu: Plan et Travaux
public abstract class Carte {
    
    //méthode abstraite à implémenter dans les sous classes afin d'afficher les informations d'une carte
    @Override
    abstract public String toString();  
}
