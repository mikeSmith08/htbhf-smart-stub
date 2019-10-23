package uk.gov.dhsc.htbhf.smartstub.helper.v1;

import uk.gov.dhsc.htbhf.smartstub.model.v1.DepositFundsRequestDTO;

public class DepositFundsRequestDTOTestDataFactory {

    public static DepositFundsRequestDTO aValidDepositFundsRequest() {
        return buildDefaultRequest().build();
    }

    public static DepositFundsRequestDTO aDepositFundsRequestWithAmount(Integer amount) {
        return buildDefaultRequest()
                .amountInPence(amount)
                .build();
    }

    private static DepositFundsRequestDTO.DepositFundsRequestDTOBuilder buildDefaultRequest() {
        return DepositFundsRequestDTO.builder()
                .amountInPence(1860)
                .reference("My deposit");
    }
}
