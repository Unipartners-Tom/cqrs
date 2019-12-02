package be.unipartners.escqrs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class EscqrsApplicationTests {

    @Test
    void contextLoads() {
        EscqrsApplication e = new EscqrsApplication();
        Assert.isTrue(e.doSomething());
    }

}
