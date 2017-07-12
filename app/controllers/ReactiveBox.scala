package controllers

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.Json
import reactivemongo.bson.Macros.Options._
import reactivemongo.bson.{BSONDateTime, BSONDocument, BSONHandler, Macros}
import reactivemongo.bson._


sealed abstract class ReactiveBox {
  def _id: String = ""
  def length: Int = 1
  def width: Int = 2
  def height: Int = 3
  def manufactureDate: DateTime = DateTime.now()
}

case class CorrugatedReactiveBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  layers: Int) extends ReactiveBox

object CorrugatedReactiveBox {
  implicit val jsonHander = Json.format[CorrugatedReactiveBox]
}

case class RigidReactiveBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  numberOfPiece: Int) extends ReactiveBox

case class FoldingReactiveBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  style: String) extends ReactiveBox

object FoldingReactiveBox {

}
case class ReactiveBoxOfBoxes(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  boxes: Set[ReactiveBox]) extends ReactiveBox



object ReactiveBox {

  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    val fmt = ISODateTimeFormat.dateTime()
    def read(time: BSONDateTime) = new DateTime(time.value)
    def write(jdtime: DateTime) = BSONDateTime(jdtime.getMillis)
  }

//  implicit val boxHandler = Macros.handlerOpts[ReactiveBox, AutomaticMaterialization]
  implicit val corrugatedBoxHandler = Macros.handler[CorrugatedReactiveBox]
//  implicit val rigidBoxHandler = Macros.handler[RigidReactiveBox]
//  implicit val foldingBoxHandler = Macros.handler[FoldingReactiveBox]
//  implicit val boxOfBoxes = Macros.handler[ReactiveBoxOfBoxes]

}

