import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import domain.Cat;
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
            try (InputStream stream = zfile.getInputStream(entry);
                 CSVReader reader = new CSVReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                List<String[]> csv = reader.readAll();
                assertThat(csv).contains(
                        new String[]{"John", "Doe", "120 jefferson st.", "Riverside", " NJ", " 08075"},
                        new String[]{"Jack", "McGinnis", "220 hobo Av.", "Phila", " PA", "09119"}
                );
            }
        }
        if (is != null) {
            is.close();
            zis.close();
        }


    }

    @Test
    void zipPDFReaderTest() throws Exception {
        InputStream is = cl.getResourceAsStream("samplePDF.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipFile zfile = new ZipFile(new File("src/test/resources/" + "samplePDF.zip"));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            try (InputStream stream = zfile.getInputStream(entry)) {
                 PDF pdf = new PDF(stream);
                 assertThat(pdf.text).contains("A Simple PDF File");
            }
        }
        if (is != null) {
            is.close();
            zis.close();
        }
    }

    @Test
    void zipXLSReaderTest() throws Exception {
        InputStream is = cl.getResourceAsStream("sampleXLS.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipFile zfile = new ZipFile(new File("src/test/resources/" + "sampleXLS.zip"));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            try (InputStream stream = zfile.getInputStream(entry)) {
                XLS xls = new XLS(stream);
                assertThat(
                        xls.excel.getSheetAt(0)
                                .getRow(1)
                                .getCell(1)
                                .getStringCellValue()
                ).contains("Dulce");
            }
        }
        if (is != null) {
            is.close();
            zis.close();
        }
    }

    @Test
    void jsonParser() throws Exception {
        InputStream is = cl.getResourceAsStream("cat.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Cat cat = objectMapper.readValue(is, Cat.class);
        assertThat(cat.getAge()).isEqualTo(6);

    }
}
