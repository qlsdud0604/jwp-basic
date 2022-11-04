package core.di.factory;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class SetterInjector implements Injector {

    private static final Logger log = LoggerFactory.getLogger(FieldInjector.class);

    private BeanFactory beanFactory;

    public SetterInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(Class<?> clazz) {
        instantiateClass(clazz);
        Set<Method> injectedMethod = BeanFactoryUtils.getInjectedMethods(clazz);

        for (Method method : injectedMethod) {
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1) {
                throw new IllegalStateException("DI할 메서드 인자는 하나여야 합니다.");
            }

            Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanFactory.getPreInstantiateBeans());
            Object bean = beanFactory.getBean(concreteClazz);
            if (bean == null) {
                bean = instantiateClass(concreteClazz);
            }
            try {
                method.invoke(beanFactory.getBean(method.getDeclaringClass()), bean);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error(e.getMessage());
            }
        }
    }

    /** Class에 대한 빈 인스턴스를 생성하는 메서드 */
    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanFactory.getPreInstantiateBeans());
        Object bean = beanFactory.getBean(concreteClazz);
        if (bean != null) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClazz);
        if (injectedConstructor == null) {
            bean = BeanUtils.instantiate(concreteClazz);
            beanFactory.registerBean(concreteClazz, bean);
            return bean;
        }

        log.debug("Constructor : {}", injectedConstructor);
        bean = instantiateConstructor(injectedConstructor);
        beanFactory.registerBean(concreteClazz, bean);
        return bean;
    }

    /** Constructor에 대한 빈 인스턴스를 생성하는 메서드 */
    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] pType = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();
        for (Class<?> clazz : pType) {
            Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanFactory.getPreInstantiateBeans());
            Object bean = beanFactory.getBean(concreteClazz);
            if (bean == null) {
                bean = instantiateClass(concreteClazz);
            }
            args.add(bean);
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }
}
