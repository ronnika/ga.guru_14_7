import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTests {
    ClassLoader cl = FileTests.class.getClassLoader();

    @Test
    void zipCSVReaderTest() throws Exception {
        InputStream is = cl.getResourceAsStream("sampleCSV.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipFile zfile = new ZipFile(new File("src/test/resources/" + "sampleCSV.zip"));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            InputStream istr = zfile.getInputStream(entry);
            try (InputStream stream = getClass().getClassLoader().getResourceAsStream("csv/teachers.csv");
                 CSVReader reader = new CSVReader(new InputStreamReader(istr, StandardCharsets.UTF_8))) {
                List<String[]> csv = reader.readAll();
                assertThat(csv).contains(
                        new String[] {"John","Doe","120 jefferson st.", "Riverside", " NJ", " 08075"},
                        new String[] {"Jack", "McGinnis", "220 hobo Av.", "Phila", " PA", "09119"}
                );
            }
        }

    }

    @Test
    void zipPDFReaderTest() {

    }
}
