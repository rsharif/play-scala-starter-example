//package services
//
//import javax.inject.Inject
//
//import com.google.inject.Singleton
//import controllers.Person
//
//import scala.concurrent.{Future, ExecutionContext}
//import ExecutionContext.Implicits.global
//import com.mongodb.casbah.Imports._
//
//import scala.util.Random
//
//class CasbahPersonCreator @Inject()(personCollection: CasbahPersonCollection) {
//    def createPerson(): Future[Unit] = Future {
//      val person = new Person("firstName" + Random.nextInt(10).toString, "lastName" + Random.nextInt(15).toString, Random.nextInt(2))
//      personCollection.createPerson(person)
//    }
//}
//
//@Singleton
//class CasbahConnectionManager  {
//  val mongoClient = MongoClient("localhost", 27017)
//  val db = mongoClient("casbahproto")
//
//
//}
//
//@Singleton
//class CasbahPersonCollection() extends CasbahConnectionManager {
//  val coll = db("person")
//  def createPerson(person: Person) = {
//    coll.insert(write(person))
//  }
//
//  private def write(person: Person): MongoDBObject = {
//    MongoDBObject("firstName" -> person.firstName, "lastName" -> person.lastName, "age" -> person.age)
//  }
//}
