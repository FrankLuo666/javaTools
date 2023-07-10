import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import fr.free.nrw.jakaroma.Jakaroma ;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class JapaneseToRomanConverter {
    private static final Logger logger = LogManager.getLogger(JapaneseToRomanConverter.class);

    public static String convertToRoman(String japaneseText) {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.tokenize(japaneseText);
        StringBuilder romanText = new StringBuilder();

        for (Token token : tokens) {
            romanText.append(token.getPronunciation());
        }

        return romanText.toString();
    }

    public static void main(String[] args) {
        // 使System.out.print失效，仅使用log4j输出:
        // 创建一个空的输出流对象
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {
                // 空实现，忽略所有的输出
            }
        };
        // 将标准输出流重定向到自定义的输出流对象
        System.setOut(new PrintStream(outputStream));

        // 设置需要转换的日文汉字
        String japaneseText = "検索操作";
        // カタカナ
        String katakanaText = convertToRoman(japaneseText);
        logger.info(katakanaText);

        // Romaji
        Jakaroma instance = new Jakaroma();
        String romajiText =  instance.convert(katakanaText, false, true);
        // 将长音"-"符变为"u"
        romajiText = romajiText.replace("-","u");
        logger.info(romajiText);
    }
}
