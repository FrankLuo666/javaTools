package readFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 从文件中读取map
*/

public class GetMapFromFiles {
    private static final Map<String, String> map = new LinkedHashMap<>();

    static {
        try {
            loadMapFromFile("/Users/luo/workSpace/java/tools/src/main/java/readFiles/mapFiles.txt"); // 替换为你的文件路径
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String key = "abc";
        String value = map.get(key);
        System.out.println(value);
    }

    private static void loadMapFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] keyValue = line.split(",");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    map.put(key, value);
                }
            }
        }
    }
}