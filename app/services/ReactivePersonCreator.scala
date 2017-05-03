package services

import com.google.inject.Singleton
import controllers.Person
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson._

import scala.concurrent.Future
import javax.inject._

import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ReactivePersonCreator @Inject()(personCollection: ReactivePersonCollection) {

  def createPerson(): Future[Unit] = {
    val person = new Person("firstName" + Random.nextInt(10).toString, "lastName" + Random.nextInt(15).toString, Random.nextInt(2))
    personCollection.createPerson(person)
  }
}

class ReactiveConnectionManager() {

  val mongoUri = "mongodb://localhost:27017/reactiveProto"

  // Connect to the database: Must be done only once per application
  val driver = MongoDriver()
  val parsedUri = MongoConnection.parseURI(mongoUri)
  val connection = parsedUri.map(driver.connection(_))

  val futureConnection = Future.fromTry(connection)

  def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("reactiveproto"))
}

class ReactivePersonCollection extends ReactiveConnectionManager {

  implicit def personWriter: BSONDocumentWriter[Person] = Macros.writer[Person]

  implicit def personReader: BSONDocumentReader[Person] = Macros.reader[Person]

  def personCollection = db1.map(_.collection("person"))

  def createPerson(person: Person): Future[Unit] = {
    val bsonDocument = BSONDocument("firstNamt" -> person.firstName) ++ BSONDocument("lastName" -> person.lastName) ++ BSONDocument("age" -> person.age)
    personCollection.flatMap(_.insert(bsonDocument).map(_ => {})) // use personWriter
  }


}