import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTest {
    @Test
    public void Test() {
        String data = "2022/7/22";
        try {
            System.out.println(
                    new SimpleDateFormat("yyyy/MM/dd").parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
