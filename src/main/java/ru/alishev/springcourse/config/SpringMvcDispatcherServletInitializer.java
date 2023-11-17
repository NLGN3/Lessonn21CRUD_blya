package ru.alishev.springcourse.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;
//import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;


//класс, который нам заменяет первичную точку входа - web.xml
// Spring запускает этот класс автоматически, если он наследует именно этот абстрактный класс
public class SpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
//    возвращаем наш конфигурационный класс для диспатчер сервлета
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    @Override
//    активируем наш Dispatcher Servlet на любой URL
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    //ниже два метода, который активируют Patch-запросы (и все остальные, которые по умолчанию не поддерживаются HTML-5)
    //onStartup - запускается при вызове приложения
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }
    //добавляем фильтр нашему приложению
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",                                          //* - фильтр будет работать для всех адресов
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }
}
