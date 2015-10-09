This is a simple example of a python script that calls an API protected with UMA.

Setup:

Assumption: A custom authorization policy is made in the Gluu Server that enables called "HelloWorldPolicy"
that allows access to a certain client id (to be provided later). A scope is created
called "HelloWorldScope" that maps the HelloWorldPolicy. The Resource Server will then register
a resource set, requiring HelloWorldScope.

1. UMA Client needs to perform OpenID Connect client registration (update policy)

--------------------------------

2. Call API (no RPT token)
https://docs.kantarainitiative.org/uma/draft-uma-core.html#rfc.section.3.1.1

3. Response contains the Permission Ticket number (look in body for permission ticket)

4. Perform openid connect client authentication to obtain an access token (AAT)

5. Call RPT Endpoint with AAT and permission ticket number
https://docs.kantarainitiative.org/uma/draft-uma-core.html#r-am-obtain-permission

6) Get back RPT

7) Call hello world API with RPT...

