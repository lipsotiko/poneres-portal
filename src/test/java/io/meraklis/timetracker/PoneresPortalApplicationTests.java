package io.meraklis.timetracker;

import io.meraklis.icare.PoneresPortalApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest(classes = PoneresPortalApplication.class)
class PoneresPortalApplicationTests {

    @Test
    void contextLoads() {
    }

}
