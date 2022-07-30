package sagi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Input {
    private int keyFieldNo;
    private int k;
    private String inputFilePath;
    private String outputFilePath;
    private String tempWorkspacePath;
}
