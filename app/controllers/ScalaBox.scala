package controllers

import org.joda.time.DateTime
import play.api.libs.json.Json

sealed class ScalaBox {
  def _id: String = ""
  def length: Int = 1
  def width: Int = 2
  def height: Int = 3
  def manufactureDate: DateTime = DateTime.now()
}

case class CorrugatedScalaBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  layers: Int) extends ScalaBox

object CorrugatedScalaBox {
  implicit val jsonHander = Json.format[CorrugatedScalaBox]
}

case class RigidScalaBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  numberOfPiece: Int) extends ScalaBox

case class FoldingScalaBox(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  style: String) extends ScalaBox

object FoldingScalaBox {

}
case class ScalaBoxOfBoxes(
  override val _id: String,
  override val length: Int,
  override val width: Int,
  override val height: Int,
  override val manufactureDate: DateTime,
  boxes: Set[ScalaBox]) extends ScalaBox