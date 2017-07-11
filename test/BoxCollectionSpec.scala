import controllers._
import org.joda.time.DateTime
import org.scalatest.FunSuite
import reactivemongo.bson.BSONObjectID
import services.{ScalaDriverBoxPersistence}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rizwansharif on 7/3/17.
 */
class BoxCollectionSpec extends FunSuite {

  test("Reactive macros") {
//    val cBox = CorrugatedBox(BSONObjectID.generate().stringify, 1, 1, 1, DateTime.now(), 4)
//    val rBox = RigidBox(BSONObjectID.generate().stringify, 2, 1, 1, DateTime.now(), 5)
//    val fBox = FoldingBox(BSONObjectID.generate().stringify, 3, 1, 1, DateTime.now(), "b")
//    val boxOfBoxes = BoxOfBoxes(BSONObjectID.generate().stringify, 4, 1, 1, DateTime.now(), Set(cBox, rBox, fBox))
//
//    val persister = new ReactiveBoxPersistence()
//    Await.result(
//      Future.sequence(
//        Seq(
//          persister.save(cBox),
//          persister.save(rBox),
//          persister.save(fBox),
//          persister.save(boxOfBoxes))), Duration(1000, "millis"))
//
//
//    val box = Await.result[Option[CorrugatedBox]](persister.findOneCorrugatedBox(), Duration(1000, "millis"))
//    println(box.get)
//
//    val boxes = Await.result[List[Box]](persister.findAllBoxes(), Duration(1000, "millis"))
//    boxes.foreach(box => println(box.length))
//
//    val totalLength = Await.result(persister.findAggregateLength(), Duration(1000, "millis"))
  }

  test("Scala Driver Macros") {
    val cBox = CorrugatedBox(BSONObjectID.generate().stringify, 1, 1, 1, DateTime.now(), 4)
    val rBox = RigidBox(BSONObjectID.generate().stringify, 2, 1, 1, DateTime.now(), 5)
    val fBox = FoldingBox(BSONObjectID.generate().stringify, 3, 1, 1, DateTime.now(), "b")
    val boxOfBoxes = BoxOfBoxes(BSONObjectID.generate().stringify, 4, 1, 1, DateTime.now(), Set(cBox, rBox, fBox))

    val persister = new ScalaDriverBoxPersistence()
//    Await.result(persister.saveBox(cBox), Duration(1000, "millis"))
          Await.result(Future.sequence(Seq(
            persister.save(cBox),
            persister.save(rBox),
            persister.save(fBox),
            persister.save(boxOfBoxes))), Duration(1000, "millis"))


    val box = Await.result[Option[CorrugatedBox]](persister.findOneCorrugatedBox(), Duration(1000, "millis"))
    println(box.get)
    //
    //      val boxes = Await.result[List[Box]](persister.findAllBoxes(), Duration(1000, "millis"))
    //      boxes.foreach(box => println(box.length))
    //
    //      val totalLength = Await.result(persister.findAggregateLength(), Duration(1000, "millis"))

  }
}
