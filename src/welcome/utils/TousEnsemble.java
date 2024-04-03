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
        int[] scorePartie;
        int place;
        int scoreTmp;
        int cpt;
        Integer[] classement;
        Jeu j = new Jeu(this.participants);
        j.verboseOnOff(false);
        for(int h=0; h<nbPartie; h++){           
            scorePartie=j.jouer();
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
        for(int i=0; i<score.length; i++)
            System.out.println(this.participants[i].nom + " a fini en moyenne " + ((double)score[i][0])/nbPartie + " avec un score moyen de " + ((double)score[i][1])/nbPartie + " points");
        return score;
    }
    
}
