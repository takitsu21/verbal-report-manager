package com.mad.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Data {
    public static Map<String, String> dataSet = null;
    public static String[][] dataArray = null;

    public static void setDataSet(Map<String, String> dataSet) {
        Data.dataSet = dataSet;
    }

    public static void setDataArray(String[][] dataArray) {
        Data.dataArray = dataArray;
    }

    public static void save(String data, String name) {
        byte[] bs = data.getBytes();
        Path path = Paths.get(name);

        try {
            Files.write(path, bs);
            System.out.println("save " + name);
        } catch (Exception e) {
            System.out.println("Erreur: " + e);
        }
    }
}
