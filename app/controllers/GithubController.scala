package controllers

import javax.inject._
import play.api._
import play.api.http.HttpEntity
import play.api.mvc._
import services.GithubService
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GithubController @Inject()(val controllerComponents: ControllerComponents,
                                 val githubService: GithubService) extends BaseController {

  lazy val logger: Logger = Logger(this.getClass())

  def getGithub = Action.async { implicit request =>
    logger.info(s"getGithub")
    githubService.getGithubApi.map { response =>
      if (response.status == 200) {
        val contentType = response.headers
          .get("Content-Type")
          .flatMap(_.headOption)
          .getOrElse("application/octet-stream")

        response.headers.get("Content-Length") match {
          case Some(Seq(length)) =>
            Ok.sendEntity(HttpEntity.Streamed(response.bodyAsSource, Some(length.toLong), Some(contentType)))
          case _ =>
            Ok.chunked(response.bodyAsSource).as(contentType)
        }
      } else {
        BadGateway
      }
    }
  }

  def getUsers(username: String) = Action.async { implicit request =>
    logger.info(s"getUsers")
    githubService.getUsers(username).map { response =>
      if (response.status == 200) {
        val contentType = response.headers
          .get("Content-Type")
          .flatMap(_.headOption)
          .getOrElse("application/octet-stream")

        response.headers.get("Content-Length") match {
          case Some(Seq(length)) =>
            Ok.sendEntity(HttpEntity.Streamed(response.bodyAsSource, Some(length.toLong), Some(contentType)))
          case _ =>
            Ok.chunked(response.bodyAsSource).as(contentType)
        }
      } else {
        BadGateway
      }
    }
  }

}
