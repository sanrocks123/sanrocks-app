/* (C) 2023 */
package programming.algos;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Java Source StackTest.java created on Aug 13, 2021
 *
 * @author : Sanjeev Saxena
 * @version : 1.0
 * @email : sanrocks123@gmail.com
 */
public class StackTest {

    @Test
    public void testBuildNestedTree() {

        List<String> arrSingleNode = List.of("A", "a", "a", "A");
        List<String> arrMultipleNodes = List.of("A", "a1", "a2", "B", "b1", "b2", "B", "A");

        // buildTree(arrSingleNode, new HashMap<String, List<String>>());

    }

    private void buildTree(
            final List<String> arrSingleNode, final HashMap<String, List<String>> startMap) {

        for (int i = 0; i < arrSingleNode.size(); i++) {

            if (StringUtils.isAllUpperCase(arrSingleNode.get(i))) {
                // create node map [key, emptyList]
                // while start node not found
                // keep adding items in node map

            }
        }
    }
}
