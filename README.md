# Overview
A restful API can manage orders which support add/delete/update/search

# Build
Please run the following command to build the project:
```
mvn clean install
```
* All unit tests will be executed during the build process.


# Usage

Any spring-boot application support API interface:

1. **Run OrderManagementAPIApplication.java**
   ```
        Application 'order-manager-api-service' is running! Access URLs:
        Local: 		http://localhost:8080
        External: 	http://192.168.1.70:8080
        Profile(s): 	[]
   ```

2. **API(s)**
Restful API Java code(framework) is created from OPENAPI 3.0.0 specification. It is defined in **OrderManagement.yaml**.

* **Add order**
    * POST http://localhost:3000/a2x/order
    * Request body:
    ```json
    {
      "date": "2019-08-24",
      "amount": 100.2,
      "currencyCode": "NZD",
      "transactionType": "Sale"
    }
    ```
    * Response:
    ```json
    {
        "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08"
    }
    ```
  
* **Update order**
    * PUT http://localhost:3000/a2x/order/497f6eca-6276-4993-bfeb-53cbbbba6f08
    * Request body:
    ```json
    {
      "date": "2019-08-24",
      "amount": 100.2,
      "currencyCode": "NZD",
      "transactionType": "Sale"
    }
    ```
    **(NOTE:All these items are optional but at least have one)**
    * Response:
    OK

* **Delete order**
    * DELETE http://localhost:3000/a2x/order/497f6eca-6276-4993-bfeb-53cbbbba6f08
    * Response:
    NO Content
      
* **Search order**
    * GET http://localhost:3000/a2x/order?date=2019-08-24&currencyCode=NZD&transactionType=Sale
    * Request:
    ```json
      {
        "pageNumber": 1,
          "pageSize": 10
      }
   ```
    * Response:
    ```json
         {
          "results": [
            {
              "id": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
              "date": "2019-08-24",
              "amount": 100.2,
              "currencyCode": "NZD",
              "transactionType": "Sale"
            }
          ],
          "pageInfo": {
            "pageNumber": 0,
            "pageSize": 10,
            "total": 1
          }
        }
    ```
**(Note: The pagination information also can be put into query parameter, put it into request body just want to make it mandatory)**

3. **Java Runtime Exceptions**
- ServerInternalException
- NotFoundException
- BadRequestException

8. **Tests**
Tests contains:
* Unit test: OrderMapperTest.java (JUnit5)
* Database test: OrderServiceTest.java  (H2 database) 
* Controller test: OrderApiControllerTest.java (MockMvc, Mockito)  
* Integration test and exception(error status) test: OrderApiControllerITest.java (TestContainer + Docker+ MySQL)
   
Please run the following command to execute all unit tests:
```
mvn test
```
* **21 test cases totally**

# References
https://arthurgao.stoplight.io/docs/ordermanagement/branches/main/hn1k3mx2cs882-order-management
https://www.openapis.org/