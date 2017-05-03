package controllers

import _root_.reactivemongo.api.DefaultDB
import _root_.reactivemongo.api.MongoConnection
import _root_.reactivemongo.api.MongoDriver
import _root_.reactivemongo.bson.BSONDocumentReader
import _root_.reactivemongo.bson.BSONDocumentWriter
import _root_.reactivemongo.bson.Macros
import _root_.reactivemongo.bson._
import akka.actor.ActorSystem
import javax.inject._
import play.api._
import play.api.mvc._
import services.{ScalaDriverPersonCreator, ReactivePersonCreator}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */

@Singleton
class AsyncController @Inject()(
  actorSystem: ActorSystem,
  reactivePersonCreator: ReactivePersonCreator,
//  casbahPersonCreator: CasbahPersonCreator,
  scalaDriverPersonCreator: ScalaDriverPersonCreator)
  (implicit exec: ExecutionContext) extends Controller {

  /**
   * Create an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def message = Action.async {
    Future.successful(Ok)
  }

  def reactiveInsert = Action.async {
    reactivePersonCreator.createPerson().map { _ => Ok }
  }

//  def casbahInsert = Action.async {
//    casbahPersonCreator.createPerson().map { _ => Ok }
//  }

  def scalaDriverInsert = Action.async {
    scalaDriverPersonCreator.createPerson().map { _ => Ok }
  }


}
