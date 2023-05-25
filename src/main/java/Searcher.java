import data.RowData;
import structure.EconomicalTrie;
import structure.StringMap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public final class Searcher {
    private final StringMap<RowData> rowDataStringMap = new EconomicalTrie<>();

    public Searcher(File file, String filterText) {
        Filterer filterer = new Filterer(filterText);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            String row;
            int currIndex = 0;
            while ((row = randomAccessFile.readLine()) != null) {
                String[] values = row.split(",");
                if (filterer.matchesFilter(row)) {
                    rowDataStringMap.add(
                            values[1].replaceAll("\"", ""),
                            new RowData(currIndex, intToShort(row.length()))
                    );
                }
                currIndex = (int) randomAccessFile.getFilePointer();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private short intToShort(int i) {
        if (i > Short.MAX_VALUE) throw new IllegalArgumentException();
        return (short) i;
    }

    public List<RowData> search(String startName) {
        return rowDataStringMap.getValuesByPrefix(startName);
    }
}
