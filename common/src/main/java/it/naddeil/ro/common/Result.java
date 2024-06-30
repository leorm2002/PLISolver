package it.naddeil.ro.common;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

public interface Result {
    SimpleMatrix getSoluzione();
    SimpleMatrix getTableauOttimo();
    List<Integer> getBasis();
    
}