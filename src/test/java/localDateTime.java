import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Test
    public void test2(){
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(123456789L);
        longs.add(46541864L);
        longs.add(645151684L);

        longs.stream().forEach(System.out::println);
    }
}
