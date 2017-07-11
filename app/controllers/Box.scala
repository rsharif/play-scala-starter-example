package controllers

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import reactivemongo.bson.Macros.Options._
import reactivemongo.bson.{BSONDateTime, BSONDocument, BSONHandler, Macros}
import reactivemongo.bson._


sealed class Box {
  def _id: String = ""
  def length: Int = 1
  def width: Int = 2
  def height: Int = 3
  def manufactureDate: DateTime = DateTime.now()
}

case class CorrugatedBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  layers: Int) extends Box

case class RigidBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  numberOfPiece: Int) extends Box

case class FoldingBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  style: String) extends Box

object FoldingBox {

}
case class BoxOfBoxes(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  boxes: Set[Box]) extends Box



object Box {

//  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
//    val fmt = ISODateTimeFormat.dateTime()
//    def read(time: BSONDateTime) = new DateTime(time.value)
//    def write(jdtime: DateTime) = BSONDateTime(jdtime.getMillis)
//  }
//
//  implicit val corrugatedBoxHandler = Macros.handler[CorrugatedBox]
//  implicit val boxHandler = Macros.handlerOpts[Box, AutomaticMaterialization]
//  implicit val rigidBoxHandler = Macros.handler[RigidBox]
//  implicit val foldingBoxHandler = Macros.handler[FoldingBox]
//  implicit val boxOfBoxes = Macros.handler[BoxOfBoxes]

}

