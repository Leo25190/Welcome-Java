package welcome.exceptions;

//Exception quand l'emplacement de barriere retourné n'est pas dans l'intervalle des choix construits
public class BarriereInvalideException extends Exception{
       public BarriereInvalideException(){
        super();
    }
    
    public BarriereInvalideException(String s){
        super(s);
    } 
}
