import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractFirstString {
    public static void main(String[] args) {
        String cobolCode = "289811  03  C_終了                     PIC  X(01) VALUE '1'.\n" +
                "289811  01 03 05  C_終了2                     PIC  X(01) VALUE '1'.";

//        (?<!\\S)：使用否定的前置断言，确保前面是空格（即确保前面不是非空白字符）。
//        (\\S[^\\s]+)：捕获一个或多个非空白字符，并且第一个字符不是空白字符，作为提取的字符串。
//        \\s+PIC：匹配一个或多个空白字符后的 "PIC"。
        Pattern pattern = Pattern.compile("(?<!\\S)(\\S[^\\s]+)\\s+PIC");

        String[] lines = cobolCode.split("\\n");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String extractedString = matcher.group(1).trim();
                System.out.println(extractedString);
            }
        }
    }
}
