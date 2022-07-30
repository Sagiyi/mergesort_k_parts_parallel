package sagi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public enum FileUtils {
    Instance;

    public List<String> readSpecificLines(String filename, int fromLine, int numOfLines) {
        List<String> linesList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filename))){
            final Iterator<String> iterator = lines.skip(fromLine).iterator();
            for (int i=0; i < numOfLines; i++){
                String line = iterator.next();
                linesList.add(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return linesList;
    }

    public long countRecordsInFile(String filePath) {
        try {
            return Files.lines(Paths.get(filePath), Charset.defaultCharset()).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public File prepareWorkspace(String workspacePath) {
        File workspaceFolder = new File(workspacePath);
        if (workspaceFolder.exists()) {
            System.out.println("Please delete " + workspacePath + " since it is for temporary use and will be created and deleted during the sort.");
            throw new RuntimeException("Folder " + workspacePath + " should not exist");
        } else {
            workspaceFolder.mkdir();
        }
        return workspaceFolder;
    }

    public void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}
