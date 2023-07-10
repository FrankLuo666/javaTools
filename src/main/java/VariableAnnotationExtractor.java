import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
正则表达式 `/\/\*\*\s*\n\*\s*([^\\n]+)\s*\n(\*\s*@\\w+)?\s*\*\/\s*\n(private|public)\s+\w+\s+(\w+);/`
允许在注释中添加额外的行，并且可以匹配 `private` 或 `public` 修饰符。
使用了 `(\\*\\s*@\\w+)?` 部分来匹配可能存在的 `@generated` 注释行。
 */

public class VariableAnnotationExtractor {
    public static void main(String[] args) {
        String classDefinition = "/** \n" +
                "*  姓名\n" +
                "*  @generated\n" +
                "*/ \n" +
                "private String name; \n" +
                "/** \n" +
                "*  年龄\n" +
                "*  @generated\n" +
                "*/ \n" +
                "private String age;" +
                "/**\n" +
                "*  性别\n" +
                "*/\n" +
                "public String gender;";

        Pattern pattern = Pattern.compile("/\\*\\*\\s*\n\\*\\s*([^\\n]+)\\s*\n(\\*\\s*@\\w+)?\\s*\\*/\\s*\n(private|public)\\s+\\w+\\s+(\\w+);");
        Matcher matcher = pattern.matcher(classDefinition);

        while (matcher.find()) {
            String annotation = matcher.group(1).trim();
            String variableName = matcher.group(4).trim();
            System.out.println(annotation + "，" + variableName);
        }
    }
}
