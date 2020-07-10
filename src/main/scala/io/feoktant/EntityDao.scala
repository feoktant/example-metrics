package io.feoktant

import java.time.ZonedDateTime
import java.util.UUID

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{TableQuery, Tag}

object EntityDao {
  lazy val entityDao = TableQuery[EntityTable]

  case class Entity(id: UUID,
                    title: String,
                    count: Int,
                    done: Boolean,
                    createdAt: ZonedDateTime,
                   )

  class EntityTable(tag: Tag) extends Table[Entity](tag, "entity") {
    def id        = column[UUID]("id", O.PrimaryKey)
    def title     = column[String]("title")
    def count     = column[Int]("count")
    def done      = column[Boolean]("done")
    def createdAt = column[ZonedDateTime]("createdAt")

    def * = (id, title, count, done, createdAt).mapTo[Entity]
  }
}


