package uk.gov.dhsc.htbhf.smartstub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import uk.gov.dhsc.htbhf.requestcontext.RequestContextConfiguration;

@SpringBootApplication
@Import(RequestContextConfiguration.class)
public class SmartStubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartStubApplication.class, args);
    }

}
