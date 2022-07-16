package programming.algos;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Java Source GreedyTest.java created on Jul 23, 2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */

public class GreedyTest {


    @Test
    public void testFindmaximumMeetings() {

        int s[] = {1, 3, 0, 5, 8, 5};
        int f[] = {2, 4, 6, 7, 9, 9};

        System.out.printf("[%s,%s]\n", s[0], f[0]);

        for (int i = 1, j = 0; i < s.length; i++) {
            if (s[i] >= f[j]) {
                j = i;
                System.out.printf("[%s,%s]\n", s[i], f[j]);
            }
        }
    }


    /**
     * int[] start_times = {10, 11, 13, 12, 12, 13};
     * <p>
     * int[] end_times = {12, 13, 14, 14, 13, 14};
     * <p>
     * Maximum possible shows: Movie 1: 10-12, Movie 5: 12-13 ,Movie 3 or Movie
     * 6: 13-14
     */
    @Test
    public void testGetMaximumMoviesCount() {

        final int[] start = {10, 11, 13, 12, 12, 13};
        final int[] end = {12, 13, 14, 14, 13, 14};

        final Set<Integer> count = new HashSet<>();
        count.add(0);
        int previous = 0;

        for (int i = 1; i < start.length; i++) {

            int prevInterval = Integer.MAX_VALUE;
            int prevEnd = 0;

            for (int j = i; j < start.length; j++) {

                if (start[j] == end[previous]) {
                    final int currentInterval = end[j] - start[j]; // 13-12 = 1,

                    if (currentInterval <= prevInterval) {
                        prevInterval = currentInterval;
                        prevEnd = j;
                    }
                }
            }
            previous = prevEnd;
            count.add(previous);
        }

        // expected count 0 4 5
        System.out.println(count);

        Assert.assertEquals(3, count.size());
    }

    @Test
    public void testMinimizeHeight() {
        final int arr[] = {1, 15, 10};
        final int k = 6;
        int minsofar = Integer.MAX_VALUE;

        for (int i = 0; i < arr.length; i++) {

            if (i + 1 >= arr.length) {
                break;
            }

            final int a = arr[i] + k;
            final int b = arr[i + 1] + k;

            final int min1 = Math.min(a, b);

            final int c = arr[i] - k > 0 ? arr[i] - k : arr[i];
            final int d = arr[i + 1] - k > 0 ? arr[i + 1] - k : arr[i];

            final int min2 = Math.min(c, d);

            if (Math.min(min1, min2) <= minsofar) {
                minsofar = Math.min(min1, min2);
            }
        }
        System.out.println(minsofar);
    }

    @Ignore
    @Test
    public void testMinimumCoin() {

        // final int arr[] = { 1, 5, 1, 2, 5, 1 };
        // final int k = 3;

        final int arr[] = {2, 2, 2, 2};
        final int k = 0;
        int count = 0;

        for (int i = 0; i < arr.length; i++) {

            final int diff = arr[i] - arr[i + 1];

            if (diff <= 0) {
                System.out.printf("negative difff, %d\n", diff);
                continue;
            }

            if (diff <= k) {
                count++;
            } else {
                final int reducedDiff = arr[i] - (diff - k);
                if (reducedDiff - arr[i + 1] <= k) {
                    count++;
                    System.out.printf("count: %d\n", count);
                }
            }
        }

    }

}
