## Usage of the application :
IntegrationProjectApi21 is an simple application that make GET request to entered URL and show the JSON response in Card View
 
<img src="demo.gif" width="360" height="640" alt="Demonstrating the app"> 

## SDK Integration 
1. Add the SDK as dependancy in [app/build.gradle.kts](app/build.gradle.kts) file.
2. Start the execution of SDK in [App's application class](app/src/main/java/com/example/integrationprojectapi21/ClientApplication.java).
3. Add SDK's Interceptor with OkHttp Client when building it ([NetworkRequests.java](app/src/main/java/com/example/integrationprojectapi21/NetworkRequests.java) ).
* once the developer performed all 3 steps ,whenever an app performs HTTP request ,an additional header (token) will be added
* refer SDK integration Document for more clarity

