package services

import com.google.inject.Singleton
import controllers._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONDocument, BSONString}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


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
}

@Singleton
class ReactiveBoxPersistence extends ReactiveBoxConnectionManager{


  def save(box: CorrugatedReactiveBox): Future[WriteResult] = {
    boxCollection.flatMap { collection =>
      collection.insert(box)
    }
  }

  def findOneCorrugatedBox(): Future[Option[CorrugatedReactiveBox]] = {
    boxCollection.flatMap { collection =>
      val query = BSONDocument("length" -> 1)
      collection.find(query).one[CorrugatedReactiveBox]
    }

  }

  def findCorrugatedBoxById(id: String): Future[Option[CorrugatedReactiveBox]] = {
    boxCollection.flatMap { collection =>
      val query = BSONDocument("_id" -> id)
      collection.find(query).one[CorrugatedReactiveBox]
    }
  }

  def findAllBoxesSortedByLength(): Future[List[ReactiveBox]] = {
    boxCollection.flatMap { collection =>
      collection.find(BSONDocument()).sort(BSONDocument("length" -> 1)).cursor[CorrugatedReactiveBox]().collect[List](25)
    }
  }

  def findAggregateLength(): Future[Int] = {

    boxCollection.flatMap { collection =>
      import collection.BatchCommands.AggregationFramework.{Group, Project, SumField}

      val res = collection.aggregate(
        Project(BSONDocument("_id" -> 0, "length" -> 1)),
        List(Group(BSONString("null"))("TotalLength" -> SumField("length")))
      )

      res.map { results =>
        results.head.head.getAs[Int]("TotalLength").getOrElse(0)
      }
    }

  }

  def deleteAll(): Future[WriteResult] = {
    boxCollection.flatMap { collection =>
      collection.remove(BSONDocument())
    }
  }


}
