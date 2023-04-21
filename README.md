# Overview
A restful API can manage orders which support add/delete/update/search

# Build
Please run the following command to build the project:
```
mvn clean install
```
* All unit tests will be executed during the build process.


# Usage

1. **Run OrderManagementAPIApplication.java**
   ```
        Application 'order-manager-api-service' is running! Access URLs:
        Local: 		http://localhost:8080
        External: 	http://192.168.1.70:8080
        Profile(s): 	[]
   ```

2. **API(s)**
Restful API Java code(framework) is created from OPENAPI 3.0.0 specification. It is defined in **OrderManagement.yaml**. This specification is also used to generate the API documentation. 
It is supported by both Swagger UI and Stoplight.
Maven plugin "openapi-generator-maven-plugin(https://openapi-generator.tech/docs/plugins/)" help create all Java interfaces and classes from the specification.

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

3. **Validation**
* **Date format**: yyyy-MM-dd and not in the future
* **CurrencyCode**: NZD, AUD, USD, GBP, EUR or ISO 4217 currency code
* **TransactionType**: Sale or Refund

4. **Java Runtime Exceptions**
- ServerInternalException
- NotFoundException
- BadRequestException

5. **Tests**
Tests contains:
* Unit test: 
  OrderMapperTest.java (JUnit5): Test the mapping methods between OrderDTO and OrderEntity
* Database test: 
  OrderServiceTest.java  (H2 database and JPATest): Test the database operations
* Controller test: 
  OrderApiControllerTest.java (MockMvc, Mockito): Test the controller methods
* Integration test and exception(error status) test: 
  OrderApiControllerITest.java (TestContainer + Docker+ MySQL): Simulate a real environment. Integration test from end to end.
   
Please run the following command to execute all unit tests:
```
mvn test
```
* **21 test cases totally**
  (Note: A docker container is started in background to run the integration test. Please make sure the docker is installed and running.)
 
  If you want to run the integration test only, please run the following command:
  ```
    mvn -Dtest=OrderApiControllerITest test
  ```
* **9 integration test cases totally**

# References
https://arthurgao.stoplight.io/docs/ordermanagement/branches/main/hn1k3mx2cs882-order-management
https://www.openapis.org/
https://openapi-generator.tech/docs/plugins/
https://swagger.io/