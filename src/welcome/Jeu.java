package welcome;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.Comparator;
import welcome.exceptions.*;
import welcome.utils.Couleur;

public class Jeu implements Cloneable{
    
    //Valeurs des points en fonction de l'avancement sur chaque catégories
    public static final int[][] valeurLotissements= new int[][]{{1,3},{2,3,4},{3,4,5,6},{4,5,6,7,8},{5,6,7,8,10},{6,7,8,10,12}};
    public static final int[][] pointParcs = new int[][]{{0,2,4,10},{0,2,4,6,14},{0,2,4,6,8,18}};
    public static final int[] pointPiscines = new int[]{0,3,6,9,13,17,21,26,31,36};
    public static final int[] pointBis = new int[]{0,1,3,6,9,12,16,20,24,28};
    public static final int[] pointRefusDePermis= new int[]{0,0,3,5};
       
    public Pioche[] numeros; //pioches coté numero visible (Tableau qui sera initialisé par la suite avec une taille de 3) 
    public Pioche[] actions; //pioches coté actions visible (Tableau qui sera initialisé par la suite avec une taille de 3) 
    public Plan[] plans; //les plans disponibles (Tableau qui sera initialisé par la suite avec une taille de 3)  
    public Joueur[] joueurs; // le tableau des joueurs
    
    int tour; //un compteur de tour
    boolean verbose; //mode verbose
       
    public Jeu(Joueur[] _joueurs){
        joueurs=_joueurs;
        verbose=true; 
        tour=1;
        //On crée les 3 pioches de cartes Travaux (celles dont on voit le numéro)
        initCartesTravaux();
        //On crée les 3 pioches de cartes Travaux (celles dont on voit l'action)
        initActions();
        //On crée le tableau des cartes plans et on tire au hazard les 3 plans disponible pour la partie.
        initPlans();
        
    }
    
    public void reset(){
        tour=1;
        //On réinitialise les pioches de cartes
        initCartesTravaux();
        initActions();
        initPlans();
        //On réinitialise les joueurs et leurs villes
        for (Joueur joueur : this.joueurs) {
            joueur.reset();
        }
    }
    
    //TODO
    @Override
    public Object clone() throws CloneNotSupportedException {    
        Jeu j = (Jeu)super.clone();
        //Clone des pioches
        
        j.numeros=new Pioche[3];
        j.actions=new Pioche[3];
        j.numeros[0]=(Pioche)numeros[0].clone();
        j.numeros[1]=(Pioche)numeros[1].clone();
        j.numeros[2]=(Pioche)numeros[2].clone();
        j.actions[0]=(Pioche)actions[0].clone();
        j.actions[1]=(Pioche)actions[1].clone();
        j.actions[2]=(Pioche)actions[2].clone();
        
        //Clone des plans (apparemment Ã§a marche automatiquement)
        /*
        j.plans=new Plan[3];
        j.plans[0]=(Plan) this.plans[0].clone();
        j.plans[1]=(Plan) this.plans[1].clone();
        j.plans[2]=(Plan) this.plans[2].clone();
        */
        
        //Clone des joueurs
        j.joueurs=new Joueur[this.joueurs.length];
        for(int i=0; i<this.joueurs.length; i++)
            j.joueurs[i]=(Joueur)this.joueurs[i].clone();
        return j;
    }
    
    //Changer la valeur du mode verbose
    public void verboseOnOff(boolean b){
        verbose=b;
    }
    
    //Jouer une partie
    public int[] jouer(){
        //On déclare le tableau contenant le score des joueurs (qu'on va calculer en fin de partie)
        int[] score = new int[joueurs.length];
        //boucle de jeu principal
        //Tant que ce n'est pas le dernier tour, on joue un nouveau tour (tout simplement)
        while(!dernierTour()){
            try{
                jouerUnNouveauTour(); //nouveau tour
                tour++; //incrément du compteur de tour
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        //Fin de partie, on compte les points de tout le monde
        for(int i=0; i<joueurs.length; i++) // Pour chaque joueur:
            score[i]=calculerScore(joueurs[i]); // on compte ses points (ne prends pas en compte les intérimaires)
        ajoutPointInterimaires(score); // On regarde les intérimaires de chacun pour savoir quels joueurs marquent les bonus.
        return score; //Retour du tableau contenant les scores des joueurs
    }
    
    //Ajout des points pour les intérimaires
    //Comme on risque de faire jouer beaucoup de joueurs ensemble,
    //j'ai pris l'option d'attribuer 7 points Ã  tout les premiers, 4 points Ã  tout les seconds et 1 points Ã  tout les 3e
    public void ajoutPointInterimaires(int[] _score){
        int max=0, cpt=0, score;
        int[] points= new int[]{7,4,1}; // les points Ã  attribuer
        //On crée une liste dans laquelle on ajoute le couple id du joueur / nombre d'intérimaire
        ArrayList<int[]> l = new ArrayList<>();
        for(int i=0; i<joueurs.length;i++)
            l.add(new int[]{i, joueurs[i].ville.nbInterimaire});
        //on trie cette liste en fonction du nombre d'intérimaire dans l'ordre décroissant
        l.sort(Comparator.comparingInt((int[] o) -> o[1]).reversed());
        // On ajoute les points Ã  tout les joeurs ayant le plus d'intérimaires comme expliqué ci-dessus
        score=l.get(0)[1];
        while(max<3 && cpt<l.size()){
            if(l.get(cpt)[1]!= score){
                max++;
                score=l.get(cpt)[1];
            }
            if(max<3){
                _score[l.get(cpt)[0]]+=points[max];
            }
            cpt++;
        }
    }
    
    //Valider un plan pour un joueur
    public void validerPlan(Joueur _j, int _plan){
        //On decompte les tailles nécessaires pour valider le plan
        int[] tab=new int[]{0,0,0,0,0,0};
        for(int i=0; i<plans[_plan].nbLotissement;i++)
            tab[plans[_plan].tailleLotissements[i]-1]++;
        //On parcours les lotissements pour trouver ceux qui nécéssaire, dispo et complet afin de les rendre indisponible
        for (Lotissement lotissement : _j.ville.lotissements) {
            if(lotissement.taille<7 && lotissement.dispo && lotissement.complet && tab[lotissement.taille -1] > 0){
                tab[lotissement.taille-1]--;
                lotissement.dispo=false;
            }
        }
    }
    
    //piocher des nouveaux numéros (en début de chaque tour)
    public void nouveauxNumeros(){
        //Si les pioches ne sont pas vides, tirer les 3 prochaines cartes travaux
        if(numeros[0].getTaille()>1){
            actions[0].ajouter(numeros[0].piocher());
            actions[1].ajouter(numeros[1].piocher());
            actions[2].ajouter(numeros[2].piocher());
        }
        //Sinon remelanger et piocher les 3 cartes travaux
        else{
            //On ajoute toutes les cartes dans des pioches actions visibles dans une pioche temporaire
            Pioche piocheTmp = new Pioche();
            for(int i=0; i<3; i++)
                while(actions[i].getTaille()!=0){
                    piocheTmp.ajouter(actions[i].piocher());
            }
            //on melange tout
            piocheTmp.melanger();
            //on ajoute les dernieres cartes (coté numéro visible) aux pioches (coté action visible)
            actions[0].ajouter(numeros[0].piocher());
            actions[1].ajouter(numeros[1].piocher());
            actions[2].ajouter(numeros[2].piocher());
            //On ajoute les cartes de la pioche temporaire une par une sur les pioches (coté numéro visible)
            int tmp=0;
            while(piocheTmp.getTaille()!=0){
                numeros[tmp].ajouter(piocheTmp.piocher());
                tmp=(tmp+1)%3;
            }
        }
    }
    
    
    public void jouerUnNouveauTour() throws Exception{
        //Affichage joueur Ã  améliorer
        if(verbose){
            System.out.print(Couleur.VERT +"#########################" + Couleur.RESET);
            System.out.print(" Objectif 1: " + Couleur.RESET + plans[0]);
            if(this.plans[0].dejaAtteint>0){
                System.out.print(Couleur.ROUGE + " Atteint par:");
                for(int i=0; i<this.joueurs.length; i++)
                    if(joueurs[i].objectifs[0]>0)
                        System.out.print(" " + joueurs[i].nom);
            }
            System.out.println(Couleur.RESET);
            System.out.print(Couleur.VERT + "#   Le tour " + tour + " démarre\t#" + Couleur.RESET);
            System.out.print(" Objectif 2: " + Couleur.RESET + plans[1]);
            if(this.plans[1].dejaAtteint>0){
                System.out.print(Couleur.ROUGE + " Atteint par:");
                for(int i=0; i<this.joueurs.length; i++)
                    if(joueurs[i].objectifs[1]>0)
                        System.out.print(" " + joueurs[i].nom);
            }
            System.out.println(Couleur.RESET);
            System.out.print(Couleur.VERT + "#########################" + Couleur.RESET);
            System.out.print(" Objectif 3: " + Couleur.RESET + plans[2]);
            if(this.plans[2].dejaAtteint>0){
                System.out.print(Couleur.ROUGE + " Atteint par:");
                for(int i=0; i<this.joueurs.length; i++)
                    if(joueurs[i].objectifs[2]>0)
                        System.out.print(" " + joueurs[i].nom);
            }
            System.out.println(Couleur.RESET + "\n");
        }
        //On tire les nouveaux numéros
        nouveauxNumeros();
        
        Carte carteTmp; //objet Carte
        //Créer un clone du jeu et laisser uniquement la carte du haut de chaque pile pour éviter la triche
        Jeu[] copies=new Jeu[joueurs.length]; // Déclaration du tableau qui contiendra les clones du Jeu
        try{          
            for(int i=0; i<copies.length; i++){ //pour chaque joueur
                copies[i]=(Jeu)this.clone(); // on crée un clone
                for(int j=0; j<3; j++){ //pour chaque pioche
                    carteTmp=copies[i].numeros[j].piocher(); //on memorise la 1ere carte de la pioche des numéros
                    copies[i].numeros[j].vider(); //on vide le reste
                    copies[i].numeros[j].ajouter(carteTmp); //on rajoute la 1ere carte sur la pioche des numéros
                }     
            }
        }
        catch(CloneNotSupportedException e){
            System.err.println(e);
        }
              
        //déclarion de variables utiles
        int combi, nb, nbI, placement, rue, position, rueBis, positionBis, num; 
        boolean bis=false;
        ArrayList<Integer> p, b, numBis;
        
        //Pour chaque joueur faire un choix
        for(int i=0; i<joueurs.length; i++){
            //Affichage des 3 choix
            if(joueurs[i].verbose){
                System.out.println(Couleur.VIOLET + "C'est au tour de " + joueurs[i].nom + " (Propriétaire de " + joueurs[i].ville.nom + ")"+ Couleur.RESET);
                System.out.println(joueurs[i].ville);
                System.out.println(Couleur.FOND_ROUGE + Couleur.JAUNE_BOLD + "\tCHOIX POSSIBLES\t\t\t\tACTIONS DU TOUR SUIVANT\t\t" + Couleur.RESET);
                System.out.println("Choix" + Couleur.ROUGE_BOLD + " 0" + Couleur.RESET +": n°"+ ((Travaux)numeros[0].top()).getNumero() + " - " + ((Travaux)actions[0].top()).getSymbole() + " " + ((Travaux)actions[0].top()).getCouleur() + ((Travaux)actions[0].top()).getActionString() + Couleur.RESET + "\t\t" + ((Travaux)numeros[0].top()).getSymbole() + " " + ((Travaux)numeros[0].top()).getCouleur() + ((Travaux)numeros[0].top()).getActionString() + Couleur.RESET);
                System.out.println("Choix" + Couleur.ROUGE_BOLD + " 1" + Couleur.RESET +": n°"+ ((Travaux)numeros[1].top()).getNumero() + " - " + ((Travaux)actions[1].top()).getSymbole() + " " + ((Travaux)actions[1].top()).getCouleur() + ((Travaux)actions[1].top()).getActionString() + Couleur.RESET + "\t\t" + ((Travaux)numeros[1].top()).getSymbole() + " " + ((Travaux)numeros[1].top()).getCouleur() + ((Travaux)numeros[1].top()).getActionString() + Couleur.RESET);
                System.out.println("Choix" + Couleur.ROUGE_BOLD + " 2" + Couleur.RESET +": n°"+ ((Travaux)numeros[2].top()).getNumero() + " - " + ((Travaux)actions[2].top()).getSymbole() + " " + ((Travaux)actions[2].top()).getCouleur() + ((Travaux)actions[2].top()).getActionString() + Couleur.RESET + "\t\t" + ((Travaux)numeros[2].top()).getSymbole() + " " + ((Travaux)numeros[2].top()).getCouleur() + ((Travaux)numeros[2].top()).getActionString() + Couleur.RESET);      
                System.out.print("\nChoix de votre combinaison: ");
            }
            
            combi=joueurs[i].choixCombinaison(copies[i], i); //Récupération du choix fait par le joueur
            //combi=joueurs[i].choixCombinaison(this); 
            
            if(combi<0 || combi >2)
                throw new CombinaisonInvalideException(); //si le choix c'est pas compris entre 0 et 2 on balance une exception
            else{
                nbI=((Travaux)numeros[combi].top()).getNumero(); //on récupÃšre le numéro du choix
                //Si action==1 (Interimaire), demandez quel numéro mettre.
                if(((Travaux)actions[combi].top()).getAction() == 1){
                    if(joueurs[i].verbose){
                        System.out.println("Intérimaire choisi, quel numéro voulez vous placer?");
                    }
                    nb=joueurs[i].choixNumero(copies[i], i, nbI); //On récupÃšre le numéro du choix
                    if(nb < Math.max(0, nbI-2) || (nb>nbI+2)) // Check si le numéro choisi est valide
                        throw new NumeroInvalideException(); //Si ce n'est pas le cas, on balance une exception
                    joueurs[i].ville.nbInterimaire++; //On ajoute un intérimaire Ã  la ville du joueur
                }
                else
                    nb=nbI; //Si pas d'intérimaire le numéro choisi est tout simplement celui de la combinaison
                if(joueurs[i].verbose){ //Affichage du choix fait
                    System.out.println("Vous avez choisi de placer le " + Couleur.ROUGE + "numéro " + nb + Couleur.RESET + " avec l'action " +  ((Travaux)actions[combi].top()).getCouleur() + ((Travaux)actions[combi].top()).getActionString() + Couleur.RESET);
                }
                //Créer la liste des possibilités
                p=construirePossibilite(nb, joueurs[i]);
                if (p.isEmpty()){ //Si la liste est vide c'est que le numéro de peut pas Ãªtre placé
                    joueurs[i].refusPermis++; //on ajoute donc un refus de permis au joueur
                    if(joueurs[i].verbose)
                        System.out.println("Vous n'avez pas de possibilité de placement!!!"); // Affichage en fonction du mode verbose du joueur en cours
                    if(verbose){//Affichage pour tout le monde (info importante conditionnant la fin de partie)         
                        System.out.print(Couleur.ROUGE + joueurs[i].nom + " a obtenu son ");
                        if(joueurs[i].refusPermis==1)
                            System.out.println(Couleur.ROUGE + "1er refus de permis de construire." + Couleur.RESET);
                        else
                            System.out.println(Couleur.ROUGE + joueurs[i].refusPermis + "eme refus de permis de construire." + Couleur.RESET);
                    }
                }
                else{
                    placement=joueurs[i].choixEmplacement(copies[i], i, nb, p); //récupération du choix de placement fait par le joueur
                    if(placement<0 || placement>=p.size())
                        throw new PlacementInvalideException(); //si le choix de placement n'est pas compris dans les choix , on balance une exception
                    
                    rue=p.get(placement)/100; //on récupÃšre la rue
                    position=p.get(placement)%100; //on récupÃšre la position dans la rue
                    
                    joueurs[i].ville.rues[rue].maisons[position].setNumero(nb); //on fixe le numéro Ã  la maison correspondant au placement choisi
                    copies[i].joueurs[i].ville.rues[rue].maisons[position].setNumero(nb); // On le fixe également sur la copie du joueur en cours.
                    //Si action == 0 (Piscine) et que emplacement choisi en contient une -> La construire
                    if(((Travaux)actions[combi].top()).getAction() == 0 && joueurs[i].ville.rues[rue].maisons[position].aUnEmplacementPiscine()){
                        joueurs[i].ville.rues[rue].maisons[position].construirePiscine();
                        joueurs[i].ville.nbPiscine++;
                    }
                    //Si action == 2 (Bis) -> Ajouter un Bis uniquement si nécessaire
                    if(((Travaux)actions[combi].top()).getAction() == 2){
                        if(joueurs[i].ville.nbBis<9){
                            if(joueurs[i].verbose)
                                System.out.println(joueurs[i].ville);
                            numBis=constuireBis(joueurs[i]); // On construit les possibilités pour placer un numéro bis
                            placement=joueurs[i].choixBis(copies[i], i, numBis); //on récupère le choix fait par le joueur
                            if(numBis.get(placement)!=1000){//Si choix différent de 0 (pas de placement de numéro bis)
                                rueBis=Math.abs(numBis.get(placement))/100; //on récupère la rue correspondante
                                positionBis=Math.abs(numBis.get(placement))%100; // on récupère la position dans la rue
                                if(numBis.get(placement)<0) //Si négatif on copie le numéro de l'emplacement à gauche
                                    num=joueurs[i].ville.rues[rueBis].maisons[positionBis-1].numero;
                                else //Sinon celui de l'emplacement à droite
                                    num=joueurs[i].ville.rues[rueBis].maisons[positionBis+1].numero;
                                //On fixe le numéro bis!
                                joueurs[i].ville.rues[rueBis].maisons[positionBis].setNumero(num);
                                joueurs[i].ville.rues[rueBis].maisons[positionBis].bis=true;
                                joueurs[i].ville.nbBis++;
                            }
                        }
                        else if(joueurs[i].verbose){
                            System.out.println(Couleur.ROUGE + "Vous avez déjà atteint votre maximum de numero bis." + Couleur.RESET);
                        }
                    }          
                    //Si action == 3 (Paysagiste) -> Ajouter un Parc Ã  la Rue si pas plein
                    else if(((Travaux)actions[combi].top()).getAction() == 3){
                        joueurs[i].ville.construireParc(rue);
                    }
                    //Si action == 4 (Agence Immobilier) -> Demander taille des lotissements Ã  augmenter
                    else if(((Travaux)actions[combi].top()).getAction() == 4){
                        if(joueurs[i].verbose){
                            System.out.println("Vous devez valoriser les prix d'une taille de lotissement!");
                        }
                        nb = joueurs[i].valoriseLotissement(copies[i], i) - 1; // on récupÃšre la taille des lotissements Ã  valoriser
                        if(nb<0 || nb>5)
                            throw new ValorisationInvalideException(); //si le choix de valorisation n'est pas compris dans l'intervalle [1-6] , on balance une exception
                        joueurs[i].ville.avancementPrixLotissement[nb] = min(joueurs[i].ville.avancementPrixLotissement[nb] + 1, joueurs[i].ville.maxAvancement[nb]); // On valorise la taille correspondante sans dépasser le max
                    }
                    //Si action == 5 (GéomÃštre) -> Créer les Possibilités de BarriÃšres + choix
                    else if(((Travaux)actions[combi].top()).getAction() == 5){
                        if(joueurs[i].verbose){
                            System.out.println(joueurs[i].ville);
                        }
                        b=construireChoixPlacementBarriere(joueurs[i]); //On construit les possibilités de construction de barriÃšres
                        nb=joueurs[i].choixBarriere(this, i,  b);
                        if(nb<0 || nb >= b.size())
                            throw new BarriereInvalideException(); //si le choix de valorisation n'est pas compris dans l'intervalle [0-(nbPossibilité-1)] , on balance une exception
                        if(nb>0)
                            this.joueurs[i].ville.ajouterBarriere(b.get(nb)/100, b.get(nb)%100); // On construit la barriÃšre
                        
                    }
                    joueurs[i].ville.lotissements.forEach(lotissement -> lotissement.check()); // On check si il y a des nouveaux lotissements complets
                    
                    for(int pl=0; pl<plans.length; pl++){ //Pour chaque plan
                        int[] tab=new int[]{0,0,0,0,0,0};
                        for (Lotissement lotissement : joueurs[i].ville.lotissements) { // On compte les lotissements complets et dispo du joueurs
                            if(lotissement.dispo && lotissement.complet && lotissement.taille<7)
                                tab[lotissement.taille-1]++;
                        }
                        if(joueurs[i].objectifs[pl]==0 && plans[pl].check(tab)){ // on check si il peut valider le plan et qu'il ne l'a pas déjà validé
                            if(joueurs[i].verbose)
                                System.out.println("Voulez vous valider le plan suivant:\n" + plans[pl]);
                            if(joueurs[i].validePlan(copies[i], i, pl)){//Si le joueur veut valider le plan
                                if(plans[pl].dejaAtteint==2){ // on check si c'est la premiÃšre fois que ce plan a été validé
                                    joueurs[i].objectifs[pl]=plans[pl].pointsSuivants; //Sinon on marque le second score
                                }
                                else{
                                    joueurs[i].objectifs[pl]=plans[pl].pointsPremier; // Si oui on marque le premier score
                                    plans[pl].dejaAtteint=1; //On affecte le plan dans l'état correspondant Ã : validé ce tour-ci
                                }
                                validerPlan(joueurs[i], pl); // On valide le plan pour le joueur
                            }
                        }
                    }
                }
                if(joueurs[i].verbose){
                    System.out.println("---------------------------------------------------------------------------------------------------");
                    System.out.println(joueurs[i].ville);
                    System.out.println("Refus de Permis: " + joueurs[i].refusPermis);
                    System.out.println("---------------------------------------------------------------------------------------------------");
                }
               
            }
        }
        //On met tout les plans atteints ce tour comme étant déja atteint aprÃšs le tour de chaque joueur
        for (Plan pl: plans){
            if(pl.dejaAtteint==1)
                pl.dejaAtteint=2;
        }
    }
    
    //Construction des possibilités de placement des numéros bis
    private ArrayList<Integer> constuireBis(Joueur joueur){
        ArrayList<Integer> possibilite= new ArrayList();
        possibilite.add(1000);
        if(joueur.verbose){
            //System.out.println(joueur.ville);
            System.out.println("Possibilités de placement: position dans la rue (numero du choix à entrer au clavier)");
            System.out.print("\t" + Couleur.ROUGE + "Ne pas utiliser l'action Bis " + Couleur.RESET + "(" + (possibilite.size()-1) + ")\n");
        }
        for(int i=0; i<3; i++){ //on parcours les rues
            if(joueur.verbose){
                System.out.print("Rue " + (i+1) + ":");
            }
            for(int j=0; j<joueur.ville.rues[i].taille; j++){ //on parcours les maisons
                if(joueur.ville.rues[i].maisons[j].estVide()){ //Pour chaque maison vide
                    if(j>0 && !joueur.ville.rues[i].maisons[j-1].estVide() && !joueur.ville.barrieres[i][j]){ //On check le numero à gauche
                        possibilite.add(-1 * i * 100 - j);
                        if(joueur.verbose){
                            if(i==0)
                                System.out.print("\t" + Couleur.CYAN + "<" + (j+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                            else if(i==1)
                                System.out.print("\t" + Couleur.JAUNE + "<" + (j+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                            else
                                System.out.print("\t" + Couleur.VERT + "<" + (j+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        }
                    }
                    if(j<joueur.ville.rues[i].taille-1 && !joueur.ville.rues[i].maisons[j+1].estVide() && !joueur.ville.barrieres[i][j+1]){//On check le numero à droite
                        possibilite.add(i * 100 + j);
                        if(joueur.verbose){
                            if(i==0)
                                System.out.print("\t" + Couleur.CYAN + (j+1) + ">" + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                            else if(i==1)
                                System.out.print("\t" + Couleur.JAUNE + (j+1) + ">" + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                            else
                                System.out.print("\t" + Couleur.VERT + (j+1) + ">" + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        }
                    }
                }
            }
            if(joueur.verbose){
                System.out.println("");
            }
        }
        return possibilite;
    }
    
    //Construction des possibilités de placement des numéros
    private ArrayList<Integer> construirePossibilite(int numero, Joueur joueur){
        if(joueur.verbose){
            //System.out.println(joueur.ville);
            System.out.println("Possibilités de placement: position dans la rue (numero du choix à entrer au clavier)");
        }
        int min; // Variable utiles
        ArrayList<Integer> possibilite= new ArrayList(); //List des possibilités Ã  construire
        for(int i=0; i<3; i++){//Pour chaque rue
            min=joueur.ville.rues[i].taille-1; //on part de la fin
            if(joueur.verbose){
                System.out.print("Rue " + (i+1) + ":");
            }
            while(min>=0  && (joueur.ville.rues[i].maisons[min].numero==-1 || joueur.ville.rues[i].maisons[min].numero > numero))
                min--; // on décrement le min tant qu'on a pas trouvé un numéro <=
            if(min<0 || joueur.ville.rues[i].maisons[min].numero!=numero){

                min++;// On part de la case suivante
                while(min < joueur.ville.rues[i].taille && joueur.ville.rues[i].maisons[min].numero == -1){
                    possibilite.add((Integer)(min+ 100*i)); // on construit les possibilités tant qu'on a des cases vides
                    if(joueur.verbose){
                        if(i==0)
                            System.out.print("\t" + Couleur.CYAN + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        else if(i==1)
                            System.out.print("\t" + Couleur.JAUNE + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        else
                            System.out.print("\t" + Couleur.VERT + (min+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                    }
                    min++;
                }       
            }
            if(joueur.verbose){
                System.out.println("");
            }
        }
        return possibilite;
    }
    
    //Construction des possibilités pour les choix de barriÃšres
    private ArrayList<Integer> construireChoixPlacementBarriere(Joueur joueur) {
        //TODO CHECKER ET AJUSTER AVEC LES LOTISSEMENTS POUR LA CONSTRUCTION
        Lotissement l;
        int num=0;
        if(joueur.verbose){
            //System.out.println(joueur.ville);
            System.out.print("Possibilités de placement de barrieres: entre X-Y (numero du choix) ou 0 pour ne pas mettre de barrière");
        }
        //Les possibilités seront ajoutés dans une liste
        //Une possibilité sera représenté par un entier ayant la valeur suivante: 100*n°rue + position dans la rue
        //Création de la liste
        ArrayList<Integer> possibilite= new ArrayList();
        possibilite.add(0);
        //On parcours les lotissements
        for(int i=0; i<joueur.ville.lotissements.size(); i++){
            l=joueur.ville.lotissements.get(i);
            if(l.dispo){//Si le lotissement est dispo (pas encore utilisé pour la validation d'un objectif)
                if(joueur.verbose && l.rue.numero > num){
                    System.out.print("\nRue " + l.rue.numero + ":");
                    num++;
                }
                for(int j=l.debut+1; j<l.fin; j++){ //on ajoute aux possibilités les barriÃšres entre les 2 barriÃšres du lotissements   
                    if(joueur.ville.rues[l.rue.numero-1].maisons[j-1].numero <0 || joueur.ville.rues[l.rue.numero-1].maisons[j-1].numero != joueur.ville.rues[l.rue.numero-1].maisons[j].numero){
                        possibilite.add((Integer)((l.rue.numero-1) * 100 + j));
                        if(joueur.verbose){
                            if(l.rue.numero==1)
                                System.out.print("\t" + Couleur.CYAN + j + "-" + (j+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                            else if(l.rue.numero==2)
                                System.out.print("\t" + Couleur.JAUNE + j + "-" + (j+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                            else
                                System.out.print("\t" + Couleur.VERT + j + "-" + (j+1) + Couleur.RESET + "(" + (possibilite.size()-1) + ")");
                        }
                    }
                }
            }
        }
        if(joueur.verbose)
            System.out.println();
        return possibilite;
    }
    
    //Test de fin de partie
    public boolean dernierTour(){
        boolean res=false;
        //si un joueur a rempli toutes les cases de sa ville OU
        //si un joueur a eu son 3e refus de permis OU
        //si un joueur a validé les 3 objectifs
        //alors la partie s'arrÃªte
        for (Joueur joueur : joueurs) {
            if (tour> (33 + joueur.refusPermis - joueur.ville.nbBis) || joueur.refusPermis >= 3 || (joueur.objectifs[0]>0 && joueur.objectifs[1]>0 && joueur.objectifs[2]>0))
                res=true;           
        }
        return res;
    }
    
    //Calcul des scores d'un joueur (hors intérimaire)
    public int calculerScore(Joueur j){
        int score=0;
        //Ajout des points d'objectif
        score+=j.objectifs[0] + j.objectifs[1] + j.objectifs[2];
        //Ajout des points de piscine
        score+=pointPiscines[j.ville.nbPiscine];
        //Ajout des points pour les parcs
        score+=pointParcs[0][j.ville.nbParcs[0]] + pointParcs[1][j.ville.nbParcs[1]] + pointParcs[2][j.ville.nbParcs[2]];
        //Ajout des points pour les Lotissements
        for (Lotissement l: j.ville.lotissements){
            if(l.complet && l.taille<=6)
                score+=valeurLotissements[l.taille-1][j.ville.avancementPrixLotissement[l.taille-1]];
        }
        //Retrait des points pour numéros bis
        score-=pointBis[j.ville.nbBis];
        //Retrait des points pour refus de permis
        score-=pointRefusDePermis[j.refusPermis];
        return score;
    }
    
    //Initialisation des cartes travaux
    private void initCartesTravaux(){ 
        Pioche p = new Pioche(); // On initialise une pioche
        //On créé et ajoute toutes les cartes Travaux
        //Cartes 1
        p.ajouter(new Travaux(1, 3));
        p.ajouter(new Travaux(1, 4));
        p.ajouter(new Travaux(1, 5));
        //Cartes 2
        p.ajouter(new Travaux(2, 3));
        p.ajouter(new Travaux(2, 4));
        p.ajouter(new Travaux(2, 5));
        //Cartes 3
        p.ajouter(new Travaux(3, 0));
        p.ajouter(new Travaux(3, 1));
        p.ajouter(new Travaux(3, 2));
        p.ajouter(new Travaux(3, 5));
        //Cartes 4
        p.ajouter(new Travaux(4, 0));
        p.ajouter(new Travaux(4, 1));
        p.ajouter(new Travaux(4, 2));
        p.ajouter(new Travaux(4, 3));
        p.ajouter(new Travaux(4, 4));      
        //Cartes 5
        p.ajouter(new Travaux(5, 3));
        p.ajouter(new Travaux(5, 3));
        p.ajouter(new Travaux(5, 4));
        p.ajouter(new Travaux(5, 4));
        p.ajouter(new Travaux(5, 5));
        p.ajouter(new Travaux(5, 5));
        //Cartes 6
        p.ajouter(new Travaux(6, 0));
        p.ajouter(new Travaux(6, 1));
        p.ajouter(new Travaux(6, 2));
        p.ajouter(new Travaux(6, 3));
        p.ajouter(new Travaux(6, 4));
        p.ajouter(new Travaux(6, 5));
        p.ajouter(new Travaux(6, 5));
        //Cartes 7
        p.ajouter(new Travaux(7, 0));
        p.ajouter(new Travaux(7, 1));
        p.ajouter(new Travaux(7, 2));
        p.ajouter(new Travaux(7, 3));
        p.ajouter(new Travaux(7, 3));
        p.ajouter(new Travaux(7, 4));
        p.ajouter(new Travaux(7, 4));
        p.ajouter(new Travaux(7, 5));
        //Cartes 8
        p.ajouter(new Travaux(8, 0));
        p.ajouter(new Travaux(8, 1));
        p.ajouter(new Travaux(8, 2));
        p.ajouter(new Travaux(8, 3));
        p.ajouter(new Travaux(8, 3));
        p.ajouter(new Travaux(8, 4));
        p.ajouter(new Travaux(8, 4));
        p.ajouter(new Travaux(8, 5));
        p.ajouter(new Travaux(8, 5));
        //Cartes 15
        p.ajouter(new Travaux(15, 3));
        p.ajouter(new Travaux(15, 4));
        p.ajouter(new Travaux(15, 5));
        //Cartes 14
        p.ajouter(new Travaux(14, 3));
        p.ajouter(new Travaux(14, 4));
        p.ajouter(new Travaux(14, 5));
        //Cartes 13
        p.ajouter(new Travaux(13, 0));
        p.ajouter(new Travaux(13, 1));
        p.ajouter(new Travaux(13, 2));
        p.ajouter(new Travaux(13, 5));
        //Cartes 12
        p.ajouter(new Travaux(12, 0));
        p.ajouter(new Travaux(12, 1));
        p.ajouter(new Travaux(12, 2));
        p.ajouter(new Travaux(12, 3));
        p.ajouter(new Travaux(12, 4));      
        //Cartes 11
        p.ajouter(new Travaux(11, 3));
        p.ajouter(new Travaux(11, 3));
        p.ajouter(new Travaux(11, 4));
        p.ajouter(new Travaux(11, 4));
        p.ajouter(new Travaux(11, 5));
        p.ajouter(new Travaux(11, 5));
        //Cartes 10
        p.ajouter(new Travaux(10, 0));
        p.ajouter(new Travaux(10, 1));
        p.ajouter(new Travaux(10, 2));
        p.ajouter(new Travaux(10, 3));
        p.ajouter(new Travaux(10, 4));
        p.ajouter(new Travaux(10, 5));
        p.ajouter(new Travaux(10, 5));
        //Cartes 9
        p.ajouter(new Travaux(9, 0));
        p.ajouter(new Travaux(9, 1));
        p.ajouter(new Travaux(9, 2));
        p.ajouter(new Travaux(9, 3));
        p.ajouter(new Travaux(9, 3));
        p.ajouter(new Travaux(9, 4));
        p.ajouter(new Travaux(9, 4));
        p.ajouter(new Travaux(9, 5));
        
        //On mélange tout Ã§a
        p.melanger();
        //On créé les 3 pioches (coté numéros visible) utile pour le jeu
        numeros = new Pioche[3];
        numeros[0]= new Pioche();
        numeros[1]= new Pioche();
        numeros[2]= new Pioche();
        //On réparties les cartes de notre gros sur les 3 pioches une Ã  une
        int tmp=0;
        while(p.getTaille()!=0){
            numeros[tmp].ajouter(p.piocher());
            tmp=(tmp+1)%3;
        }
    }
    
    // Créations des pioche de plan et tirage alétoire des plans pour la partie
    private void initPlans(){
        Pioche[] piochePlans = new Pioche[3]; //Initialisation du tableau de pioche
        piochePlans[0]=new Pioche(); //Initialisation de la pioche d'objectif 1
        piochePlans[1]=new Pioche(); //Initialisation de la pioche d'objectif 2
        piochePlans[2]=new Pioche(); //Initialisation de la pioche d'objectif 3
        //Plan n°1
        piochePlans[0].ajouter(new Plan(8, 4, new int[]{5, 5}, 2));
        piochePlans[0].ajouter(new Plan(8, 4, new int[]{2, 2, 2, 2}, 4));
        piochePlans[0].ajouter(new Plan(6, 3, new int[]{4, 4}, 2));
        piochePlans[0].ajouter(new Plan(8, 4, new int[]{1, 1, 1, 1, 1, 1}, 6));
        piochePlans[0].ajouter(new Plan(10, 6, new int[]{6, 6}, 2));
        piochePlans[0].ajouter(new Plan(8, 4, new int[]{3, 3, 3}, 3));
        //Plan n°2
        piochePlans[1].ajouter(new Plan(11, 6, new int[]{1, 1, 1, 6}, 4));
        piochePlans[1].ajouter(new Plan(10, 6, new int[]{2, 2, 5}, 3));
        piochePlans[1].ajouter(new Plan(9, 5, new int[]{4, 5}, 2));
        piochePlans[1].ajouter(new Plan(8, 4, new int[]{3, 6}, 2));
        piochePlans[1].ajouter(new Plan(12, 7, new int[]{3, 3, 4}, 3));
        piochePlans[1].ajouter(new Plan(9, 5, new int[]{1, 1, 1, 4}, 4));
        //Plan n°3
        piochePlans[2].ajouter(new Plan(7, 3, new int[]{2, 5}, 2));
        piochePlans[2].ajouter(new Plan(13, 7, new int[]{2, 3, 5}, 3));
        piochePlans[2].ajouter(new Plan(13, 7, new int[]{1, 4, 5}, 3));
        piochePlans[2].ajouter(new Plan(7, 3, new int[]{3, 4}, 2));
        piochePlans[2].ajouter(new Plan(11, 6, new int[]{1, 2, 2, 3}, 4));
        piochePlans[2].ajouter(new Plan(12, 7, new int[]{1, 2, 6}, 3));
        
        //On crée le tableau des plans et on tire au hasards les 3 plans pour la partie
        plans= new Plan[3];
        piochePlans[0].melanger();
        plans[0]=(Plan)piochePlans[0].piocher();
        piochePlans[1].melanger();
        plans[1]=(Plan)piochePlans[1].piocher();
        piochePlans[2].melanger();
        plans[2]=(Plan)piochePlans[2].piocher();
    }
    
    //Initialisation des pioches (coté action visible)
    private void initActions(){
        actions= new Pioche[3];
        actions[0]= new Pioche();
        actions[1]= new Pioche();
        actions[2]= new Pioche();
    }


    
}
