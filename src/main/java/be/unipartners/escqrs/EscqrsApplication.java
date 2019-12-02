package be.unipartners.escqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EscqrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EscqrsApplication.class, args);
    }

    public boolean doSomething() {
        return true;
    }

}
