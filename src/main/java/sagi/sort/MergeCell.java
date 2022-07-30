package sagi.sort;


import sagi.adt.ConcurrentRecordList;
import sagi.entity.Record;
import sagi.entity.RecordAndFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static sagi.MyApplication.getConcurrentRecordList;

public class MergeCell {
    private final String workspacePath;
    private TreeSet<RecordAndFile> entries;
    private final Comparator<? super RecordAndFile> comparator = new RecordComparator();
    private Map<String, Scanner> scanners;
    private final Path outputPath;
    private final int keyFieldNo;
    private final int k;

    public MergeCell(String workspacePath, int keyFieldNo, int[] pos){
        this.workspacePath = workspacePath;
        this.keyFieldNo = keyFieldNo;
        this.k = pos.length - 1;
        List<String> files = new ArrayList<>();
        for (int i=0; i < k; i++){
            files.add( "p"+pos[i] + "r"+pos[i+1]);
        }
        outputPath = Paths.get(workspacePath +  "p" + pos[0] + "r" + pos[pos.length-1]);
        initMergeCell(files);
    }

    public void initMergeCell(List<String> files){
        scanners= new HashMap<>();
        files.forEach(file -> {
            try {
                scanners.put(file, new Scanner(new File(workspacePath + file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void merge(){
        entries = new TreeSet<>(comparator);
        final ConcurrentRecordList concurrentRecordList = getConcurrentRecordList();
        List<Record> busyRecords = null;
        try {
            busyRecords = concurrentRecordList.getIfAvailable(k);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i=0;
        for (Map.Entry<String, Scanner> entry : scanners.entrySet()){
            String file = entry.getKey();
            Scanner scanner = entry.getValue();
            readRecord(file, scanner, busyRecords.get(i));
            i++;
        }

        while (!entries.isEmpty()) {
            final RecordAndFile minRecordAndFile = entries.pollFirst();
            String recordStr = minRecordAndFile.getRecord().getLine() + System.lineSeparator();
            try {
                Files.write(outputPath, recordStr.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final String curMinFile = minRecordAndFile.getFile();
            readRecord(curMinFile, scanners.get(curMinFile), minRecordAndFile.getRecord());
        }
        concurrentRecordList.unlock(busyRecords);
    }

    private void readRecord(String file, Scanner scanner, Record record) {
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            record.setLine(line);
            record.initKey(keyFieldNo);
            RecordAndFile recordAndFile = new RecordAndFile(record, file);
            entries.add(recordAndFile);
        }
    }
}
