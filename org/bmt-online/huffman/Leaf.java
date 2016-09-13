/*
 * Leaf.java
 *
 * Created on 1 mars 2002, 23:17
 */

package org.bmt-online.huffman;

/**
 *
 * @author  bmt
 * @version
 */
public class Leaf
implements
Node
{
    byte symbol;
    double weight;
    Code code;
    
    /** Creates new Leaf */
    private Leaf()
    {
    }
    
    public Leaf(byte symbol, double weight)
    {
        if (weight < 0)
        {
            throw new IllegalArgumentException("Negative weight not supported in leaf.");
        }
        this.symbol = symbol;
        this.weight = weight;
        this.code = null;
    }
    
    public Node getLeftChild()
    {
        throw new UnsupportedOperationException();
    }
    
    public Node getRightChild()
    {
        throw new UnsupportedOperationException();
    }
    
    public double getWeight()
    {
        return this.weight;
    }
    
    public byte getSymbol()
    {
        return this.symbol;
    }
    
    public boolean isLeaf()
    {
        return true;
    }
    
    public void setCode(Code code)
    {
        if (code == null)
        {
            throw new IllegalArgumentException("Null code not supported in leaf.");
        }
        this.code = code;
    }
    
    public Code getCode()
    {
        return this.code;
    }
    
    public int compareTo(Object o)
    {
        Node n = (Node)o;
        double diff = this.weight - n.getWeight();
        if (diff < 0)
        {
            return -1;
        }
        else if (diff > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    
    public String toString()
    {
        return "Leaf[s=" + this.symbol + ", w=" + this.weight + "]";
    }
    
    public String toCodeString()
    {
        return "[s=" + this.symbol + ", w=" + this.weight + ", c=" + this.code + "]\n";
    }
    
    public double getEntropy()
    {
        return - this.weight * Math.log(this.weight) / Math.log(2);
    }
    
    public double getCodeLength()
    {
        return this.code.length * this.weight;
    }
    
}
