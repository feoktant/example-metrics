package io.feoktant

import java.time.ZonedDateTime
import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route => AkkaRoute}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.feoktant.EntityDao.{Entity, entityDao}
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.util.Random
import scala.util.control.NonFatal

trait Route extends FailFastCirceSupport {
  private val log: Logger = LoggerFactory.getLogger(classOf[Route])
  implicit val todoEncoder: Encoder[Entity] = deriveEncoder
  private val random = Random

  def db: Database

  val route: AkkaRoute =
    AkkaRoute.seal(
      pathEndOrSingleSlash {
        get {
          complete(logic)
        }
      }
    )

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case NonFatal(e) =>
        extractUri { uri =>
          log.error(s"Request to $uri could not be handled normally", e)
          complete(StatusCodes.InternalServerError, "Horrible disaster")
        }
    }

  def logic: Future[Seq[Entity]] = db.run {
    val entity = Entity(
      UUID.randomUUID(),
      random.alphanumeric.take(10).mkString,
      random.nextInt(),
      random.nextBoolean(),
      ZonedDateTime.now(),
    )

    (entityDao += entity) andThen entityDao.result
  }
}
