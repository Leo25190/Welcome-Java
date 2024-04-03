package welcome.utils;
import welcome.*;

/**
 *
 * @author jeremie.humeau
 */
public class Championnat1v1 extends Tournoi{

    public int nbPartie;

     public Championnat1v1(int[] _strats, int _nbPartie) throws NoSuchMethodException{
        super(_strats);
        nbPartie=_nbPartie;
    }
    
    @Override
    public int[][] run() {
        int[][] score= new int[this.participants.length][2];
        int[] scorePartie;
        int scoreJ1;
        int scoreJ2;
        for(int i=0; i<this.participants.length-1; i++){
            for(int j=i+1; j<this.participants.length; j++){
                System.out.print(i + " vs " + j + ": ");
                scoreJ1=0;
                scoreJ2=0;
                Joueur[] opp= new Joueur[2];
                opp[0]=this.participants[i];
                opp[1]=this.participants[j];
                Jeu jeu = new Jeu(opp);
                jeu.verboseOnOff(false);
                for(int h=0; h<nbPartie; h++){           
                    scorePartie=jeu.jouer();
                    if(scorePartie[0] > scorePartie[1])
                        scoreJ1++;
                    else if(scorePartie[0] < scorePartie[1])
                        scoreJ2++;
                    
                    jeu.reset();
                    participants[i].resetStrat();
                    participants[j].resetStrat();
                    jeu.verboseOnOff(false);
                }
                if (scoreJ1 > scoreJ2)
                    System.out.println(Couleur.ROUGE + scoreJ1 + Couleur.RESET + " - " + scoreJ2);
                else if (scoreJ1 == scoreJ2)
                    System.out.println(Couleur.BLEU + scoreJ1 + " - " + scoreJ2 + Couleur.RESET);
                else
                    System.out.println(scoreJ1 + " - " + Couleur.ROUGE + scoreJ2 + Couleur.RESET);
                score[i][1]+=scoreJ1;
                score[j][1]+=scoreJ2;
                if(scoreJ1>scoreJ2){
                    score[i][0]+=2;
                }
                else if(scoreJ1==scoreJ2){
                    score[i][0]++;
                    score[j][0]++;
                }
                else{
                    score[j][0]+=2;
                }
            }
        }
        for(int i=0; i<score.length; i++)
            System.out.println(participants[i].nom + " a " + score[i][0] + " points et a gagné un total de "+ score[i][1] + " parties.");
        return score;
    }
}
