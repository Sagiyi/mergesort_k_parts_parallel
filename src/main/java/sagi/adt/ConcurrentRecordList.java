package sagi.adt;

import sagi.entity.Record;
import java.util.ArrayList;
import java.util.List;

/**
 * ConcurrentRecordList represents a list of records that use limited size in cache memory
 * Limits the use of memory by a List of Records with certain maxCapacity.
 * Implementation is inspired by ConditionLock
 */
public class ConcurrentRecordList  {
    private boolean locked = false;
    private final int maxCapacity;
    private final List<Record> busyRecords;

    public ConcurrentRecordList(int max) {
        this.maxCapacity = max;
        busyRecords = new ArrayList<>(max);
    }

    public synchronized List<Record> getIfAvailable(int k) throws InterruptedException {
        while (locked || busyRecords.size() + k > maxCapacity) {
            wait();
        }
        locked = true;

        for (int i = 0; i < k; i++) {
            busyRecords.add(new Record());
        }
        return busyRecords.subList(busyRecords.size() - k, busyRecords.size());
    }

    public synchronized void unlock(List<Record> subList){
        busyRecords.removeAll(subList);
        locked = false;
        notifyAll();
    }

    public List<Record> getBusyRecords() {
        return busyRecords;
    }
}
