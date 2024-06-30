package it.naddeil.ro.common;

import it.naddeil.ro.common.pub.PublicProblem;

public class ProblemTransformer {
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

    static Vincolo trasformaVincolo(Vincolo v){
        Vincolo out = new Vincolo(null, null);
        //if(Verso.LE.equals(v.getVerso())){
        //    out.setVerso(Verso.GE);
        //    out.setVincolo(v.getVincolo().stream().map(x -> -x).toList());
        //else{
            out.setVerso(v.getVerso());
            out.setVincolo(v.getVincolo());
        //}
        return out;
    }

    /**
     * Dato un problema generico lo restituisce in forma canonica ovvero
     *      min c^T x
     *      s.t. Ax <= b
     */
    public static PublicProblem portaInFormaStandard(PublicProblem problem) {
        PublicProblem out = new PublicProblem();
        // Gestione funzione obiettivo
        out.setFunzioneObbiettivo(getFunzioneObbiettivoStd(problem.getFunzioneObbiettivo()));
        out.setVincoli(problem.getVincoli().stream().map(ProblemTransformer::trasformaVincolo).toList());

        return out;
    }
}
