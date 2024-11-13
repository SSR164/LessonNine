import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkingFilesTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ClassLoader classLoader = getClass().getClassLoader();


    private InputStream zip(String fileExtension) throws Exception {
        ZipInputStream zis = new ZipInputStream(classLoader.getResourceAsStream("one.zip"));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().endsWith(fileExtension)) {
                return zis;
            }
        }
        return InputStream.nullInputStream();
    }
@Test
void jjj(){

}

    @Test
    void pdfFileExtension() throws Exception {
        try (InputStream is = zip(".pdf")) {
            PDF pdf = new PDF(is);
            String actualText = pdf.text.replaceAll("\\s+", " ").trim(); // Заменяем несколько пробелов на один и обрезаем
            String expected = "Тестовый PDF-документ Здравствуйте! Это документ в формате PDF, который был создан для тестирования загрузки файлов. Никакой полезной информации он не несёт.";
            assertEquals(expected, actualText);

        }
    }

    @Test
    void xlsFileExtension() throws Exception {
        try (InputStream is = zip(".XLS")) {
            XLS xls = new XLS(is);
            String string = xls.excel.getSheetAt(0).getRow(1).getCell(6).getStringCellValue();
            assertEquals("iivanova@company.ru", string);

        }

    }

    @Test
    void csvFileExtension() throws Exception {
        try (InputStream is = zip(".csv")) {
            CSVReader csvReader= new CSVReader(new InputStreamReader(is));
            List<String[]> data = csvReader.readAll();
            Assertions.assertEquals(2,data.size());
            Assertions.assertEquals("Home", data.get(0)[0]);
            Assertions.assertEquals("Welcome to the selenide wiki!", data.get(0)[1].trim());
            // Assertions.assertEquals(new String[]{"Home", "Welcome to the selenide wiki!"},data.get(0));
            Assertions.assertEquals("Quick Start", data.get(1)[0]);
            Assertions.assertEquals("How to start?", data.get(1)[1].trim());
            //Assertions.assertEquals(new String[]{"Quick Start","How to start?"},data.get(1));




        }



    }
    @Test
    void jsonFileParsingImprovedTest() throws Exception {
        try (Reader reader = new InputStreamReader(
                classLoader.getResourceAsStream("jsonFile.json"))) {
            JsonFile actual = objectMapper.readValue(reader, JsonFile.class);
            Assertions.assertEquals("Eternal Flame", actual.getName());
            Assertions.assertEquals(1000000, actual.getAge());
            Assertions.assertEquals("Super hero squad", actual.getSecretIdentity().getSquadName());
            Assertions.assertEquals(2016, actual.getSecretIdentity().getFormed());
        }

    }
}

