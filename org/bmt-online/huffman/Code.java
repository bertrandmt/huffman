/*
 * Code.java
 *
 * Created on 2 mars 2002, 01:13
 */

package org.bmt-online.huffman;

/**
 *
 * @author  bmt
 * @version
 */
public class Code
{
    byte[] data;
    int length;
    
    /** Creates new Code */
    public Code()
    {
        data = new byte[10];
        length = 0;
    }
    
    public Code(Code code)
    {
        this.data = new byte[code.data.length];
        System.arraycopy(code.data, 0, this.data, 0, this.data.length);
        this.length = code.length;
    }
    
    private static final int BYTE_BITS = 7;
    
    public Code append(boolean bit)
    {
        if (length / BYTE_BITS > data.length)
        {
            throw new RuntimeException("Code size overflow.");
        }
        Code c = new Code(this);
        
        int byteSelector = length / BYTE_BITS;
        int bitSelector = length % BYTE_BITS;
        
        if (bit)
        {
            c.data[byteSelector] |= SET_MASK[bitSelector];
        }
        else
        {
            c.data[byteSelector] &= UNSET_MASK[bitSelector];
        }

        c.length += 1;
        
        return c;
    }
    
    private static final byte SET_MASK[] = 
    {
        0x40,
        0x20,
        0x10,
        0x08,
        0x04,
        0x02,
        0x01
    };
    
    private static final byte UNSET_MASK[] =
    {
        0x3f,
        0x5f,
        0x7f,
        0x77,
        0x7b,
        0x7d,
        0x7e
    };
    
    public int getBit(int offset)
    {
        if ((offset < 0) || (offset > length))
        {
            throw new IllegalArgumentException("Invalid offset");
        }
        return (data[offset / BYTE_BITS] & SET_MASK[offset % BYTE_BITS]) >> (BYTE_BITS - 1 - (offset % BYTE_BITS));
    }
    
    public String toString()
    {
        String s = "";
        for (int i = 0; i < this.length; i += 1)
        {
            s += getBit(i);
        }
        return s;
    }
}
