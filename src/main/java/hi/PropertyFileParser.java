package hi;

import java.io.*;
import java.util.*;

public class PropertyFileParser {
    public static Map<String, FieldInfo> parsePropertyFile(String filePath) throws IOException {
        Map<String, FieldInfo> fieldInfoMap = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Field")) {
                    String[] parts = line.split("=");
                    String fieldName = parts[0];
                    String[] attributes = parts[1].split(",");
                    String dataType = attributes[0];
                    String variable = attributes[1];
                    int position = Integer.parseInt(attributes[2]);
                    int length = Integer.parseInt(attributes[3]);
                    System.out.println(fieldName+" "+dataType+" "+variable+" "+ position+" "+length);
                    fieldInfoMap.put(fieldName, new FieldInfo(dataType, variable, position, length));
                }
            }
        }
        return fieldInfoMap;
    }

    public static class FieldInfo {
        public String dataType;
        public String variable;
        public int position;
        public int length;

        public FieldInfo(String dataType, String variable, int position, int length) {
            this.dataType = dataType;
            this.variable = variable;
            this.position = position;
            this.length = length;
        }
    }
}

