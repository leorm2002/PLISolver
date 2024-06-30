package it.naddeil.ro.common;


public class ProblemTransformer {



    static FunzioneObbiettivo getFunzioneObbiettivoStd(FunzioneObbiettivo f){
        FunzioneObbiettivo out = f.copy();

        if(Tipo.MAX.equals(f.getTipo())){
            out.setC(f.getC().stream().map(x -> -x).toList());
            out.setTipo(Tipo.MIN);
        }
        return out;
    }

    static Vincolo trasformaVincolo(Vincolo v){
        Vincolo out = v.copy();
        if(Verso.GE.equals(v.getVerso())){
            out.setVerso(Verso.LE);
            out.setVincolo(v.getVincolo().stream().map(x -> -x).toList());
        }
        return out;
    }

    /**
     * Dato un problema generico lo restituisce in forma canonica ovvero
     *      min c^T x
     *      s.t. Ax <= b
     */
    public static Problema portaInFormaStandard(Problema problem) {
        Problema out = problem.copy();
        // Gestione funzione obiettivo
        out.setFunzioneObbiettivo(getFunzioneObbiettivoStd(problem.getFunzioneObbiettivo()));
        out.setVincoli(problem.getVincoli().stream().map(ProblemTransformer::trasformaVincolo).toList());

        return out;
    }
}
