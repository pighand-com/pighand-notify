package com.pighand.notify;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.pighand.framework.spring.PighandFrameworkConfig;
import com.pighand.framework.spring.api.springdoc.analysis.SpringDocParameter;
import com.pighand.framework.spring.api.springdoc.analysis.SpringDocProperty;
import com.pighand.framework.spring.page.interceptor.PageInterceptor;

import org.mybatis.spring.annotation.MapperScan;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * spring-boot:run
 *
 * @author wangshuli
 */
@EnableAsync
@SpringBootApplication
@MapperScan("com.pighand.notify.mapper")
@EnableConfigurationProperties({PighandFrameworkConfig.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PageInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public PropertyCustomizer propertyCustomizer() {
        return (schema, annotatedType) -> SpringDocProperty.analysis(schema, annotatedType);
    }

    @Bean
    public ParameterCustomizer propertyCustomizers() {
        return (parameterModel, methodParameter) ->
                SpringDocParameter.analysis(parameterModel, methodParameter);
    }
}
