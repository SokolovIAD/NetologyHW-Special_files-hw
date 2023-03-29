import java.util.List;

public class Main extends Operations {
    public static void main(String[] args) throws Exception {
        //Задача 1
        List<Employee> list = parseCSV(COLUMNMAPPING, FILENAME);
        String json = listToJson(list);
        writeString(json, "data.json");

        //Задача 2
        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2, "data2.json");

        //Задача 3
        String json3 = readString("data2.json");
        List<Employee> list3 = jsonToList(json3);
    }
}
