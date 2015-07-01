/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package myMath;

import java.util.Random;

/**
 *
 * @author mahdi
 */
public class RandVar {
    //gamma is rate
    public static double exponential (double gamma)
    {
        return (1.0 / gamma) * (-Math.log(1.0 - Math.random()));
    }
}
