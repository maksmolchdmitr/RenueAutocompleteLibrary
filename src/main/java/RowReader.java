import data.RowData;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class RowReader {
    private final File file;

    public RowReader(File file) {
        this.file = file;
    }

    public String getRow(RowData rowData) {
        try (
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")
        ) {
            randomAccessFile.seek(rowData.getBegin());
            byte[] buffer = new byte[rowData.getSize()];
            randomAccessFile.readFully(buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
