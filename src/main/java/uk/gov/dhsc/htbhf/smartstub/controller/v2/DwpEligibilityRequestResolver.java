package uk.gov.dhsc.htbhf.smartstub.controller.v2;

import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import uk.gov.dhsc.htbhf.smartstub.converter.v2.RequestHeaderToDWPEligibilityRequestV2Converter;
import uk.gov.dhsc.htbhf.smartstub.model.v2.DWPEligibilityRequestV2;

@AllArgsConstructor
public class DwpEligibilityRequestResolver implements HandlerMethodArgumentResolver {

    private RequestHeaderToDWPEligibilityRequestV2Converter converter;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(DWPEligibilityRequestV2.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return converter.convert(webRequest);
    }
}
