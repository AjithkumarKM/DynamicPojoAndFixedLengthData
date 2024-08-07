package hi;

import java.lang.reflect.Field;
import java.util.Vector;

public class ClassLoaderUtil {
    public static void printLoadedClasses() {
        try {
            Field f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            ClassLoader classLoader = PojoGenerator.class.getClassLoader();
            Vector<Class<?>> classes = (Vector<Class<?>>) f.get(classLoader);
            for (Class<?> c : classes) {
                System.out.println(c.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
