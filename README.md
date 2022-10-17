# scalaflagr
A generic flagr evaluation client 

```scala

```

```scala
import com.crystailx.scalaflagr.json.circe._
import com.crystailx.scalaflagr.syntax._

val service = new FlagrService(client)
val flagrContext = EntityContext("flag-key", entityContext = UserInfo("TW"))
val result = service.isEnabled(flagrContext)
```