package ru.alishev.springcourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
//директория c нашими компонентами(бинами)
@ComponentScan("ru.alishev.springcourse")
//Активируем функции SpringMVC (включаем SpringMVC) (включает необходимые аннотации для SpringMVC- одна из функций), теперь наше приложение поддерживает веб-функции
// аналог <mvc:annotation-driven/> в applicationContextMVC.xml
@EnableWebMvc
//тут активируем доступ к нашим ресурсам, будем получать к ним доступ через объект Environment
//classpath - это типа папочка target\classes, после компиляции наши файлы ресурсов попадут туда
@PropertySource("classpath:database.properties")
// Реализуем интерфейс WebMvcConfigurer когда мы хотим настроить под себя SpringMVC.
// В данном случае мы хотим вместо использования стандартного шаблонизатора использовать шаблонизатор Thymeleaf,
// за это отвечает третий метод configureViewResolvers()
// все это аналогично ApplicationContextMVC.xml из родительского элемента в справочнике HelpNDocs
public class SpringConfig implements WebMvcConfigurer { //extends AbstractAnnotationConfigDispatcherServletInitializer { //
    //здесь мы внедряем Application Context. (?Имеется в виду - Spring за нас автоматом собирает все бины в этот контекст?)
    private final ApplicationContext applicationContext;
    //бин для доступа к ресурсам, Spring для нас его тоже подключит сам через конструктор
    private final Environment environment;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    //здесь мы задаем настраиваем темплейтРесолвера(класс настроек, но нужно уточнить о нем посильнее)
//     передаем контекст, путь и расширение наших вьюшек(представлений)
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/"); //папка где будут лежать наши view
        templateResolver.setSuffix(".html");  //расширение наших view
        return templateResolver;
    }

    // здесь объект темплейтРесолвера передается в темплейтЭнджейн(тоже уточнить про него)
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
//    метод, в котором мы задаем шаблонизатор
//    передаем темплейтЭнджейн со всеми нашими настройками таймлифу
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("driver")));
        dataSource.setUrl(environment.getProperty("url"));
        dataSource.setUsername(environment.getProperty("name"));
        dataSource.setPassword(environment.getProperty("password"));
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/first_db");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("postgres");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}

