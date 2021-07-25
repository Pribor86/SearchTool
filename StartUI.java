package ru.job4j.searchtool;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class StartUI {
    public static void main(String[] args) throws IOException {
        Search run = new Search();
        List<Path> pathList = null;
        boolean argsCheck = run.argsVerification(args);

        if (argsCheck) {
            String searchType = run.getValues().get("t");
            String searchName = run.getValues().get("n");
            String logPath = run.getValues().get("o");
            Path dirPath = Path.of(run.getValues().get("d"));
            boolean readyToSave = true;
            switch (searchType) {
                case ("name"):
                    pathList = run.search(dirPath, p -> p.toFile().getName().contains(searchName));
                    break;
                case ("mask"):
                    if (searchName.startsWith("*")) {
                        String temp = searchName.substring(1).trim();
                        pathList = run.search(dirPath, p -> p.toFile().getName().endsWith(temp));
                    } else if (searchName.endsWith("*")) {
                        String temp = searchName.substring(0, searchName.length() - 1);
                        pathList = run.search(dirPath, p -> p.toFile().getName().startsWith(temp));
                    }
                    break;
                case ("regex"):
                    pathList = run.search(dirPath, p -> p.toFile().getName().matches(searchName));
                    break;
                default:
                    System.out.println("Search type is wrong");
                    readyToSave = false;
            }
            if (readyToSave) {
                run.savePaths(pathList, logPath);
            }
        }
    }
}