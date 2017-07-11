package controllers

import org.mongodb.scala.bson.collection.immutable.Document

case class Person(
  firstName: String,
  lastName: String, age: Int
)

object Person {
  implicit  def convert(person: Person): Document =
    Document("firstName" -> person.firstName, "lastName" -> person.lastName, "age" -> person.age)
}

