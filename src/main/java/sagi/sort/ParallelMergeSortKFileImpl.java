package sagi.sort;

import sagi.FileUtils;
import sagi.entity.Input;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ParallelMergeSortKFileImpl {

    public void sort(Input input) {
        int keyFieldNo = input.getKeyFieldNo();
        int k = input.getK();
        String workspacePath = input.getTempWorkspacePath();
        String inputFilePath = input.getInputFilePath();
        String outputFilePath = input.getOutputFilePath();

        File workspaceFolder = FileUtils.Instance.prepareWorkspace(workspacePath);

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        int numOfRecords = (int) FileUtils.Instance.countRecordsInFile(inputFilePath);//with more time I'd change everything to support long for file bigger than int.max.
        forkJoinPool.invoke(new MergeSortKFileAction(inputFilePath, workspacePath, keyFieldNo, k, 1, numOfRecords));

        try {
            Files.move(
                    Paths.get(workspacePath + "p1r" + numOfRecords),
                    Paths.get(outputFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addHeader(inputFilePath, outputFilePath);
        FileUtils.Instance.deleteDir(workspaceFolder);
    }

    private void addHeader(String inputFilePath, String outputFilePath) {
        try{
            String header = FileUtils.Instance.readSpecificLines(inputFilePath, 0, 1).get(0);
            List<String> lines = Files.readAllLines(Paths.get(outputFilePath));
            lines.add(0, header);
            Files.write(Paths.get(outputFilePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
