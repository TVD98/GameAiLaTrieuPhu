/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author midor
 */
// đây là class chứa các method hỗ trợ trong chương trình nhé.
public class Utility {

    public static void shuffleArray(int[] ar) {
        
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
