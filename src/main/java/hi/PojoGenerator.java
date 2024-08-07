package hi;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.matcher.ElementMatchers;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import java.io.IOException;
import java.util.Map;

public class PojoGenerator {

    public static Class<?> generatePojoClass(Map<String, PropertyFileParser.FieldInfo> fieldInfoMap, String className) throws IOException {
        //System.out.println(className);
        String fullQualifiedName = className.contains(".")?className:"hi."+className;
    	DynamicType.Builder<Object> builder = new ByteBuddy().subclass(Object.class).name(fullQualifiedName).modifiers(Visibility.PUBLIC);

        for (PropertyFileParser.FieldInfo fieldInfo : fieldInfoMap.values()) {
            Class<?> fieldType;
            switch (fieldInfo.dataType.toLowerCase()) {
                case "int":
                    fieldType = int.class;
                    break;
                case "string":
                    fieldType = String.class;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported data type: " + fieldInfo.dataType);
            }

            // Define the field and its getter/setter
            builder = builder.defineField(fieldInfo.variable, fieldType, java.lang.reflect.Modifier.PRIVATE)
                    .annotateField(createFieldAnnotation(fieldInfo.position, fieldInfo.length))
                    .defineMethod("get" + capitalize(fieldInfo.variable), fieldType, java.lang.reflect.Modifier.PUBLIC)
                    .intercept(FieldAccessor.ofField(fieldInfo.variable))
                    .defineMethod("set" + capitalize(fieldInfo.variable), void.class, java.lang.reflect.Modifier.PUBLIC)
                    .withParameters(fieldType)
                    .intercept(FieldAccessor.ofField(fieldInfo.variable));
        }

        // Annotate the class with @Record
        builder = builder.annotateType(createRecordAnnotation());

        //return builder.make();
        Class<?> genClass = builder.make().load(PojoGenerator.class.getClassLoader()).getLoaded();
        //System.out.println("pojoGenerator : className::"+genClass.getName());
        //System.out.println("pojoGenerator : classloaderName::"+genClass.getClassLoader());

        //return genClass;
        
        DynamicType.Unloaded<Object> unloadedType = builder.make();
        
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        
        Class<?> generatedClass = unloadedType.load(systemClassLoader,ClassLoadingStrategy.Default.INJECTION).getLoaded();
        
        System.out.println("Generated class "+ generatedClass.getName());
        System.out.println("Generated classloader "+ generatedClass.getClassLoader());
        return generatedClass;
    }

    private static AnnotationDescription createFieldAnnotation(int position, int length) {
        return AnnotationDescription.Builder.ofType(Field.class)
                .define("at", position)
                .define("length", length)
                .build();
    }

    private static AnnotationDescription createRecordAnnotation() {
        return AnnotationDescription.Builder.ofType(Record.class).build();
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
