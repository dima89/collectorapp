# collectorapp


Run collector app: mvn clean jetty:run

Run first repository: mvn clean jetty:run -Djetty.port=8091
 
Run second repository: mvn clean jetty:run -Djetty.port=8092

DB Access:

Server: http://localhost:8082; DB: jdbc:h2:mem:Documents; login: sa; password:;

#Example API calls

Content-Type application/json

ADD:

POST http://localhost:8080/api/rest/add

BODY 

{"name":"First name doc44","title":"First title doc44","indexMap":{"index one":"index two", "index three":"index four"},"content":"First content doc44","comments": [{"content": "First comment doc"}, {"content": "Second comment doc"},{"content": "Second comment doc4664"}]}

GET by id:

GET http://localhost:8080/api/rest/get/664a8fa0-312c-48ee-9863-dcb88ca0c50a

GET all:

GET http://localhost:8080/api/rest/getAllDocuments

UPDATE:

PUT http://localhost:8080/api/rest/update/664a8fa0-312c-48ee-9863-dcb88ca0c50a

BODY 

{"name":"First name doc44","title":"First title doc44","indexMap":{"index one":"index two", "index three":"index four"},"content":"First content doc44","comments": [{"id": "f9bc64b5-03c9-415b-976b-9dd597d585e6","userId": "32895c35-f496-4cb1-a505-7ccb4fbd1be1","content": "First comment doc"}, {"id": "c24fca69-dcab-4554-9aae-1b31e9576a7b","userId": "56bb26b6-8038-4a69-bb6f-508856cf85eb","content": "Second comment doc"},{"content": "Second comment doc4664"}]}

DELETE:

DELETE http://localhost:8080/api/rest/delete/664a8fa0-312c-48ee-9863-dcb88ca0c50a

