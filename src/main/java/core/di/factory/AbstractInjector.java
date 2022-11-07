package core.di.factory;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

public abstract class AbstractInjector implements Injector {
    private static final Logger log = LoggerFactory.getLogger(AbstractInjector.class);

    private BeanFactory beanFactory;

    public AbstractInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(Class<?> clazz) {
            instantiateClass(clazz);
            Set<?> injectedBeans = getInjectedBeans(clazz);
            for (Object injectedBean : injectedBeans) {
                Class<?> beanClass = getBeanClass(injectedBean);
                inject(injectedBean, instantiateClass(beanClass), beanFactory);
            }
    }

    abstract Set<?> getInjectedBeans(Class clazz);

    abstract Class<?> getBeanClass(Object injectedBean);

    abstract void inject(Object injectedBean, Object bean, BeanFactory beanFactory);

    /** Class에 대한 빈 인스턴스를 생성하는 메서드 */
    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanFactory.getPreInstantiateBeans());
        Object bean = beanFactory.getBean(concreteClass);
        if (bean != null) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
        if (injectedConstructor == null) {
            bean = BeanUtils.instantiate(concreteClass);
            beanFactory.registerBean(concreteClass, bean);
            return bean;
        }

        log.debug("Constructor : {}", injectedConstructor);
        bean = instantiateConstructor(injectedConstructor);
        beanFactory.registerBean(concreteClass, bean);
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
