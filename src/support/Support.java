package support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Support {

    public static ClassLoader getClassloader() {
        return getClassloader(Support.class);
    }

    public static ClassLoader getClassloader(Class<?> thisClass) {
        return thisClass.getClassLoader();
    }

    public static Properties appProps() {
        ClassLoader cl = getClassloader();

        // Load properties for the project
        Properties properties = new Properties();
        try {
            properties.load(cl.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * @param input String in the format [a b c d]
     * @return Parsed input list into a Java array where each value is an integer
     */
    public static ArrayList<Integer> parseSublistAsInts(String input) {
        String[] stringList = input.replaceAll("[\\[\\]]", "").split(" ");
        return (ArrayList<Integer>) Arrays.stream(stringList).map(Integer::parseInt).collect(Collectors.toList());
    }


    /**
     * @param is Input stream
     * @return Parsed CSV
     */
    public static String[][] parseCsv(InputStream is) {
        Scanner scanner = new Scanner(is);
        String[][] output = new String[][]{};

        scanner.nextLine();  // Skip header
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[][] newArr = Arrays.copyOf(output, output.length + 1);
            newArr[output.length] = (line.replace(", ", ",").split(","));
            output = newArr;
        }
        scanner.close();
        return output;
    }
}
