package com.example.bookmanage;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * @author hri
 *
 */
@Configuration
public class BookManageConfig {

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();
        FilterRegistrationBean<HiddenHttpMethodFilter> filterRegBean = new FilterRegistrationBean<>(filter);
        filterRegBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegBean;
    }

}
