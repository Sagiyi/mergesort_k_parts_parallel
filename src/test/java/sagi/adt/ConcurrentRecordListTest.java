package sagi.adt;

import org.junit.jupiter.api.Test;
import sagi.entity.Record;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrentRecordListTest {
    private static ConcurrentRecordList list;
    @Test
    void testAdt() throws InterruptedException {
        list = new ConcurrentRecordList(7);
        final List<Record> subList1 = list.getIfAvailable(4);
        Record item0 = subList1.get(0);
        item0.setLine("abc");
        Record item2 = subList1.get(2);
        item2.setLine("qwe");
        list.unlock(Arrays.asList(item0,item2, subList1.get(3)));
        assertEquals(1, list.getBusyRecords().size());

        final List<Record> subList2 = list.getIfAvailable(6);
        assertEquals(7, list.getBusyRecords().size());
        list.unlock(Collections.singletonList(new Record()));
        assertEquals(6, list.getBusyRecords().size());
        final Runnable runnable = () -> {
            System.out.println("in runnable. sleeping 4secs");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("in runnable. unlocking");
            list.unlock(Arrays.asList(new Record(),new Record()));
        };

        Thread thread = new Thread(runnable);
        thread.start();

        final List<Record> subList3 = list.getIfAvailable(2);
    }

}