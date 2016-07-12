package savings.service.impl;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackageClasses = ServiceConfiguration.class, excludeFilters = {
        // this filter was added to prevent interference with test configurations
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
})
public class ServiceConfiguration {

}
