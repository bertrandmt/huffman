/*
 * HuffmanEncoder.java
 *
 * Created on 1 mars 2002, 23:59
 */

package org.bmt-online.huffman;

import java.util.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 *
 * @author  bmt
 * @version
 */
public class HuffmanEncoder
{
    FileInputStream in;
    FileOutputStream out;
    
    /** Creates new HuffmanEncoder */
    public HuffmanEncoder()
    {
    }
    
    double pi[], pji[][];
    List nodes;
    
    private static final int BUF_SIZE = 65536;
    private static final int SYM_COUNT = Byte.MAX_VALUE - Byte.MIN_VALUE + 1;
    
    private void computeSymbolProbs()
    throws
    IOException
    {
        FileChannel f = in.getChannel();
        ByteBuffer dst = ByteBuffer.allocateDirect(BUF_SIZE);
        long ci[] = new long[SYM_COUNT], cji[][] = new long[SYM_COUNT][SYM_COUNT];
        
        f.position(0);
        while(true)
        {
            dst.clear();
            int nread = f.read(dst);
            if ((nread == 0) || (nread == -1))
            {
                break;
            }
            
            dst.flip();
            int p_sym = 0;
            while(dst.hasRemaining())
            {
                int c_sym = (int)dst.get() - Byte.MIN_VALUE;
                ci[c_sym] += 1;
                cji[p_sym][c_sym] += 1;
                p_sym = c_sym;
            }
        }
        
        this.pi = normalize(ci);
        this.pji = new double[SYM_COUNT][];
        for (int i = 0; i < cji.length; i += 1)
        {
            this.pji[i] = normalize(cji[i]);
        }
        
        nodes = new ArrayList(pi.length);
        for (int i = 0; i < pi.length; i += 1)
        {
            if (pi[i] != 0)
            {
                Node symbol = new Leaf((byte)(i + Byte.MIN_VALUE), pi[i]);
                nodes.add(symbol);
            }
        }
        Collections.sort(nodes);
    }
    
    static double[] normalize(long[] u)
    {
        if (u == null)
        {
            throw new NullPointerException();
        }
        double[] v = new double[u.length];
        double sum = 0;
        for (int i = 0; i < SYM_COUNT; i += 1)
        {
            sum += u[i];
        }
        if (sum != 0)
        {
            for (int i = 0; i < SYM_COUNT; i += 1)
            {
                v[i] = u[i] / sum;
            }
        }
        return v;
    }
    
    static final double LOG_2 = Math.log(2);
    
    static double compute_ml_entropy(double []pi)
    {
        if (pi == null)
        {
            throw new NullPointerException();
        }
        double entropy = 0;
        for (int i = 0; i < pi.length; i += 1)
        {
            double p = pi[i];
            if (p != 0)
            {
                entropy -= p * Math.log(p) / LOG_2;
            }
        }
        return entropy;
    }
        
    static double compute_mf_entropy(double []pi, double [][]pji)
    {
        if ((pi == null) || (pji == null))
        {
            throw new NullPointerException();
        }
        double entropy = 0;
        for (int i = 0; i < pi.length; i += 1)
        {
            double p = pi[i];
            if (p != 0)
            {
                double interdependence = 0;
                double []pj = pji[i];
                for (int j = 0; j < pj.length; j += 1)
                {
                    double p2 = pj[j];
                    if (p2 != 0)
                    {
                        interdependence -= p2 * Math.log(p2) / LOG_2;
                    }
                }
                entropy += p * interdependence;
            }
        }
        return entropy;
    }
    
    Node hTree;

    private void buildHTree()
    {
        if ((nodes == null) || (nodes.size() == 0))
        {
            throw new RuntimeException("erm");
        }
        
        
        while (nodes.size() != 1)
        {
            Node n1 = (Node)nodes.remove(0);
            Node n2 = (Node)nodes.remove(0);
            if (n1.getWeight() < n2.getWeight())
            {
                hTree = new BasicNode(n1, n2);
            }
            else
            {
                hTree = new BasicNode(n2, n1);
            }
            
            nodes.add(hTree);
            Collections.sort(nodes);
        }
    }
    
    Code[] symbolArray;
    
    private void buildSymbolArray()
    {
        symbolArray = new Code[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
        fillArray(symbolArray, hTree);
        for (int i = 0; i < symbolArray.length; i += 1)
        {
            //System.out.println("i=" + i + ", s=" + symbolArray[i]);
        }
    }
    
    private void fillArray(Code[] symbolArray, Node n)
    {
        if (n.isLeaf())
        {
            symbolArray[n.getSymbol() - Byte.MIN_VALUE] = n.getCode();
        }
        else
        {
            fillArray(symbolArray, n.getLeftChild());
            fillArray(symbolArray, n.getRightChild());
        }
    }
    
    private static final int SCALE = 3;
    
    public static void main(String[] args)
    throws
    Exception
    {
        long start = System.currentTimeMillis();
        
        HuffmanEncoder h = new HuffmanEncoder();
        h.in = new FileInputStream(args[0]);
        h.computeSymbolProbs();
        h.in.close();
        h.buildHTree();
        h.hTree.setCode(new Code());
        //System.out.println(h.hTree.toCodeString());
        h.buildSymbolArray();
        
        long stop = System.currentTimeMillis();
        
        System.out.println("elapsed time = " + (stop - start) + " ms\n");
        System.out.println("entropy      = " + Formater.toString(compute_ml_entropy(h.pi), SCALE) + " bits (memoryless-ness assumed)");
        System.out.println("             = " + Formater.toString(compute_mf_entropy(h.pi, h.pji), SCALE) + " bits (general case)");
        System.out.println("code length  = " + Formater.toString(h.hTree.getCodeLength(), SCALE) + " bits");
        System.out.println("efficiency   = " + Formater.toString(compute_mf_entropy(h.pi, h.pji) / h.hTree.getCodeLength() * 100, SCALE) + " %");
        System.out.println("ratio        = " + Formater.toString((1 - (h.hTree.getCodeLength() / 8)) * 100, SCALE) + " %");
    }
}
