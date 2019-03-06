# htbfh-smart-stub
Service for stubbing out HMRC and DWP apis.

## Description
The service returns stubbed responses depending on the national insurance number (nino) which is sent in the request.
The nino is encoded as follows:

* The first character determines if the person as been found or not. 

  'A' will return a 404 response, anything else will return a valid response.

  e.g. AB123456C will return 404 not found, 
     DB123456C will return a valid response.
    
* The first numeric digit represents the number of children under one.
  e.g BA200000C will return a valid response with two children under one.
  
* The second numeric digit represents the number of children under four.
  e.g BA030000C will return a valid response with three children under four.
  
* The third numeric digit represents if the person is on universal credits. Any digit accept '0' means the person is receiving universal credits.

* The fourth numeric digit represents if the person is on child tax credits. Any digit accept '0' means the person is receiving child tax credits.
