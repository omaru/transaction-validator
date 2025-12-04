package com.github.omaru.transaction.validator.test.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {
    private static final long TARGET_SIZE = 50L * 1024L * 1024L; // 50 MB

    public static void main(String[] args) throws IOException {
        String filePath = "records-fat.csv";
        String header = "Reference,AccountNumber,Description,Start Balance,Mutation,End Balance\n";
        String[] rows = {
                "194261,NL91RABO0315273637,Book John Smith,21.6,-41.83,-20.23\n",
                "112806,NL27SNSB0917829871,Clothes Irma Steven,91.23,+15.57,106.8\n",
                "183049,NL69ABNA0433647324,Book Arndt Thilo,86.66,+44.5,131.16\n",
                "183356,NL74ABNA0248990274,Toy Jimmie Clarice,92.98,-46.65,46.33\n",
                "112806,NL69ABNA0433647324,Book Peter de Vries,90.83,-10.91,79.92\n",
                "112806,NL93ABNA0585619023,Book Richard Tyson,102.12,+45.87,147.99\n",
                "139524,NL43AEGO0773393871,Flowers Jan Tyson,99.44,+41.23,140.67\n",
                "179430,NL93ABNA0585619023,Clothes Dolores Kerensa,23.96,-27.43,-3.47\n",
                "141223,NL93ABNA0585619023,Book Mahala Kreszenz,94.25,+41.6,135.85\n",
                "195446,NL74ABNA0248990274,Toy Hal Shavonne,26.32,+48.98,75.3\n"
        };

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(header);
            long written = header.getBytes().length;
            int i = 0;

            while (written < TARGET_SIZE) {
                String row = rows[i % rows.length];
                writer.write(row);
                written += row.getBytes().length;
                i++;
            }

            System.out.println("Archivo generado: " + filePath + " (" + written + " bytes)");
        }

    }
}
