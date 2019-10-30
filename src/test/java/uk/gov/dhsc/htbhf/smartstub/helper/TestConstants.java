package uk.gov.dhsc.htbhf.smartstub.helper;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public final class TestConstants {

    public static final String HOMER_FIRST_NAME = "Homer";
    public static final String SIMPSON_LAST_NAME = "Simpson";
    public static final String HOMER_DATE_OF_BIRTH_STRING = "1985-12-31";
    public static final LocalDate HOMER_DATE_OF_BIRTH = LocalDate.parse(HOMER_DATE_OF_BIRTH_STRING);
    public static final String HOMER_EMAIL = "homer@simpson.com";
    public static final String HOMER_MOBILE = "+447700900000";
    public static final String VALID_NINO_V1 = "EE123456C";
    public static final String VALID_NINO_V2 = "MC123456C";
    public static final String MAGGIE_DATE_OF_BIRTH_STRING = "1985-12-31";
    public static final LocalDate MAGGIE_DATE_OF_BIRTH = LocalDate.parse("1985-12-31");
    public static final int UC_MONTHLY_INCOME_THRESHOLD = 40800;
    public static final String NO_HOUSEHOLD_IDENTIFIER_PROVIDED = "";

    public static final String SIMPSONS_ADDRESS_LINE_1 = "742 Evergreen Terrace";
    public static final String SIMPSONS_ADDRESS_LINE_2 = "Mystery Spot";
    public static final String SIMPSONS_TOWN = "Springfield";
    public static final String SIMPSONS_POSTCODE = "AA1 1AA";

    public static final LocalDate SIX_MONTH_OLD = LocalDate.now().minusMonths(6);
    public static final LocalDate THREE_YEAR_OLD = LocalDate.now().minusYears(3);
    public static final List<LocalDate> TWO_CHILDREN = asList(SIX_MONTH_OLD, THREE_YEAR_OLD);
    public static final List<LocalDate> SINGLE_THREE_YEAR_OLD = singletonList(THREE_YEAR_OLD);
    public static final List<LocalDate> SINGLE_SIX_MONTH_OLD = singletonList(SIX_MONTH_OLD);
}
