/*
 * Node.java
 *
 * Created on 1 mars 2002, 23:25
 */

package org.bmt-online.huffman;

/**
 *
 * @author  bmt
 * @version
 */
public interface Node
extends 
Comparable
{
    
    public boolean isLeaf();
    
    public byte getSymbol();
    
    public double getWeight();
    
    public Node getLeftChild();
    
    public Node getRightChild();
    
    public void setCode(Code code);
    public Code getCode();
    
    public String toCodeString();
    
    public double getCodeLength();
    public double getEntropy();
}

