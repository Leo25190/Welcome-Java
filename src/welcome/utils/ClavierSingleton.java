package welcome.utils;

import java.util.Scanner;

/*
 * Classe ClavierSingleton 
 * Singleton est un patron de conception de création(design pattern)
 * qui garantit que l’instance d’une classe n’existe qu’en un seul exemplaire
 * tout en fournissant un point d’accès global à cette instance
*/
public class ClavierSingleton {
    private static ClavierSingleton instance;
    private final Scanner s;

    // Constructeur privée
    private ClavierSingleton() {
        s = new Scanner(System.in);
    }

    // Méthode public qui permet de récupérer l'instance de la classe (et de la créer si c'est le 1er appel)
    public static ClavierSingleton getInstance() {
        if(instance == null) {
            instance = new ClavierSingleton();
        }
        return instance;
    }

    // Méthode permettant de récupérer un entier entre 2 bornes en gérant la levée d'exception en cas de valeur eronnée
    public int nextIntBetween(int borneInf, int borneSup) {
        int res=borneInf-1;
        boolean firstTime=true;
        System.out.println("Veuillez entrer un entier dans l'intervale " + Couleur.ROUGE_BOLD + "[" + borneInf + ", " + borneSup + "]." + Couleur.RESET);     
        while(res<borneInf || res > borneSup){
            try{
                if(!firstTime)
                    System.out.println("L'entier doit être dans l'intervale " + Couleur.ROUGE_BOLD + "[" + borneInf + ", " + borneSup + "]" + Couleur.RESET + ". Merci de fournir une valeur valide.");
                firstTime=false;
                res=s.nextInt();
            }
            catch(Exception e){
                System.out.println("L'exception suivante a été levé:\n"  + e);
                System.out.println("Nouvelle tentative...");
                s.nextLine();
            }
        }
        //System.out.println("Valeur renseignée: " + res);
        return res;
    }
    
    // Permet de récuper un boolean sous le format 0, 1 (plus pratique à taper que true ou false)
    // Utilise la méthode nextIntBetween
    public boolean nextBoolean() {
        System.out.println("0: false, 1: true");
        int r= nextIntBetween(0,1);
        return (r==1);
    }
}
