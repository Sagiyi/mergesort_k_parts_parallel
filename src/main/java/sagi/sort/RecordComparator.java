package sagi.sort;

import sagi.entity.RecordAndFile;

import java.util.Comparator;

public class RecordComparator implements Comparator<RecordAndFile> {

    @Override
    public int compare(RecordAndFile o1, RecordAndFile o2) {
        int keyComparison= o1.getRecord().getKey().compareTo(o2.getRecord().getKey());
        if (keyComparison != 0 ) {
            return keyComparison;
        } else {
            return o1.getRecord().getLine().compareTo(o2.getRecord().getLine());
        }
    }
}
