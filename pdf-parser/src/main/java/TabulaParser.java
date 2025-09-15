import java.io.File;
import java.util.ArrayList;
import java.util.List;

import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TabulaParser {

	public static void main(String[] args) throws Exception {
		
        File pdfFile = new File("/Users/kaushikmitrachowdhury/Downloads/Tables-and-Figures-continued.pdf");
        List<List<String>> rows = extractTableRows(pdfFile);

        // Print each row with its cell values
        for (List<String> row : rows) {
            System.out.println(row);
        }

	}
	
	public static List<List<String>> extractTableRows(File pdfFile) throws Exception { List<List<String>> result = new ArrayList<>();
    boolean headerAdded = false;
    int expectedCols = -1;

    try (PDDocument pd = PDDocument.load(pdfFile)) {
        ObjectExtractor extractor = new ObjectExtractor(pd);
        SpreadsheetExtractionAlgorithm algo = new SpreadsheetExtractionAlgorithm();

        int numPages = pd.getNumberOfPages();
        for (int pageNum = 1; pageNum <= numPages; pageNum++) {
            Page page = extractor.extract(pageNum);
            List<Table> tables = algo.extract(page);

            for (Table table : tables) {
                for (int r = 0; r < table.getRows().size(); r++) {
                    List<RectangularTextContainer> row = table.getRows().get(r);
                    List<String> cells = new ArrayList<>();
                    for (RectangularTextContainer cell : row) {
                        cells.add(cell.getText().trim());
                    }

                    // skip empty rows
                    boolean allEmpty = cells.stream().allMatch(String::isEmpty);
                    if (allEmpty) continue;

                    // detect header
                    if (expectedCols == -1 && r == 0) {
                        expectedCols = cells.size();
                    }

                    // skip non-table junk like "Table 1 (continued)"
                    String joined = String.join(" ", cells).toLowerCase();
                    if (joined.contains("continued") || joined.startsWith("table")) {
                        continue;
                    }

                    // skip header repeats
                    if (r == 0 && headerAdded) continue;

                    // keep only rows matching expected column count
                    if (cells.size() != expectedCols) continue;

                    if (r == 0) headerAdded = true;
                    result.add(cells);
                }
            }
        }
    }
    return result;
    }

}
