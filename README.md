# scalaflagr
A generic flagr evaluation client

- Unsupported Features
  - [ ] Authentication 
  - [ ] Export API

#### Evaluation Client
```scala
// Provide a http client and cacher
val service = new FlagrService[String, Future](client, new NoCache())
val flagrContext = EntityContext("flag-key", entityContext)
val result = service.isEnabled(flagrContext)
```



#### Management Client (for manipulating feature flags via API)
```scala
import io.github.crystailx.scalaflagr.client.syntax._

val flagrConfig = FlagrConfig("http://localhost:18000", "/api/v1")
val manager = new SttpManagerClient(flagrConfig)
manager.createTag(1L, createTagRequest value "new tag")
```