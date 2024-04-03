package welcome.utils;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import welcome.*;
import welcome.ia.*;

public abstract class Tournoi {

    Joueur[] participants;
    
    public Tournoi(int[] _strats) throws NoSuchMethodException{
        this.participants=new Joueur[_strats.length];
        for(int i=0; i<_strats.length; i++){
            try{
                Class classe = Class.forName("welcome.ia.Strat" + _strats[i]);
                Strat s = (Strat)classe.getDeclaredConstructor().newInstance();
                participants[i]=new Bot(s, s.nomJoueur(), s.nomVille());
                participants[i].setVerbose(false);
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            } catch (InstantiationException ex) {
                Logger.getLogger(Tournoi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Tournoi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Tournoi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Tournoi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public abstract int[][] run();
}
