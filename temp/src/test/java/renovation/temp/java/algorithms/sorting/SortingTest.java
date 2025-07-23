/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.sorting;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SortingTest {

    private static Logger LOG = LoggerFactory.getLogger(SortingTest.class);

    //y gr :temp:clean :temp:test --tests renovation.temp.java.algorithms.sorting.SortingTest
    interface Sorting {
        int[] sort(int[] array);

        default void swap(int[] array, int i, int j) {
            int swap = array[i];
            array[i] = array[j];
            array[j] = swap;
        }
    }

    static class BubbleSorting implements Sorting {
        public int[] sort(int[] array) {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = i + 1; j < array.length; j++) {
                    if (array[i] > array[j]) {
                        swap(array, i, j);
                    }
                    LOG.info(" > {}", Arrays.toString(array));
                }
                LOG.info(">> {}", Arrays.toString(array));
            }

            return array;
        }
    }

    static class InsertionSorting implements Sorting {
        public int[] sort(int[] array) {
            for (int i = 1; i < array.length; i++) {
                var key = array[i];
                var j = i - 1;

                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j--;
                    LOG.info(" > {}", Arrays.toString(array));
                }
                array[j + 1] = key;
                LOG.info(">> {}", Arrays.toString(array));
            }

            return array;
        }
    }

    static class QuickSorting implements Sorting {
        public int[] sort(int[] array) {
            quickSort(0, array, 0, array.length - 1);

            return array;
        }

        private void quickSort(int in, int[] array, int startIndex, int endIndex) {
            if (startIndex < endIndex) {
                var partitionIndex = partition(in, array, startIndex, endIndex);

                quickSort(++in, array, startIndex, partitionIndex - 1);
                quickSort(++in, array, partitionIndex + 1, endIndex);
            }
        }


        private int partition(int in, int array[], int begin, int end) {
            var pivotal = array[end];
            var i = begin - 1;

            for (int j = begin; j < end; j++) {
                while (array[j] <= pivotal) {
                    swap(array, ++i, j);
                    LOG.info("{} > {}", in, Arrays.toString(array));
                }
            }

            swap(array, ++i, end);
            LOG.info("{}>> {}", in, Arrays.toString(array));
            return i;
        }
    }

    @Test
    void bubbleSort_Successful() {
        var pair = getArrays();
        var originalArray = pair.getLeft();
        var expectedArray = pair.getRight();

        assertArrayEquals(expectedArray, new BubbleSorting().sort(originalArray));
    }

    @Test
    void insertionSort_Successful() {
        var pair = getArrays();
        var originalArray = pair.getLeft();
        var expectedArray = pair.getRight();

        assertArrayEquals(expectedArray, new InsertionSorting().sort(originalArray));
    }

    @Test
    void quickSort_Successful() {
        var pair = getArrays();
        var originalArray = pair.getLeft();
        var expectedArray = pair.getRight();

        assertArrayEquals(expectedArray, new QuickSorting().sort(originalArray));
    }

    private Pair<int[], int[]> getArrays() {
        var originalArray = new int[]{22, 5, -1, -100, -100, Integer.MAX_VALUE, 14, 3, 2, 2, 1};
        var expectedArray = new int[]{-100, -100, -1, 1, 2, 2, 3, 5, 14, 22, Integer.MAX_VALUE};

        // checks
        var originalCopy = Arrays.copyOf(originalArray, originalArray.length);
        Arrays.sort(originalCopy);
        assertArrayEquals(originalCopy, expectedArray);

        return new ImmutablePair<>(originalArray, expectedArray);
    }
}
