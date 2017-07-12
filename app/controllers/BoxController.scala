package controllers

import javax.inject._

import _root_.reactivemongo.bson._
import akka.actor.ActorSystem
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc._
import services.{ReactiveBoxPersistence, ScalaDriverBoxPersistence}

import scala.concurrent.ExecutionContext


@Singleton
class BoxController @Inject()(
  actorSystem: ActorSystem,
  reactiveBoxPersistence: ReactiveBoxPersistence,
  scalaDriverBoxPersistence: ScalaDriverBoxPersistence
  )
  (implicit exec: ExecutionContext) extends Controller {

  
  def reactiveInsert = Action.async {
    val cBoxId = BSONObjectID.generate().stringify
    val now = DateTime.now()
    val cBox = CorrugatedReactiveBox(_id = cBoxId, length = 1, width = 1, height = 1, manufactureDate = now, layers = 4)
    reactiveBoxPersistence.save(cBox).map(_ => Ok(cBoxId))

  }

  def reactiveRead(id: String) = Action.async {
    reactiveBoxPersistence.findCorrugatedBoxById(id).map {
      case Some(cbox) => Ok(Json.toJson(cbox))
      case None => NotFound
    }
  }

  def scalaDriverInsert = Action.async {
    val cBoxId = BSONObjectID.generate().stringify
    val now = DateTime.now()
    val cBox = CorrugatedScalaBox(_id = cBoxId, length = 1, width = 1, height = 1, manufactureDate = now, layers = 4)
    scalaDriverBoxPersistence.save(cBox).map(_ => Ok(cBoxId))
  }

  def scalaDriverRead(id: String) = Action.async {
    scalaDriverBoxPersistence.findCorrugatedBoxById(id).map {
      case Some(cbox) => Ok(Json.toJson(cbox))
      case None => NotFound
    }
  }

}
