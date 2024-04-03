package welcome.exceptions;

//Exception quand le placement choisi n'est pas dans l'intervalle des placements générés
public class PlacementInvalideException extends Exception {

    public PlacementInvalideException(){
        super();
    }
    
    public PlacementInvalideException(String s){
        super(s);
    }
}
