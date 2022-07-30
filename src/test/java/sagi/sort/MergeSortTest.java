package sagi.sort;

import org.junit.jupiter.api.Test;
import sagi.MyApplication;
import sagi.entity.Input;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

class MergeSortTest {

    public static final String BASE_PATH = "C:\\Users\\SAGII\\Downloads\\";
    private static final int KEY_FIELD_NO = 2;
    private static final int K = 5;
    private static final String INPUT_FILE_PATH = BASE_PATH + "mergesort-k-parts-parallel\\src\\main\\resources\\input.csv";
    private static final String OUTPUT_FILE_PATH = BASE_PATH + "mergesort-k-parts-parallel\\src\\main\\resources\\output.csv";
    private static final String TEMP_WORKSPACE_PATH = BASE_PATH + "mergesort-k-parts-parallel\\src\\main\\resources\\tmp\\";


    @Test
    void sortFile() {
        Input input = new Input(KEY_FIELD_NO, K, INPUT_FILE_PATH, OUTPUT_FILE_PATH, TEMP_WORKSPACE_PATH);
        MyApplication.initConcurrentRecordList(K);

        ParallelMergeSortKFileImpl mergeSort = new ParallelMergeSortKFileImpl();
        ZonedDateTime now = ZonedDateTime.now();
        mergeSort.sort(input);
        System.out.printf("%s exec time: %dms\n",
                mergeSort.getClass().getSimpleName(),
                ChronoUnit.MILLIS.between(now, ZonedDateTime.now()));
//        assertTrue(isSorted(arr));//TODO...
    }

}