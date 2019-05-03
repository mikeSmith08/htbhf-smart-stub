package uk.gov.dhsc.htbhf.smartstub.helper;

import uk.gov.dhsc.htbhf.smartstub.model.DepositFundsRequestDTO;

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
