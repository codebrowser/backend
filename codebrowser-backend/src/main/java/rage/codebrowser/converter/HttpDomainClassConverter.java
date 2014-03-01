package rage.codebrowser.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.repository.support.DomainClassConverter;
import rage.codebrowser.errors.ResourceNotFoundException;

public class HttpDomainClassConverter<T extends Object & org.springframework.core.convert.ConversionService & org.springframework.core.convert.converter.ConverterRegistry> extends DomainClassConverter<T> {

    public HttpDomainClassConverter(T conversionService) {
        super(conversionService);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Object result = super.convert(source, sourceType, targetType);
        if(result == null) {
            throw new ResourceNotFoundException();
        }
        
        return result;
    }
}
