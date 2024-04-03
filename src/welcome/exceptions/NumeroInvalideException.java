package welcome.exceptions;

//Exception quand le numéro retourné n'est pas dans l'intervalle autorisé
public class NumeroInvalideException extends Exception {
    
    public NumeroInvalideException(){
        super();
    }
    
    public NumeroInvalideException(String s){
        super(s);
    }
    
}
