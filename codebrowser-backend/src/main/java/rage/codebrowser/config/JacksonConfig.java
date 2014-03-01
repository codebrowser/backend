package rage.codebrowser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class JacksonConfig implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MappingJackson2HttpMessageConverter) {
            configJacksonMapper(bean);
        }
        return bean;
    }

    private void configJacksonMapper(Object bean) {
        MappingJackson2HttpMessageConverter converter =
                (MappingJackson2HttpMessageConverter) bean;
        ObjectMapper objectMapper = converter.getObjectMapper();
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // force sorting of nested objs?
        converter.setObjectMapper(objectMapper);
    }
}