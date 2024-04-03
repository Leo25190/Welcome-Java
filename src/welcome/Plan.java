package welcome;

public class Plan extends Carte implements Cloneable{
    int pointsPremier; // Points pour le ou les joueurs ayant validé en 1er ce plan (plusieurs joueurs peuvent y arriver pendant le mm tour de jeu)
    int pointsSuivants; // Points pour les suivants
    int[] tailleLotissements; // Contient la taile des lotissements nécessaires à la validation de ce plan
    int nbLotissement; // Nombre de lotissements nécessaires à la validation de ce plan
    int dejaAtteint; //0: pas encore atteint, 1: atteint à ce tour, 2: atteint un tour précèdent

    //Constructeur
    public Plan(int _pointsPremier, int _pointsSuivants, int[] _tailleLotissements, int _nbLotissement){
        pointsPremier=_pointsPremier;
        pointsSuivants=_pointsSuivants;
        tailleLotissements= new int[_nbLotissement];
        System.arraycopy(_tailleLotissements, 0, tailleLotissements, 0, _nbLotissement);
        nbLotissement=_nbLotissement;
        dejaAtteint=0;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {      
        Plan plan= (Plan)super.clone();
        
        return plan;
    }
    
    /*
     * Permet de checker si un plan peut être valider
     * @param _tab le tableau des tailles de lotissements complet et disponible d'un joueur
     * @return vrai si jamais le joueur a les lotissements nécessaires à la validation du plan
    */
    public boolean check(int[] _tab){
        boolean res= true;
        for(int i=0; i<tailleLotissements.length; i++)
            _tab[tailleLotissements[i]-1]--;
        for(int i=0; i<tailleLotissements.length; i++)
            if(_tab[tailleLotissements[i]-1]<0)
                res=false;
        return res;
    }
    
    //Affichage du plan
    @Override
    public String toString(){
        String res="Plan de " + nbLotissement + " lotissements:";
        for(int i=0; i<nbLotissement; i++)
            res+=" "+tailleLotissements[i];
        res+= ", rapporte " + pointsPremier + " au 1er et " + pointsSuivants + " aux suivants."; 
        return res;
    }
}
    

