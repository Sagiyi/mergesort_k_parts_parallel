package sagi.sort;

import sagi.FileUtils;
import sagi.adt.ConcurrentRecordList;
import sagi.entity.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.RecursiveAction;

import static sagi.MyApplication.getConcurrentRecordList;


/**
 * I used the following code in order to create a parallel merge sort with K parts.
 */
//https://stackoverflow.com/questions/61705366/merge-sort-in-k-parts
//https://github.com/alexandermakeev/mergesort-with-forkjoinpool
public class MergeSortKFileAction extends RecursiveAction {

    private final String inputFilePath;
    private final String workspacePath;
    private final int keyFieldNo;
    private final int k;
    private final int p;//left bound (included)
    private final int r;//right bound (excluded)

    public MergeSortKFileAction(String inputFilePath, String workspacePath, int keyFieldNo, int k, int p, int r) {
        this.inputFilePath = inputFilePath;
        this.workspacePath = workspacePath;
        this.keyFieldNo = keyFieldNo;
        this.k = k;
        this.p = p;
        this.r = r;
    }

    @Override
    public synchronized void compute() {
        if (r - p < k) {
            final ConcurrentRecordList concurrentRecordList = getConcurrentRecordList();
            List<Record> busyRecords = null;
            try {
                busyRecords = concurrentRecordList.getIfAvailable(r - p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //read lines p to r from the input file
            int i = 0;
            final List<String> lines = FileUtils.Instance.readSpecificLines(inputFilePath, p, r - p);
            for (String line : lines) {
                final Record record = busyRecords.get(i);
                record.setLine(line);
                record.initKey(keyFieldNo);
                i++;
            }
            busyRecords.sort(Comparator.comparing(Record::getKey));
            Path outputPath = Paths.get(workspacePath + "p" + p + "r" + r);
            busyRecords.forEach(record -> {
                try {
                    Files.write(outputPath, (record.getLine() + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            concurrentRecordList.unlock(busyRecords);
            return;
        }
        int[] pos = new int[k + 1]; //array for saving the indices of the "splits"
        for (int i = 0; i <= k; i++) {
            pos[i] = p + (r - p) * i / k; //saving the array indices
        }
        List<MergeSortKFileAction> actions = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            actions.add(new MergeSortKFileAction(inputFilePath, workspacePath, keyFieldNo, k, pos[i], pos[i + 1])); //sorting
        }
        invokeAll(actions);
        MergeCell mergeCell = new MergeCell(workspacePath, keyFieldNo, pos);
        mergeCell.merge();
    }
}
