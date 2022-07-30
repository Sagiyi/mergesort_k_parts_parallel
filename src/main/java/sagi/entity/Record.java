package sagi.entity;

import com.opencsv.CSVParser;
import lombok.Data;

import java.io.IOException;

@Data
public class Record {
    private static CSVParser parser = new CSVParser();
    private String key;
    private String line;

    public Record() {
    }

    public Record(String line, int keyFieldNo) {
        this.line = line;
        initKey(keyFieldNo);
    }

    private String[] parseLine() {
        String[] fields = new String[0];
        try {
            fields = parser.parseLine(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fields;
    }

    public void initKey(int fieldNo){
        String[] fields = parseLine();
        key = fields[fieldNo];
    }

    /**
     *
     * @param key the key string from the header of the CSV
     * @return the position of the key in the CSV header
     */
    public int keyToKeyFieldNo(String key){
        String[] fields = parseLine();
        for (int i=0;i<fields.length;i++){
            if (key.equals(fields[i])){
                return i;
            }
        }
        System.out.println("Key " + key + " not found");
        return -1;
    }
}
