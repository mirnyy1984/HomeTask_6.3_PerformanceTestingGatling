package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class AtOnceUsersLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in/")
    .header("Accept", "application/json")

  def userCount: Int = 3

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

  val scn = scenario("At once users load simulation")
    .exec(getAllUsers())
    .pause(2)
    .exec(getSpecificUser())
    .pause(2)
    .exec(getAllUsers())

  val scn2 = scenario("At once users load simulation 2")
    .exec(getAllUsers())
    .pause(2)
    .exec(getSpecificUser())
    .pause(2)
    .exec(getAllUsers())

  setUp(
    scn.inject(
      atOnceUsers(100)
    ).protocols(httpConf),
    scn2.inject(
      atOnceUsers(100)
    ).protocols(httpConf)
  )
}
