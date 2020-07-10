package io.feoktant

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import io.feoktant.EntityDao.entityDao
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

object App extends App with Route {

  implicit val system: ActorSystem = ActorSystem("App")
  implicit val ec: ExecutionContext = system.dispatcher

  val db = Database.forConfig("slick")
  db.run(entityDao.schema.dropIfExists >> entityDao.schema.create)

  Http().bindAndHandle(route, "localhost", 8080)
}
