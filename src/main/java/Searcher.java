import data.RowData;

import java.io.File;
import java.util.List;

public final class Searcher {
    private final File file;
    private final String filter;

    public Searcher(File file, String filter) {
        this.file = file;
        this.filter = filter;
    }

    public List<RowData> search(String startName){
        return List.of();
    }
}
