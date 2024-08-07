package hi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import net.bytebuddy.dynamic.DynamicType.Unloaded;

public class Main {
    public static void main(String[] args) throws IOException {
        String propertyFilePath = "path/to/property/field.properties";
        Map<String, PropertyFileParser.FieldInfo> fieldInfoMap = PropertyFileParser.parsePropertyFile("src\\main\\resources\\field.properties");

        String className = "GeneratedPojo";
        Class<?> pojoClass = PojoGenerator.generatePojoClass(fieldInfoMap, className);
        //Unloaded<Object> pojoClass = PojoGenerator.generatePojoClass(fieldInfoMap, className);
        
        System.out.println(pojoClass.getName());
        //ClassLoaderUtil.printLoadedClasses();
//        File file = new File("Person.class");
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            fos.write(pojoClass.getBytes());
//        }
//        System.out.println("Generated class saved to: " + file.getAbsolutePath());
//        try {
//        	Class<?> hey = Class.forName("hi."+className);
//        	System.out.println(hey.getName());
//        }
//        catch(Exception e) {
//        	System.out.println("try catch " + e);
//        }
        String dataFilePath = "src\\main\\java\\hi\\text.txt";
        BeanIOParser.parseFixedLengthFile(dataFilePath, pojoClass);
    }
}

