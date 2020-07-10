package io.feoktant

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import io.feoktant.EntityDao.entityDao
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object App extends App with Route {

  private val log: Logger = LoggerFactory.getLogger("io.feoktant.App")
  implicit val system: ActorSystem = ActorSystem("App")
  implicit val ec: ExecutionContext = system.dispatcher

  val db = Database.forConfig("slick")

  def createSchema(db: Database): Future[Unit] =
    db.run(entityDao.schema.dropIfExists >> entityDao.schema.create).recoverWith { e =>
        log.error("Could not make schema, try again in 5 seconds", e)
        Thread.sleep(5000)
        createSchema(db)
    }

  for {
    _ <- createSchema(db)
    _ <- Http().bindAndHandle(route, "0.0.0.0", 8080)
  } yield ()

}
