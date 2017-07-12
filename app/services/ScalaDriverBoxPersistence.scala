package services

import com.google.inject.Singleton
import com.mongodb.client.result.DeleteResult
import controllers.{CorrugatedScalaBox, ScalaBox}
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonWriter}
import org.joda.time.{DateTime, DateTimeZone}
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs._
import org.mongodb.scala.model.Accumulators._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class JodaCodec extends Codec[DateTime] {

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): DateTime = new DateTime(bsonReader.readDateTime(), DateTimeZone.UTC)

  override def encode(bsonWriter: BsonWriter, t: DateTime, encoderContext: EncoderContext): Unit = bsonWriter.writeDateTime(t.getMillis)

  override def getEncoderClass: Class[DateTime] = classOf[DateTime]
}

@Singleton
class ScalaDriverBoxConnectionManager {
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("scalaDriverDb")
}

@Singleton
class ScalaDriverBoxPersistence extends ScalaDriverBoxConnectionManager {

  val codecRegistry = fromRegistries( fromProviders(classOf[CorrugatedScalaBox]), CodecRegistries.fromCodecs(new JodaCodec), DEFAULT_CODEC_REGISTRY )

  val collection: MongoCollection[CorrugatedScalaBox] = database.getCollection[CorrugatedScalaBox]("box").withCodecRegistry(codecRegistry)

  def save(box: CorrugatedScalaBox): Future[Unit] = {

    collection.insertOne(box).toFuture().map( _ => ())
  }

  def findOneCorrugatedBox(): Future[Option[CorrugatedScalaBox]] = {

    import org.mongodb.scala.model.Filters.{eq => eqTo}
    collection
      .find(eqTo("length", 1))
      .toFuture()
      .map(_.headOption.map(_.asInstanceOf[CorrugatedScalaBox]))
  }

  def findCorrugatedBoxById(id: String): Future[Option[CorrugatedScalaBox]] = {

    import org.mongodb.scala.model.Filters.{eq => eqTo}

    val observable : SingleObservable[CorrugatedScalaBox] = collection
      .find(eqTo("_id", id)).first()

    observable.toFuture().map(box => {
      Option(box).map(_.asInstanceOf[CorrugatedScalaBox])
    })
  }

  def deleteAll(): Future[DeleteResult] = {
    collection.deleteMany(org.mongodb.scala.model.Filters.exists("length")).toFuture()
  }

  def findAllBoxesSortedByLength(): Future[Seq[CorrugatedScalaBox]] = {
    collection
      .find().sort(ascending("length"))
      .toFuture()
  }

  def findAggregateLength() : Future[Int] = {
    val observable: SingleObservable[Document] = collection.aggregate[Document](List(project(and(excludeId(), include("length"))),group(null, sum("total", "$length"))))
    observable.toFuture().map(doc => doc.getInteger("total"))
  }

}
