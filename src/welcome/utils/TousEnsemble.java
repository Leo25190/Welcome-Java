package welcome.utils;
import java.util.Arrays;
import java.util.Comparator;
import welcome.*;

public class TousEnsemble extends Tournoi{

    public int nbPartie;
    
    public TousEnsemble(int[] _strats, int _nbPartie) throws NoSuchMethodException{
        super(_strats);
        nbPartie=_nbPartie;
    }
    
    @Override
    public int[][] run() {
        int[][] score=new int[this.participants.length][2];
        double[][] tousLesScores = new double[this.participants.length][nbPartie];

        int[] scorePartie;
        int place;
        int scoreTmp;
        int cpt;
        Integer[] classement;
        Jeu j = new Jeu(this.participants);
        j.verboseOnOff(false);
        for(int h=0; h<nbPartie; h++){           
            scorePartie=j.jouer();

            for(int i = 0; i < this.participants.length; i++){
                tousLesScores[i][h] = scorePartie[i];
            }

            classement= new Integer[this.participants.length];
            for(int i=0; i<classement.length; i++)
                classement[i]=i;
            final int[] valeursFinal = scorePartie;
            Arrays.sort(classement, Comparator.comparingInt(i -> valeursFinal[i]));
            
            System.out.println(Couleur.ROUGE + Couleur.FOND_CYAN + "Partie " + (h+1) + ":" + Couleur.RESET);
            scoreTmp=500;
            place=0;
            cpt=1;
            for(int i=classement.length-1; i>=0; i--){
                System.out.println(this.participants[classement[i]].nom + " a fini avec " + scorePartie[classement[i]] + " points.");
                if(scorePartie[classement[i]]<scoreTmp){
                    place+=cpt;
                    cpt=1;
                    scoreTmp=scorePartie[classement[i]];
                    score[classement[i]][0]+=place;
                    score[classement[i]][1]+=scorePartie[classement[i]];
                }
                else{
                    cpt++;
                    score[classement[i]][0]+=place;
                    score[classement[i]][1]+=scorePartie[classement[i]];
                }
            }
            System.out.println("");

            j.reset();
            for(int i=0; i<this.participants.length; i++)
                participants[i].resetStrat();
            j.verboseOnOff(false);
        }
        for(int i=0; i<score.length; i++) {
            System.out.println(this.participants[i].nom + " a fini en moyenne " + ((double) score[i][0]) / nbPartie + " avec un score moyen de " + ((double) score[i][1]) / nbPartie + " points");

            System.out.println("SCORE MIN : " + minimum(tousLesScores[i]));
            System.out.println("SCORE MAX : " + maximum(tousLesScores[i]));
            System.out.println("MEDIANE : " + mediane(tousLesScores[i]));
            System.out.println("PREMIER QUARTILE : " + premierQuartile(tousLesScores[i]));
            System.out.println("TROISIEME QUARTILE : " + troisiemeQuartile(tousLesScores[i]));
            System.out.println("ECART-TYPE : " + Math.sqrt(variance(tousLesScores[i])));
        }
        return score;
    }

    // Méthodes de calcul des statistiques

    // Méthode pour calculer le minimum d'un tableau d'entiers
    public static double minimum(double[] tableau) {
        double min = tableau[0];
        for (int i = 1; i < tableau.length; i++) {
            if (tableau[i] < min) {
                min = tableau[i];
            }
        }
        return min;
    }

    // Méthode pour calculer le maximum d'un tableau d'entiers
    public static double maximum(double[] tableau) {
        double max = tableau[0];
        for (int i = 1; i < tableau.length; i++) {
            if (tableau[i] > max) {
                max = tableau[i];
            }
        }
        return max;
    }

    // Méthode pour calculer la médiane d'un tableau d'entiers
    public static double mediane(double[] tableau) {
        Arrays.sort(tableau);
        int n = tableau.length;
        if (n % 2 == 0) {
            return (tableau[n / 2 - 1] + tableau[n / 2]) / 2.0;
        } else {
            return tableau[n / 2];
        }
    }

    // Méthode pour calculer le premier quartile d'un tableau d'entiers
    public static double premierQuartile(double[] tableau) {
        Arrays.sort(tableau);
        int n = tableau.length;
        if (n % 4 == 0) {
            return tableau[n / 4 - 1];
        } else {
            return tableau[n / 4];
        }
    }

    // Méthode pour calculer le troisième quartile d'un tableau d'entiers
    public static double troisiemeQuartile(double[] tableau) {
        Arrays.sort(tableau);
        int n = tableau.length;
        if (n % 4 == 0) {
            return tableau[3 * n / 4 - 1];
        } else {
            return tableau[3 * n / 4];
        }
    }

    // Méthode pour calculer la variance d'un tableau d'entiers
    public static double variance(double[] tableau) {
        double moyenne = Arrays.stream(tableau).average().orElse(Double.NaN);
        double sommeCarres = 0;
        for (double valeur : tableau) {
            sommeCarres += Math.pow(valeur - moyenne, 2);
        }
        return sommeCarres / tableau.length;
    }


}
