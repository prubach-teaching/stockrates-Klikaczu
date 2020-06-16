package sgh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class StockData {

    public static void getAndProcessChange(String stock) throws IOException {
        String filePath = "data_in/" + stock + ".csv";
        File stockDataFile = new File(filePath);
        // Checking if the file has already been created
        if (!stockDataFile.exists()) {
            download("https://query1.finance.yahoo.com/v7/finance/download/" + stock +
                    "?period1=1554504399&period2=1586126799&interval=1d&events=history", filePath);
        }

        Scanner sc = new Scanner(stockDataFile);
        String line = sc.nextLine();

        // New data file

        FileWriter newDataFile = new FileWriter("data_out/" + stock + ".csv");
        newDataFile.write(line + ",Change\n");

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] elements = line.split(",");
            double openPrice = Double.valueOf(elements[1]);
            double closePrice = Double.valueOf(elements[4]);
            double changeInPrice = (closePrice - openPrice) / openPrice;

            newDataFile.write(line + "," + changeInPrice * 100 + "\n");
        }
        sc.close();
        newDataFile.close();
    }

    public static void download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }

    public static void main(String[] args) throws IOException {
        String[] stocks=new String[] {"IBM", "MSFT", "GOOG"};
        for (String s : stocks){
            getAndProcessChange(s);
        }
    }
}
