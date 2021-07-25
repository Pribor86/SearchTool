package ru.job4j.searchtool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Search {
    private final Map<String, String> values = new HashMap<>();

    public Map<String, String> getValues() {
        return values;
    }

    public List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        SearchFiles searcher = new SearchFiles(condition);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }

    public void savePaths(List<Path> pathList, String file) {
        try (PrintWriter out = new PrintWriter(
                new BufferedOutputStream(
                        new FileOutputStream(file)
                ))) {
            for (Path str : pathList) {
                out.println(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void argsParser(String[] args) {
        if (args.length > 0) {
            for (String ar : args) {
                if (ar.startsWith("-")) {
                    String[] keyValue = ar.substring(1).trim().split("=");
                    if (keyValue.length == 2) {
                        values.put(keyValue[0].toLowerCase(), keyValue[1].toLowerCase());
                    }
                }
            }
        }
    }

    public boolean argsVerification(String[] args) {
        boolean isDir = false;
        boolean isName = false;
        boolean isType = false;
        boolean isLogPath = false;
        argsParser(args);
        isDir = values.containsKey("d");
        isName = values.containsKey("n");
        isType = values.containsKey("t");
        isLogPath = values.containsKey("o");

        if (isDir && isName && isType && isLogPath) {
            //Path validator
            Path dirPath = Path.of(values.get("d"));
            isDir = Files.isDirectory(dirPath);

            //Name(by mask) validator
            if (values.get("t").equals("mask")) {
                String nValue = values.get("n");
                if (!nValue.startsWith("*") && !nValue.endsWith("*")) {
                    isName = false;
                }
            }

            //Mask validator
            String tValue = values.get("t");
            isType = tValue.equals("name") || tValue.equals("mask") || tValue.equals("regex");

            //Log path validator
            String logPath = values.get("o");
            String temp;
            if (!logPath.startsWith("/")) {
                temp = logPath.trim().split("/")[0];
            } else {
                temp = "/" + logPath.trim().split("/")[1];
            }
            isLogPath = Files.isDirectory(Path.of(temp));
            if (isDir && isName && isType && isLogPath) {
                return true;
            } else {
                if (!isDir) {
                    System.out.println("Searching path does not exists");
                }
                if (!isName) {
                    System.out.println("Incorrect mask template. "
                            + "\nPlease use \"*.txt\" or \"whatever.*\" template");
                }
                if (!isType) {
                    System.out.println("Wrong type format."
                            + "\nPlease use \"name\", \"mask\" or \"regex\"");
                }
                if (!isLogPath) {
                    System.out.println("Log destination is incorrect");
                }
            }

        } else {
            System.out.println("Missing arguments:");
            System.out.println();
            if (!isDir) {
                System.out.println("-d=c:/Windows(searching directory path)");
            }
            if (!isName) {
                System.out.println("-n=filename(name of the object you willing to find)");
            }
            if (!isType) {
                System.out.println("-t=name(3 options: name, mask, regex)");
            }
            if (!isLogPath) {
                System.out.println("-o=c:/Windows/log.txt(log file path)");
            }
        }
        return false;
    }
}
