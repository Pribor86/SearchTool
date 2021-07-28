package ru.job4j.searchtool;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public class StartUI {
    public static void main(String[] args) throws IOException {
        Search run = new Search();
        List<Path> pathList;
        boolean argsCheck = run.argsVerification(args);

        if (argsCheck) {

            String searchType = run.getValues().get("t");
            String inputSearch = run.getValues().get("n");
            String logPath = run.getValues().get("o");
            Path dirPath = Path.of(run.getValues().get("d"));

            Predicate<Path> predicate = run.searchType(searchType, inputSearch);
            pathList = run.search(dirPath, predicate);

            if (run.isReadyToSave()) {
                run.savePaths(pathList, logPath);
            }
        }
    }
}