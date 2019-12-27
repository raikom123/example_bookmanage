package com.example.bookmanage;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * 書籍管理システムのConfiguration
 *
 * PUT/DELETEをPOSTするためにHiddenHttpMehotdFilterをFilterとして設定する。
 */
@Configuration
public class BookManageConfig {

    /**
     * HiddenHttpMehotdFilterをFilterに設定するためのBeanを返却する。
     *
     * @return HiddenHttpMehotdFilterをFilterに設定するためのBean
     */
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();
        FilterRegistrationBean<HiddenHttpMethodFilter> filterRegBean = new FilterRegistrationBean<>(filter);
        filterRegBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegBean;
    }

}
