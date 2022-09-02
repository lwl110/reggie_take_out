import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@ContextConfiguration
public class localDateTime {

    @Test
    public void test1(){
        System.out.println(LocalDateTime.now());

        DateTimeFormatter dfDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dfDateTime.format(LocalDateTime.now()));
        String format = dfDateTime.format(LocalDateTime.now());
        System.out.println(dfDateTime.parse(format));

        System.out.println(new Date());
    }

}
