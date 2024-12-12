package com.uday;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Configuration
public class ThymeleafConfig {

    // Define a ClassLoaderTemplateResolver bean
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");  // Location of your templates
        templateResolver.setSuffix(".html");  // Template file extension
        templateResolver.setTemplateMode("HTML");  // Template mode
        templateResolver.setCharacterEncoding("UTF-8");  // Encoding for the templates
        return templateResolver;
    }

    // Define a SpringTemplateEngine bean
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());  // Inject the template resolver
        return templateEngine;
    }

    // Define a ThymeleafViewResolver bean
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());  // Inject the template engine
        return viewResolver;
    }
}
