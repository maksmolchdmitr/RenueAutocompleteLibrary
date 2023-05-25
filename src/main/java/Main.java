import data.RowData;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

public class Main {
    private static final File RESOURCE_FILE = Paths.get("src/main/resources/airports.csv").toAbsolutePath().toFile();
    private static final RowReader ROW_READER = new RowReader(RESOURCE_FILE);
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        String filterText = getString("Print filter text > ");
        Searcher searcher = new Searcher(RESOURCE_FILE, filterText);
        String startName = getString("Print start name text > ");
        while (!startName.equals("!quit")) {
            long startTime = currentTimeMillis();
            List<RowData> rows = searcher.search(startName);
            long endTime = currentTimeMillis();
            rows.forEach(rowData -> {
                String row = ROW_READER.getRow(rowData);
                String[] values = row.split(",");
                out.printf("%s[%s]\n", values[1], row);
            });
            out.printf(
                    "Количество найденных строк: %d Время, затраченное на поиск: %d мс\n",
                    rows.size(),
                    endTime - startTime
            );
            startName = getString("Print start name text > ");
        }
        // Получаем экземпляр класса Runtime
        Runtime runtime = Runtime.getRuntime();
        // Вычисляем количество используемой памяти в мб
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        System.out.println("Используемая память: " + usedMemory + " МБ");
    }

    private static String getString(String message) {
        out.print(message);
        return in.nextLine();
    }
}