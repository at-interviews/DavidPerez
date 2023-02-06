# Brief high level overview: 

## Structure
The core of the app is set up as in a 4 package core structure.
ui, domain, data, and common.
Since the app is small, I thought it would be overkill to actually split these up into separate modules, but the design would allow for it to be separated
into distinct modules fairly easily if the app were to grow in size.

**Libraries to note**
* Hilt for dependency injection
* Retrofit and Moshi for API call and serialization
* Compose for UI and Coroutines / Flows for threading.

**data package**:
* Includes repository implementation, retrofit api interface, and dto data classes.

**domain package**:
* Holds the models that are mapped from the dtos
* Repository interface definition, abstracting away the implementation in the data package away from the ui (potentially allowing for implementation classes to be swapped out if needed).
* Use cases would go here, but again I opted not to add a use case for now since the apps scope is still pretty small. 
* If the screen were to grow in complexity, I'd likely introduce use cases and inject the use cases into the view model instead of accessing the repository directly.

**ui package**:
* Has all the UI Composables, ViewModels, and navigation logic