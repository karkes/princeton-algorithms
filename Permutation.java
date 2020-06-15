/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 24/04/2020 DD/MM/YYYY
 *  Description: Client code
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        // StdOut.println("Hello there, from client Permutation!");
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int c = 0;
        int run = 0;
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            if (c < k) {
                // StdOut.println("Input in RQ: " + str);
                rq.enqueue(str);
                c++;
                run++;
                continue;
            }
            // StdOut.println("Input : " + str);
            int randIdx = StdRandom.uniform(0, run + 1);
            if (randIdx < k) {
                rq.dequeue();
                rq.enqueue(str);
            }
            run++;
        }
        // Random Algo : similar to Reservoir sampling
        // StdOut.println("Printing " + k + " random elements : ");
        for (int i = 1; i <= k; i++) {
            StdOut.println(rq.dequeue());
        }

    }
}
