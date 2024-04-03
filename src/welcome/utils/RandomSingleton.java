package welcome.utils;

import java.util.Random;

/*
 * Classe RandomSingleton 
 * Singleton est un patron de conception de création(design pattern)
 * qui garantit que l’instance d’une classe n’existe qu’en un seul exemplaire
 * tout en fournissant un point d’accès global à cette instance
*/
public class RandomSingleton {
    private static RandomSingleton instance;
    private final Random rnd;

    // Constructeur privée
    private RandomSingleton() {
        rnd = new Random();
    }

    // Méthode public qui permet de récupérer l'instance de la classe (et de la créer si c'est le 1er appel)
    public static RandomSingleton getInstance() {
        if(instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }
    
    // nextInt retourne un entier aléatoire dans l'intervalle [0, a[
    // Utilise la méthode du même nom de la classe Random.
    public int nextInt(int a) {
         return rnd.nextInt(a);
    } 
}