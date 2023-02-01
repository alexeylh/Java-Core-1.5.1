import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String resultFileName = "data.json";
        List<Employee> list = parseCSV(columnMapping, fileName);
        System.out.println("Список сотрудников получен. Количество сотрудников: " + list.size());
        String json = listToJson(list);
        if (writeString(json, resultFileName)) {
            System.out.println("Файл " + resultFileName + " создан.");
        } else {
            System.out.println("Запись результата не была произведена.");
        }
    }

    static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy).build();
            List<Employee> result = csv.parse();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    static boolean writeString(String json, String resultFileName) {
        try(FileWriter writer = new FileWriter(resultFileName, false))
        {
            writer.write(json);
            writer.flush();
            return true;
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
