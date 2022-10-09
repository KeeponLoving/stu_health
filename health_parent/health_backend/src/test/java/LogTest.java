import org.apache.batik.apps.rasterizer.Main;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
    static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void test(){
        LOGGER.debug("log4j2");
    }
}
