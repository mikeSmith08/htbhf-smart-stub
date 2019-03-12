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

  e.g. EB123456C will return ELIGIBLE, 
       DB123456C will return NOMATCH.
    
* The first numeric digit represents the number of children under one. (Note, this can not be greater than the number of children under four)
  e.g EA220000C will return a valid response with two children under one.
  
* The second numeric digit represents the number of children under four.
  e.g EA030000C will return a valid response with three children under four.
