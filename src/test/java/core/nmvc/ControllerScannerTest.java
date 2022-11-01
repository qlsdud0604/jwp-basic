package core.nmvc;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.*;

public class ControllerScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);

    private ControllerScanner scanner;

    @Before
    public void setup() {
        scanner = new ControllerScanner("core.mvc");
    }

    @Test
    public void getControllers() throws Exception {
        Map<Class<?>, Object> controllers = scanner.getControllers();

        for (Class<?> controller : controllers.keySet()) {
            logger.debug("controller : {}", controller);
        }
    }
}