package services

import javax.inject.{Inject, Singleton}
import com.typesafe.config.ConfigFactory
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

@Singleton
class GithubService @Inject()(ws: WSClient)(implicit ex: ExecutionContext) {

  lazy val logger: Logger = Logger(this.getClass())

  def getGithubApi = {
    logger.info(s"getGithubApi")
    ws.url(ConfigFactory.load().getString("GITHUB_API_URL")).addHttpHeaders("Authorization" -> s"token ${ConfigFactory.load().getString("GITHUB_TOKEN")}").get()
  }

  def getUsers(username: String) = {
    logger.info(s"getUsers")
    logger.info(s"GITHUB_API_URL -> ${ConfigFactory.load().getString("GITHUB_API_URL")}")
    logger.info(s"GITHUB_USERS_PATH -> ${ConfigFactory.load().getString("GITHUB_USERS_PATH").replace(":username", username)}")
    ws.url(ConfigFactory.load().getString("GITHUB_API_URL") + ConfigFactory.load().getString("GITHUB_USERS_PATH").replace(":username", username)).get()
  }

}
