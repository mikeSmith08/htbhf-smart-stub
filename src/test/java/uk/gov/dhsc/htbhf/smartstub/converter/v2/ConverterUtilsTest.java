package uk.gov.dhsc.htbhf.smartstub.converter.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.NativeWebRequest;
import uk.gov.dhsc.htbhf.smartstub.helper.TestConstants;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConverterUtilsTest {

    @Mock
    private NativeWebRequest nativeWebRequest;

    @Test
    void shouldGetDate() {
        //Given
        given(nativeWebRequest.getHeader(any())).willReturn(TestConstants.HOMER_DATE_OF_BIRTH_STRING);
        //When
        LocalDate dateOfBirth = ConverterUtils.nullSafeGetDate(nativeWebRequest, "dateOfBirth");
        //Then
        assertThat(dateOfBirth).isEqualTo(TestConstants.HOMER_DATE_OF_BIRTH);
        verify(nativeWebRequest).getHeader("dateOfBirth");
    }

    @Test
    void shouldReturnNullWhenDateHeaderNotPresent() {
        //Given
        given(nativeWebRequest.getHeader(any())).willReturn(null);
        //When
        LocalDate dateOfBirth = ConverterUtils.nullSafeGetDate(nativeWebRequest, "dateOfBirth");
        //Then
        assertThat(dateOfBirth).isNull();
        verify(nativeWebRequest).getHeader("dateOfBirth");
    }

    @Test
    void shouldGetInteger() {
        //Given
        given(nativeWebRequest.getHeader(any())).willReturn("40800");
        //When
        Integer threshold = ConverterUtils.nullSafeGetInteger(nativeWebRequest, "ucMonthlyIncomeThreshold");
        //Then
        assertThat(threshold).isEqualTo(TestConstants.UC_MONTHLY_INCOME_THRESHOLD);
        verify(nativeWebRequest).getHeader("ucMonthlyIncomeThreshold");
    }

    @Test
    void shouldReturnNullWhenIntegerHeaderNotPresent() {
        //Given
        given(nativeWebRequest.getHeader(any())).willReturn(null);
        //When
        Integer threshold = ConverterUtils.nullSafeGetInteger(nativeWebRequest, "ucMonthlyIncomeThreshold");
        //Then
        assertThat(threshold).isNull();
        verify(nativeWebRequest).getHeader("ucMonthlyIncomeThreshold");
    }
}
