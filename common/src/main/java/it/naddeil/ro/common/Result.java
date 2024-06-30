package it.naddeil.ro.common;

import org.ejml.simple.SimpleMatrix;

public interface Result {
    SimpleMatrix getSoluzione();
    SimpleMatrix getTableauOttimo();
}