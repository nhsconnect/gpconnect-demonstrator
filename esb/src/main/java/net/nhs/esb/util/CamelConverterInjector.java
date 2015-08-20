package net.nhs.esb.util;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
@Component
public class CamelConverterInjector implements InitializingBean {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ApplicationContext springContext;

    private abstract static class TypeConverterWrapper extends TypeConverterSupport {
        protected final Method method;
        protected final Object bean;

        protected TypeConverterWrapper(Method method, Object bean) {
            this.method = method;
            this.bean = bean;
        }
    }

    private static final class TypeConverterSimpleWrapper extends TypeConverterWrapper {
        protected TypeConverterSimpleWrapper(Method method, Object bean) {
            super(method, bean);
        }

        @Override
        public <T> T convertTo(Class<T> type, Exchange exchange, Object value) {
            try {
                return (T)method.invoke(bean, value);
            } catch (Throwable t) {
                throw new TypeConversionException(value, type, t);
            }
        }
    }

    private static final class TypeConverterExchangeWrapper extends TypeConverterWrapper {
        protected TypeConverterExchangeWrapper(Method method, Object bean) {
            super(method, bean);
        }

        @Override
        public <T> T convertTo(Class<T> type, Exchange exchange, Object value) {
            try {
                return (T)method.invoke(bean, value, exchange);
            } catch (Throwable t) {
                throw new TypeConversionException(value, type, t);
            }
        }
    }

    public void afterPropertiesSet() throws Exception {
        final Map<String, Object> beans = springContext.getBeansWithAnnotation(Converter.class);
        for (Map.Entry<String, Object> stringObjectEntry : beans.entrySet()) {
            final Object bean = stringObjectEntry.getValue();
            for (Method method : bean.getClass().getMethods()) {
                if (method.getAnnotation(Converter.class) != null) {
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    final TypeConverterWrapper converter;
                    if (parameterTypes.length == 1) {
                        converter = new TypeConverterSimpleWrapper(method, bean);
                    } else {
                        converter = new TypeConverterExchangeWrapper(method, bean);
                    }
                    camelContext.getTypeConverterRegistry().addTypeConverter(method.getReturnType(), parameterTypes[0], converter);
                }
            }
        }
    }

}
