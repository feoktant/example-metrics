package io.feoktant

import java.time.ZonedDateTime
import java.util.UUID

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route => AkkaRoute}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.feoktant.EntityDao.{Entity, entityDao}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.util.Random

trait Route extends FailFastCirceSupport {
  implicit val todoEncoder: Encoder[Entity] = deriveEncoder
  private val random = Random

  def db: Database

  val route: AkkaRoute =
    pathEndOrSingleSlash {
      get {
        complete(logic)
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
