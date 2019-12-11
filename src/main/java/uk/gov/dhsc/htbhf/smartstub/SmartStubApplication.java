package uk.gov.dhsc.htbhf.smartstub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.dhsc.htbhf.CommonRestConfiguration;
import uk.gov.dhsc.htbhf.smartstub.controller.v2.DwpEligibilityRequestResolver;
import uk.gov.dhsc.htbhf.smartstub.converter.v2.RequestHeaderToDWPEligibilityRequestConverter;

import java.util.List;

@SpringBootApplication
@Import(CommonRestConfiguration.class)
public class SmartStubApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SmartStubApplication.class, args);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        RequestHeaderToDWPEligibilityRequestConverter converter = new RequestHeaderToDWPEligibilityRequestConverter();
        argumentResolvers.add(new DwpEligibilityRequestResolver(converter));
    }

}
