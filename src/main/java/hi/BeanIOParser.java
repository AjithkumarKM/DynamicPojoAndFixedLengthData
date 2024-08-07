package hi;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.stream.fixedlength.FixedLengthRecordParserFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class BeanIOParser {

    public static void parseFixedLengthFile(String filePath, Class<?> pojoClass) throws IOException {
        StreamFactory factory = StreamFactory.newInstance();
        factory.define(new org.beanio.builder.StreamBuilder("file")
                .format("fixedlength")
                .parser(new FixedLengthRecordParserFactory())
                .addRecord(pojoClass));

        try {
        	BeanReader reader = factory.createReader("file", new File(filePath));
        	Object record;
            while ((record = reader.read()) != null) {
                // Process the record (the generated POJO instance)
            	System.out.println("Hello record " + record.toString());
                //System.out.println(record);
            	 if (pojoClass.isInstance(record)) {
                     Object pojoInstance = pojoClass.cast(record);
                     //pojoClass myObject = 
                     // Print the fields of the pojoInstance
                     printFieldValues(pojoInstance, pojoClass);
                 }

            }
        }
        finally{
        	System.out.println("END");
        }
    }
    
    private static void printFieldValues(Object pojoInstance, Class<?> pojoClass) {
        try {
            // Use reflection to access and print the fields
            for (Field field : pojoClass.getDeclaredFields()) {
                field.setAccessible(true); // Make private fields accessible
                Object value = field.get(pojoInstance);
                System.out.println(field.getName() + ": " + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
