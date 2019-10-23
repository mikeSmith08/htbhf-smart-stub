package uk.gov.dhsc.htbhf.smartstub.factory.v1;

public class PostcodeDataFactory {

    public static String postcodeData(String postcode) {
        return "\"postcode\": \"" + postcode + "\",\n"
                + "        \"quality\": 1,\n"
                + "        \"eastings\": 358705,\n"
                + "        \"northings\": 173153,\n"
                + "        \"country\": \"England\",\n"
                + "        \"nhs_ha\": \"South West\",\n"
                + "        \"longitude\": -2.595721,\n"
                + "        \"latitude\": 51.455841,\n"
                + "        \"european_electoral_region\": \"South West\",\n"
                + "        \"primary_care_trust\": \"Bristol\",\n"
                + "        \"region\": \"South West\",\n"
                + "        \"lsoa\": \"Bristol 032B\",\n"
                + "        \"msoa\": \"Bristol 032\",\n"
                + "        \"incode\": \"4TB\",\n"
                + "        \"outcode\": \"BS1\",\n"
                + "        \"parliamentary_constituency\": \"Bristol West\",\n"
                + "        \"admin_district\": \"Bristol, City of\",\n"
                + "        \"parish\": \"Bristol, City of, unparished area\",\n"
                + "        \"admin_county\": null,\n"
                + "        \"admin_ward\": \"Central\",\n"
                + "        \"ced\": null,\n"
                + "        \"ccg\": \"NHS Bristol, North Somerset and South Gloucestershire\",\n"
                + "        \"nuts\": \"Bristol, City of\",\n"
                + "        \"codes\": {\n"
                + "            \"admin_district\": \"E06000023\",\n"
                + "            \"admin_county\": \"E99999999\",\n"
                + "            \"admin_ward\": \"E05010892\",\n"
                + "            \"parish\": \"E43000019\",\n"
                + "            \"parliamentary_constituency\": \"E14000602\",\n"
                + "            \"ccg\": \"E38000222\",\n"
                + "            \"ced\": \"E99999999\",\n"
                + "            \"nuts\": \"UKK11\"\n"
                + "        }";
    }
}
