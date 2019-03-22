# htbhf-smart-stub
Service for stubbing out HMRC and DWP apis.

## Description
The service returns stubbed responses depending on the national insurance number (nino) which is sent in the request.
The nino is encoded as follows:

* The first character determines the person's DWP eligibility status. (ELIGIBLE|INELIGIBLE|PENDING|NOMATCH)

  'E' will return ELIGIBLE
  'I' will return INELIGIBLE
  'P' will return PENDING
  Any other character will return NOMATCH

  e.g. EB123456C will return ELIGIBLE according to DWP, 
       DB123456C will return NOMATCH according to DWP.
       
* The second character determines the person's HMRC eligibility status. (ELIGIBLE|INELIGIBLE|PENDING|NOMATCH)

  'E' will return ELIGIBLE
  'I' will return INELIGIBLE
  'P' will return PENDING
  Any other character will return NOMATCH

  e.g. BE123456C will return ELIGIBLE according to HMRC, 
       BD123456C will return NOMATCH according to HMRC.
    
* The first numeric digit represents the number of children under one. (Note, that if this is greater than the number of children under four, 
then the stub will return the same value for children under 1 and children under 4)
  e.g EA220000C will return a valid response with two children under one.
  
* The second numeric digit represents the number of children under four.
  e.g EA030000C will return a valid response with three children under four.
  
* The NINO ZZ999999D can be used if you want to trigger an error within the Smart stub, which will in turn return a 500 response.

* Note that the number of children returned will be dependant upon the same digits for both DWP and HMRC requests.

## postman collection
Use https://www.getpostman.com/collections/b1e8a55b936abd3879e3 to import a postman collection with api examples.
