/*
 * Node.java
 *
 * Created on 1 mars 2002, 23:18
 */

package org.bmt-online.huffman;

/**
 *
 * @author  bmt
 * @version
 */
public class BasicNode
implements
Node
{
    double weight;
    Node leftChild, rightChild;
    Code code;
    
    /** Creates new Node */
    private BasicNode()
    {
    }
    
    public BasicNode(Node leftChild, Node rightChild)
    {
        if ((leftChild == null) || (rightChild == null))
        {
            throw new IllegalArgumentException("Null child not supported in basic node.");
        }
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.weight = leftChild.getWeight() + rightChild.getWeight();
        this.code = null;
    }
    
    public Node getLeftChild()
    {
        return this.leftChild;
    }
    
    public Node getRightChild()
    {
        return this.rightChild;
    }
    
    public double getWeight()
    {
        return this.weight;
    }
    
    public byte getSymbol()
    {
        throw new UnsupportedOperationException();
    }
    
    public boolean isLeaf()
    {
        return false;
    }
    
    public Code getCode()
    {
        return this.code;
    }
    
    public void setCode(Code code)
    {
        if (code == null)
        {
            throw new IllegalArgumentException("Null code not supported in basic node.");
        }
        this.code = code;
        this.leftChild.setCode(code.append(false));
        this.rightChild.setCode(code.append(true));
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
        return "BasicNode[w=" + this.weight + "]{" + this.leftChild + "}{" + this.rightChild + "}";
    }
    
    public String toCodeString()
    {
        return this.leftChild.toCodeString() + this.rightChild.toCodeString();
    }
    
    public double getEntropy()
    {
        return this.leftChild.getEntropy() + this.rightChild.getEntropy();
    }
    
    public double getCodeLength()
    {
        return this.leftChild.getCodeLength() + this.rightChild.getCodeLength();
    }
    
}
