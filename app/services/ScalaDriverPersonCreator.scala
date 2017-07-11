package services

import javax.inject.{Singleton, Inject}

import org.mongodb.scala
import org.mongodb.scala._
import org.mongodb.scala.bson.collection.immutable.Document
import controllers.Person

import scala.concurrent.{Promise, Future}
import scala.util.{Try, Random}

/**
 * Created by rizwansharif on 5/2/17.
 */
@Singleton
class ScalaDriverPersonCreator @Inject()(personCollection: ScalaDriverPersonCollection) {

  def createPerson(): Future[Unit] = {

    val person = new Person(
      "firstName" + Random.nextInt(10).toString,
      "lastName" + Random.nextInt(15).toString,
      Random.nextInt(2))
    personCollection.createPerson(person)

  }
}

@Singleton
class ScalaDriverConnectionManager {

  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("scalaDriverDb")

}



@Singleton
class ScalaDriverPersonCollection extends ScalaDriverConnectionManager {

  val collection: MongoCollection[Document] = database.getCollection("person")

  def createPerson(person: Person): Future[Unit] = {
    val promise = Promise[Unit]()

    def observer  = new Observer[Completed] {

      override def onNext(result: Completed): Unit = promise.complete(Try(()))

      override def onError(e: Throwable): Unit = promise.failure(new Exception("Failed to insert :" + e.getMessage))

      override def onComplete(): Unit = promise.complete(Try(()))
    }
    collection.insertOne(person).subscribe(observer)
    collection.find().subscribe(new Observer[Document] {override def onError(e: scala.Throwable): Unit = ???

      override def onComplete(): Unit = ???

      override def onNext(document: Document): Unit = document.getInteger("")
    })

    promise.future
  }



}

