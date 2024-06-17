package general;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConnectWisetest {

    @Test
    public void test() {

        int[] arr = {1, 1, 2, 2, 1, 2, 1, 1};
        int k = 1;

        List<Integer> list = new ArrayList<>();

        int prev = 0;
        int freqCounter = 0;

        for (int i = 1; i < arr.length; i++) {

            if (arr[prev] == arr[i]) {
                freqCounter++;
                prev = i;
                continue;
            }

            if (freqCounter == k) {
                list.add(arr[prev]); // [a,b]
                freqCounter = 0;
                System.out.printf("\nmatched, %s", list);

            } else {
                list.add(arr[prev]);
            }
            prev = i;

            if (prev == arr.length - 1) {
                list.add(arr[prev]);
            }
        }
        System.out.printf("\nresult : %s\n", list);
    }
}
