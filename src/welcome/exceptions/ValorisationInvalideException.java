package welcome.exceptions;

//Exception quand la valeur retourné n'est pas dans l'intervalle [1-6]
public class ValorisationInvalideException extends Exception {
    public ValorisationInvalideException(){
        super();
    }
    
    public ValorisationInvalideException(String s){
        super(s);
    }
}
