# htbhf-smart-stub
Service for stubbing out DWP and Card Service provider apis.

## V1 Benefit Eligibility Stubbing
The service returns stubbed responses depending on the national insurance number (nino) which is sent in the request.
The nino is encoded as follows:

* The first two characters determine the person's DWP eligibility status. (ELIGIBLE|INELIGIBLE|PENDING|NO_MATCH)
  

  'E' will return ELIGIBLE
  'I' will return INELIGIBLE
  'P' will return PENDING
  Any other character will return NO_MATCH
  
  The first character is checked for a match, if this results in a no match, the second character is then checked.

  e.g. 
       
       EB123456C will return ELIGIBLE according to DWP, 
       FE123456C will return ELIGIBLE according to DWP,
       PE123456C will return PENDING according to DWP,
       DB123456C will return NO_MATCH according to DWP.
             
    
* The first numeric digit represents the number of children under one. (Note, that if this is greater than the number of children under four, 
then the stub will return the same value for children under 1 and children under 4)
  e.g EA220000C will return a valid response with two children under one.
  
* The second numeric digit represents the number of children under four.
  e.g EA030000C will return a valid response with three children under four.
  
* The NINO ZZ999999D can be used if you want to trigger an error within the Smart stub, which will in turn return a 500 response.

The household identifier value is a Base64 generated value based on the NINO.

## V2 Benefit Eligibility and Identity Checking Stubbing

The service returns stubbed responses depending on the national insurance number (NINO) and surname which is sent in the request.
The NINO is encoded as follows:

 * The first character of the NINO is used to determine the Identity Status:
   * If the NINO starts with M the identity status is MATCHED, otherwise NOT_MATCHED
 * The second character of the NINO is used to determine the Eligibility Status:
   * If the second character of the NINO is N the eligibility status is NOT_CONFIRMED
   * If the second character of the NINO is C the eligibility status is CONFIRMED
 * There are 4 verification outcomes specified in the response, `AddressLine1Verification`, `PostcodeVerification`, `MobileVerification` 
 and `EmailVerification`. The surname is then used to determine these verification outcomes as such:
   * A surname of `AddressLineOneNotMatched` returns NOT_MATCHED for `AddressLine1Verification`, MATCHED for `PostcodeVerification` and NOT_SET for other verification outcomes
   * A surname of `PostcodeNotMatched` returns NOT_MATCHED for `PostcodeVerification`, MATCHED for `AddressLine1Verification` and NOT_SET for other verification outcomes
   * A surname of `MobileNotHeld` returns NOT_HELD for `MobileVerification` and MATCHED for other verification outcomes
   * A surname of `EmailNotHeld` returns NOT_HELD for `EmailVerification` and MATCHED for other verification outcomes
   * A surname of `MobileAndEmailNotHeld` returns NOT_HELD for both `EmailVerification` and `MobileVerification` and MATCHED for other verification outcomes
   * A surname of `MobileNotMatched` returns NOT_MATCHED for `MobileVerification` and MATCHED for other verification outcomes
   * A surname of `EmailNotMatched` returns NOT_MATCHED for `EmailVerification` and MATCHED for other verification outcomes
   * A surname of `MobileAndEmailNotMatched` returns NOT_MATCHED for both `EmailVerification` and `MobileVerification` and MATCHED for other verification outcomes
 * The `QualifyingBenefit` is only set if the Identity Status is matched, the Eligibility Status is confirmed and their address has
  been matched. If this is the case then then `QualifyingBenefit` is set to `UNIVERSAL_CREDIT`.
 * The dob of children under 4 is only set if they have a `QualifyingBenefit`.
 * The first and second digits of the NINO are used to determine the dates of birth of children that will be returned:
   * The first is the number of children under 1
   * The second digit is the total number of children under 4 (including those under 1).
   * If the second digit is smaller than the first digit, then a partial children match response is returned, which simply means
   that we'll match the total number of children under 4 digit (2nd digit) and ignore the number of children under 1 digit (1st digit).
   * The dates of birth returned will be as such:
     * Any children under 1 will have a birth day of the first day of the month 6 months ago. e.g. If today is 31 Oct 2019, the date of birth returned
     for a child under 1 will be 01 Apr 2019. This will be the same for all children under 1.
     * Any children between 1 and 4 will similarly have a date of birth of the first day of the month 3 years ago. e.g. If today is 31 Oct 2019, the
     date of birth returned will be 01 Oct 2016 for all children.
  
* The NINO XX999999D can be used if you want to trigger an error within the Smart stub, which will in turn return a 500 response.

## postman collection
Use https://www.getpostman.com/collections/b1e8a55b936abd3879e3 to import a postman collection with api examples.

## Card Service Provider Stubbing

The service returns responses based upon the request sent to the following three api endpoints:

### Create Card

Card number will be returned from a request to create a card and this will trigger all the scenarios below.
The first name will be used to trigger a response from a request to create a new card, and the subsequent
calls will all be triggered from the card id provided from the first call. The first digit of the card id
will define which scenarios below are to be triggered. All first name matching will be case insensitive.
We have specifically left a few prefixes unused so that we have some spare should any further scenarios
arise that we want to cater for.

#### Scenario 1 - Default, Card prefix: 9
Card creation will be successful and a card id will be returned. If the first name doesn't match any other scenarios listed
below then the card prefix will be returned as 9.

#### Scenario 2 - First name: CardError
A first name of CardError will cause the card creation to fail.

### Current Balance

Scenarios for the current balance request are based off the cardId returned from the createCard request,
which is in turn based off the first name provided for that request. Both the available balance and ledger balance
(unreconciled balance) will be returned and will always be the same amount.

#### Scenario 1 - Default, Card prefix: 9

Balance returned is random between 0p and £12.39 (1p less than 4 week's worth of a single voucher,
hence will always allow a full top-up to be made.

#### Scenario 2 - First name: NoTopup, Card prefix: 1

A first name of NoTopup for the card creation request will return a cardId prefixed with 1, which will
in turn return a balance which is too high to allow a top-up (e.g. £1000 just to make sure)

#### Scenario 3 - First name: Partial, Card prefix: 2

A first name of Partial for the card creation request will return a cardId prefixed with 2, which will
in turn return a balance which is 6 times weekly entitlement for a pregnant woman with no children (£18.60).
This will allow a partial payment to be made (2 weeks). Note that this partial payment test is only guaranteed
to work for a pregnant woman with no children

#### Scenario 4 - First name: BalanceError, Card prefix: 3

A first name of BalanceError for the card creation request will return a cardId prefixed with 3, which will
in turn will cause the balance check to fail (create card will have succeeded)

### New payments

#### Scenario 1 - Default, Card prefix: 9

Any first name not listed above will result in a successful payment.

#### Scenario 2 - First name: PaymentError, Card prefix: 4

A first name of PaymentError for the card creation request will return a cardId prefixed with 4, which will
in turn cause the card payment to fail (create card and balance check will have succeeded)
