package services

import com.google.inject.Singleton
import controllers._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONDocument, BSONString}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@Singleton
class ReactiveBoxConnectionManager() {

  val mongoUri = "mongodb://localhost:27017/reactiveProto"

  // Connect to the database: Must be done only once per application
  val driver = MongoDriver()
  val parsedUri = MongoConnection.parseURI(mongoUri)
  val connection = parsedUri.map(driver.connection(_))

  val futureConnection = Future.fromTry(connection)

  def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("reactiveproto"))

  def boxCollection = db1.map[BSONCollection](database => database.collection("box"))

  val collection = Await.result(boxCollection, Duration(1000, "millis"))
}

@Singleton
class ReactiveBoxPersistence extends ReactiveBoxConnectionManager {

  def save(box: CorrugatedReactiveBox): Future[WriteResult] = {
    collection.insert(box)
  }

  def findOneCorrugatedBox(): Future[Option[CorrugatedReactiveBox]] = {

    val query = BSONDocument("length" -> 1)
    collection.find(query).one[CorrugatedReactiveBox]

  }

  def findCorrugatedBoxById(id: String): Future[Option[CorrugatedReactiveBox]] = {

    val query = BSONDocument("_id" -> id)
    collection.find(query).one[CorrugatedReactiveBox]

  }

  def findAllBoxesSortedByLength(): Future[List[ReactiveBox]] = {

    collection.find(BSONDocument()).sort(BSONDocument("length" -> 1)).cursor[CorrugatedReactiveBox]().collect[List](25)

  }

  def findAggregateLength(): Future[Int] = {

    import collection.BatchCommands.AggregationFramework.{Group, Project, SumField}

    val res = collection.aggregate(
      Project(BSONDocument("_id" -> 0, "length" -> 1)),
      List(Group(BSONString("null"))("TotalLength" -> SumField("length")))
    )

    res.map { results =>
      results.head.head.getAs[Int]("TotalLength").getOrElse(0)
    }

  }

  def deleteAll(): Future[WriteResult] = {

    collection.remove(BSONDocument())

  }

}
