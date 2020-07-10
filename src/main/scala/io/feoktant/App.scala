package io.feoktant

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import io.feoktant.EntityDao.entityDao
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object App extends App with Route {

  private val log: Logger = LoggerFactory.getLogger("io.feoktant.App")
  implicit val system: ActorSystem = ActorSystem("App")
  implicit val ec: ExecutionContext = system.dispatcher

  val db = Database.forConfig("slick")

  def createSchema(db: Database): Unit =
    db.run(entityDao.schema.dropIfExists >> entityDao.schema.create).onComplete {
      case Success(_) => log.info("Successfully created schema")
      case Failure(e) =>
        log.error("Could not make schema, try again in 5 seconds", e)
        Thread.sleep(5000)
        createSchema(db)
    }

  createSchema(db)
  Http().bindAndHandle(route, "localhost", 8080)
}
