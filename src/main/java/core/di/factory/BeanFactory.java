package core.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private List<Injector> injectors;

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;

        injectors = Arrays.asList(
                new FieldInjector(this),
                new SetterInjector(this),
                new ConstructorInjector(this)
        );
    }

    public Set<Class<?>> getPreInstantiateBeans() {
        return preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> clazz : preInstantiateBeans) {
            if (beans.get(clazz) == null) {
                log.debug("Instantiated Class : {}", clazz);
                inject(clazz);
            }
        }
    }

    private void inject(Class<?> clazz) {
        for (Injector injector : injectors) {
            injector.inject(clazz);
        }
    }
    void registerBean(Class<?> clazz, Object bean) {
        beans.put(clazz, bean);
    }

    /** @Controller 애너테이션이 설정된 클래스 정보를 반환 */
    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for (Class<?> clazz : preInstantiateBeans) {
            Annotation annotation = clazz.getAnnotation(Controller.class);
            if (annotation != null) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }

    void clear() {
        preInstantiateBeans.clear();
        beans.clear();
    }
}