package sagi;

import sagi.adt.ConcurrentRecordList;
import sagi.entity.Input;
import sagi.entity.Record;
import sagi.sort.ParallelMergeSortKFileImpl;

public class MyApplication {
    public static final String BASE_PATH = "C:\\Users\\XXXX\\Downloads\\";
    private static final String KEY = "ddd";
    private static final int K = 5;
    private static final String INPUT_FILE_PATH = BASE_PATH + "mergesort-k-parts-parallel\\src\\main\\resources\\input.csv";
    private static final String OUTPUT_FILE_PATH = BASE_PATH + "mergesort-k-parts-parallel\\src\\main\\resources\\output.csv";
    private static final String TEMP_WORKSPACE_PATH = BASE_PATH + "mergesort-k-parts-parallel\\src\\main\\resources\\tmp\\";

    private static ConcurrentRecordList concurrentRecordList;

    public static void main(String[] args) {
        int keyFiledNo = findKeyFiledNo(KEY);
        Input input = new Input(keyFiledNo, K, INPUT_FILE_PATH, OUTPUT_FILE_PATH, TEMP_WORKSPACE_PATH);
        initConcurrentRecordList(K);
        ParallelMergeSortKFileImpl mergeSort = new ParallelMergeSortKFileImpl();
        mergeSort.sort(input);
    }

    private static int findKeyFiledNo(String key) {
        String header = FileUtils.Instance.readSpecificLines(INPUT_FILE_PATH,0,1).get(0);
        Record record = new Record();
        record.setLine(header);
        return record.keyToKeyFieldNo(key);
    }

    public static void initConcurrentRecordList(int k) {
        concurrentRecordList = new ConcurrentRecordList(K);
    }

    public static ConcurrentRecordList getConcurrentRecordList() {
        return concurrentRecordList;
    }
}