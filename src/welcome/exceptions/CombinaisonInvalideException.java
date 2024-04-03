package welcome.exceptions;

//Exception quand la combinaison retourné n'est pas dans l'intervalle[0-2]
public class CombinaisonInvalideException extends Exception{
    
    public CombinaisonInvalideException(){
        super();
    }
    
    public CombinaisonInvalideException(String s){
        super(s);
    }
}
