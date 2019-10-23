package uk.gov.dhsc.htbhf.smartstub.service.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;

import static uk.gov.dhsc.htbhf.smartstub.service.v1.FirstNameScenario.*;

@Slf4j
@Component
public class CardBalanceGenerator {

    private final int maxLowBalanceInPence;
    private final Map<FirstNameScenario, IntSupplier> scenarioBalanceMap;

    public CardBalanceGenerator(@Value("${get-balance.max-low-balance-in-pence}") int maxLowBalanceInPence,
                                @Value("${get-balance.partial-payment-balance-in-pence}") int partialPaymentBalanceInPence,
                                @Value("${get-balance.high-balance-in-pence}") int highBalanceInPence) {
        this.maxLowBalanceInPence = maxLowBalanceInPence;
        this.scenarioBalanceMap = Map.of(
                NO_TOP_UP, () -> highBalanceInPence,
                PARTIAL, () -> partialPaymentBalanceInPence,
                PAYMENT_ERROR, this::generateRandomLowBalance
        );
    }

    public int generateBalanceForCardId(String cardId) {
        Optional<FirstNameScenario> scenarioMatchingCardIdPrefix = findScenarioMatchingCardIdPrefix(cardId);
        if (scenarioMatchingCardIdPrefix.isEmpty()) {
            return generateRandomLowBalance();
        }
        FirstNameScenario scenario = scenarioMatchingCardIdPrefix.get();
        if (scenario == BALANCE_ERROR) {
            String message = String.format("Card ID provided [%s] matches prefix [%s] that has been configured to trigger an Exception when getting "
                            + "the balance for the card", cardId, BALANCE_ERROR.getCardIdPrefixToMatch());
            log.info(message);
            throw new RuntimeException(message);
        }
        return generateBalanceForScenario(scenario);
    }

    private int generateBalanceForScenario(FirstNameScenario scenario) {
        if (!scenarioBalanceMap.containsKey(scenario)) {
            throw new IllegalArgumentException("No balance response configured for FirstNameScenario: " + scenario);
        }
        return scenarioBalanceMap.get(scenario).getAsInt();
    }

    private Integer generateRandomLowBalance() {
        return ThreadLocalRandom.current().nextInt(0, maxLowBalanceInPence);
    }
}
