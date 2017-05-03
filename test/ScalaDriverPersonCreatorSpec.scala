/**
 * Created by rizwansharif on 5/2/17.
 */
import org.scalatest.FunSuite
import services.{ScalaDriverPersonCollection, ScalaDriverPersonCreator}

class ScalaDriverPersonCreatorSpec extends FunSuite {

  val scalaPersonCreator: ScalaDriverPersonCreator = new ScalaDriverPersonCreator( new ScalaDriverPersonCollection())
  
  test("insert one document") {
    val startTime = System.currentTimeMillis()

    for(i <- 1 to 1000000) {
      scalaPersonCreator.createPerson()
    }

    val endTime = System.currentTimeMillis()
    System.out.println("Total Time In Seconds" + getTime())

    def getTime(): Long = {
      (endTime - startTime)/1000
    }
  }
}
