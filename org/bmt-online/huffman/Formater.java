/*
 * Formater.java
 *
 * Created on 20 janvier 2002, 13:54
 */

package org.bmt-online.huffman;

import java.math.BigDecimal;

/**
 * Provides a few utility classes to format numbers into strings.
 *
 * @author  bmt
 * @version 1.0
 */
public final class Formater
{
    /**
     * Creates new Formater. This class is not instanciable.
     */
    private Formater()
    {
    }
    
    /**
     * Returns a String representing the double <code>d</code>, in so-called
     * scientific notation, that is a value between 0 and 10, with
     * <code>scale</code> digits on the right of the dot, and the adequate
     * exponent.
     */
    public static String toScientificNotation(double d, int scale)
    {
        if (Double.isNaN(d))
        {
            return "NaN";
        }
        else if (Double.isInfinite(d))
        {
            return "Infinity";
        }
        else
        {
            double abs_d = Math.abs(d);
            if ((d == 0) || ((abs_d > LO_THRESHOLD) && (abs_d < HI_THRESHOLD)))
            {
                return new BigDecimal(d).setScale(3, BigDecimal.ROUND_HALF_EVEN).toString();
            }
            else
            {
                String sgn;
                if (d < 0)
                {
                    sgn = "-";
                    d = abs_d;
                }
                else
                {
                    sgn = "";
                }
                
                int exp = (int) Math.floor(Math.log(d) / LN10);
                d *= Math.pow(10, -exp);
                
                String mantissa = new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_EVEN).toString();
                
                return sgn + mantissa + "E" + exp;
            }
        }
    }
    
    public static final double LN10 = Math.log(10);
    private static final double LO_THRESHOLD = Math.pow(10, -1);
    private static final double HI_THRESHOLD = Math.pow(10, 3);
    
    /**
     * convert state value to a string to truncate the value to
     * <CODE>scale</CODE> significant digits.
     */
    public static String toString(double value, int scale)
    {
        if (value > Math.pow(10., -scale))
        {
            String newValue = Double.toString(value);
            int length = newValue.length();
            int point = newValue.indexOf(".");
            int end = point + scale + 1;
            if (length < end)
            {
                int difference = end - length;
                for(int i = 0;i < difference; i += 1)
                {
                    newValue = newValue + "0";
                }
            }
            newValue = newValue.substring(0 , end);
            return newValue;
        }
        else
        {
            return new String("0.000");
        }
    }
    
    public static String toString(float[] a)
    {
        String s = "[";
        for (int i = 0; i < a.length - 1; i += 1)
        {
            s += a[i] + ", ";
        }
        s += a[a.length - 1] + "]";
        return s;
    }
}
