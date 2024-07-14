package it.naddeil.ro.common;

import java.util.ArrayList;
import java.util.List;


import it.naddeil.ro.common.api.FunzioneObbiettivo;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.api.Tipo;
import it.naddeil.ro.common.api.Verso;
import it.naddeil.ro.common.api.Vincolo;

public class ProblemTransformer {
    static List<Vincolo> aggiungiSlack(PublicProblem problem, int numeroVariabiliSlackDaAggiungere){

        int i = 0;
        int j= 0;
        List<Vincolo> vincoli = new ArrayList<>();
        while (j < problem.getVincoli().size()) {
            Vincolo vincolo = problem.getVincoli().get(i);
            boolean addSlack = !vincolo.getVerso().equals(Verso.E);

            List<Double> vettoreDaAggiungere = addSlack ? getSlackVector(numeroVariabiliSlackDaAggiungere, i) : getSlackVector(numeroVariabiliSlackDaAggiungere, -1);
            vincoli.add(trasformaVincolo(vincolo, vettoreDaAggiungere));
            i = addSlack ? i +1  : i;
            j += 1;
        }

        return vincoli;
    }

    static int getNumeroVariabiliDaAggiungere(PublicProblem problem){
        return (int)problem.getVincoli().stream().map(Vincolo::getVerso).filter(c -> !Verso.E.equals(c)).count();
    }

    static List<Double> getExtendedC(List<Double> cin, int n){
        List<Double> c = new ArrayList<>(cin);
        for(int i = 0; i < n; i++){
            c.add(0.0);
        }
        return c;
    }
    static FunzioneObbiettivo getFunzioneObbiettivoStd(FunzioneObbiettivo f){
        FunzioneObbiettivo out = new FunzioneObbiettivo();  

        if(Tipo.MAX.equals(f.getTipo())){
            out.setC(f.getC().stream().map(x -> -x).toList());
            out.setTipo(Tipo.MIN);
        }else{
            out.setC(f.getC());
            out.setTipo(Tipo.MIN);
        }
        return out;
    }

    static Vincolo trasformaVincoloPerDuale(Vincolo v){
        Vincolo out = new Vincolo(null, null);
        if(Verso.GE.equals(v.getVerso())){
            out.setVerso(Verso.LE);
            out.setVincolo(v.getVincolo().stream().map(x -> -x).toList());
        }else{
            out.setVerso(v.getVerso());
            out.setVincolo(v.getVincolo());
        }
        return out;
    }

    static Vincolo trasformaVincolo(Vincolo v, List<Double> slackVector){
        Vincolo out = trasformaVincoloPerDuale(v);
        out.setVerso(Verso.E);
        List<Double> vincolo = new ArrayList<>(out.getVincolo());
        Double last = vincolo.remove(vincolo.size() - 1);
        vincolo.addAll(slackVector);
        vincolo.add(last);
        out.setVincolo(vincolo);
        return out;
    }

    static List<Double> getSlackVector(int size, int slakPos){
        List<Double> out = new ArrayList<>(size);
        for(int i = 0; i < size; i++){
            out.add(i == slakPos ? 1.0 : 0.0);
        }
        return out;
    }


    /**
     * Dato un problema generico resituisce preparato per il duale
     *      min c^T x
     *      s.t. Ax >= b
     */
    public static PublicProblem preparaPerDuale(PublicProblem problem) {
        int numeroVariabiliSlackDaAggiungere = getNumeroVariabiliDaAggiungere(problem);
        PublicProblem out = new PublicProblem();
        // Gestione funzione obiettivo
        var funzioneObbiettivo = getFunzioneObbiettivoStd(problem.getFunzioneObbiettivo());
        funzioneObbiettivo.setC(getExtendedC(funzioneObbiettivo.getC(), numeroVariabiliSlackDaAggiungere));

        out.setVincoli(problem.getVincoli().stream().map(ProblemTransformer::trasformaVincoloPerDuale).toList());
        aggiungiSlack(out, numeroVariabiliSlackDaAggiungere);
        out.setFunzioneObbiettivo(funzioneObbiettivo);

        // Todo aggiungere slack
        out.setFunzioneObbiettivo(funzioneObbiettivo);

        return out;
    }



    /**
     * Dato un problema generico lo restituisce in forma canonica ovvero
     */
    public static PublicProblem portaInFormaCanonica(PublicProblem problem) {
        PublicProblem out = new PublicProblem();
        // Gestione funzione obiettivo
        // Il numero di disequazioni è anche il numeoro di variabili di slack
        int numeroVariabiliSlackDaAggiungere = getNumeroVariabiliDaAggiungere(problem);
        FunzioneObbiettivo funzioneObbiettivoStd = getFunzioneObbiettivoStd(problem.getFunzioneObbiettivo());
        funzioneObbiettivoStd.setC(getExtendedC(funzioneObbiettivoStd.getC(), numeroVariabiliSlackDaAggiungere));
        out.setFunzioneObbiettivo(funzioneObbiettivoStd);
        out.setVincoli(aggiungiSlack(problem,numeroVariabiliSlackDaAggiungere));
        return out;
    }
}