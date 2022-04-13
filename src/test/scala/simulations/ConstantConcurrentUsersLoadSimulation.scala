package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class ConstantConcurrentUsersLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in/")
    .header("Accept", "application/json")

  def userCount: Int = 4

  def getAllUsers() = {
    exec(
      http("Get all users")
        .get("/api/users?page=2")
        .check(status.is(200))
    )
  }

  def getSpecificUser() = {
    exec(
      http("Get specific user")
        .get(s"/api/users/${userCount}")
        .check(status.is(200))
    )
  }

  val scn = scenario("Constant concurrent users load simulation")
    .exec(getAllUsers())
    .pause(2)
    .exec(getSpecificUser())
    .pause(2)
    .exec(getAllUsers())

  setUp(
    scn.inject(
      constantConcurrentUsers(50) during (15)
    ).protocols(httpConf)
  )
}
