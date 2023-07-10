import java.util.HashMap;
import java.util.Map;

public class JapaneseVariableConverter_bk {
    private static final Map<String, String> japaneseToRomanMap = new HashMap<>();

    static {
        // 添加对应的日文变量名和罗马字变量名的映射关系
        japaneseToRomanMap.put("条件付枝番", "jokenTsukiEdaban");
        // 添加更多的映射关系...
    }

    public static String convertVariableName(String japaneseVariableName) {
        String[] parts = japaneseVariableName.split("_", 2);
        if (parts.length == 2) {
            String prefix = parts[0] + "_";
            String romanSuffix = japaneseToRomanMap.get(parts[1]);
            if (romanSuffix != null) {
                return prefix + romanSuffix;
            }
        }
        return japaneseVariableName;
    }

    public static void main(String[] args) {
        // 示例用法
        String variable = "C_条件付枝番";
        System.out.println("Before conversion: " + variable);

        String romanVariableName = convertVariableName(variable);

        System.out.println("After conversion: " + romanVariableName);
    }
}
