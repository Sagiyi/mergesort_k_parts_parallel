package sagi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordAndFile {
    private Record record;
    private String file;
}
