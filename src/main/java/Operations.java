import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Operations {

    public static String[] COLUMNMAPPING = {"id", "firstName", "lastName", "country", "age"};
    public static String FILENAME = "data.csv";

    public static List<Employee> parseCSV(String[] columnMapping, String name) {
        try (CSVReader csvReader = new CSVReader(new FileReader(name))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> stuff = csv.parse();
            return stuff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String name) {

        try (FileWriter file = new FileWriter(name)) {
            file.write(json);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String name) throws Exception {
        List<String> elements = new ArrayList<>();
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(name));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeName().equals("employee")) {
                    NodeList nodeList1 = node.getChildNodes();
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        Node node_ = nodeList1.item(j);
                        if (Node.ELEMENT_NODE == node_.getNodeType()) {
                            elements.add(node_.getTextContent());
                        }
                    }
                    list.add(new Employee(
                            Long.parseLong(elements.get(0)),
                            elements.get(1),
                            elements.get(2),
                            elements.get(3),
                            Integer.parseInt(elements.get(4))));
                    elements.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public static String readString(String url) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(url))) {
            List<String> list = new ArrayList<>();
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                list.add(string);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type listType = new TypeToken<List>() {
            }.getType();
            String json = gson.toJson(list, listType);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Employee> jsonToList(String json) {
        JSONParser parser = new JSONParser();
        List<Employee> list = new ArrayList<>();
        try(FileReader fr = new FileReader(json)) {
            JSONObject jsonObject = (JSONObject) parser.parse(fr);
            JSONArray jsonArray = (JSONArray) jsonObject.get("{");
            for (Object item : jsonArray) {
                JSONObject jSO = (JSONObject) item;
                String id = (String) jsonObject.get("id");
                String firstName = (String) jsonObject.get("firstName");
                String lastName = (String) jsonObject.get("lastName");
                String country = (String) jsonObject.get("country");
                String age = (String) jsonObject.get("age");
                list.add(new Employee(Integer.parseInt(id), firstName, lastName, country, Integer.parseInt(age)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
