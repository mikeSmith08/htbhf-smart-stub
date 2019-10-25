package uk.gov.dhsc.htbhf.smartstub.helper;

import java.time.LocalDate;

public final class TestConstants {

    public static final String HOMER_FIRST_NAME = "Homer";
    public static final String SIMPSON_LAST_NAME = "Simpson";
    public static final String HOMER_DATE_OF_BIRTH_STRING = "1985-12-31";
    public static final LocalDate HOMER_DATE_OF_BIRTH = LocalDate.parse(HOMER_DATE_OF_BIRTH_STRING);
    public static final String HOMER_EMAIL = "homer@simpson.com";
    public static final String HOMER_MOBILE = "+447700900000";
    public static final String NINO = "EE123456C";
    public static final String MAGGIE_DATE_OF_BIRTH_STRING = "1985-12-31";
    public static final LocalDate MAGGIE_DATE_OF_BIRTH = LocalDate.parse("1985-12-31");
    public static final int UC_MONTHLY_INCOME_THRESHOLD = 40800;

    public static final String SIMPSONS_ADDRESS_LINE_1 = "742 Evergreen Terrace";
    public static final String SIMPSONS_ADDRESS_LINE_2 = "Mystery Spot";
    public static final String SIMPSONS_TOWN = "Springfield";
    public static final String SIMPSONS_POSTCODE = "AA1 1AA";
}
