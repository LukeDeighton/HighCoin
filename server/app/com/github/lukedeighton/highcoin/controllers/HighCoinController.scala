package com.github.lukedeighton.highcoin.controllers

import javax.inject._

import com.github.lukedeighton.highcoin.ScalaContext
import com.github.lukedeighton.highcoin.ServerState
import com.github.lukedeighton.highcoin.shared.{Block, Transaction}
import com.github.lukedeighton.highcoin.utils.Json._
import com.github.lukedeighton.highcoin.utils.JsonWriteables._
import play.api.mvc._

import scala.util.{Failure, Success, Try}

@Singleton
class HighCoinController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  import ServerState.blockchain
  import ScalaContext.scalaContext

  def getBlockchain = Action {
    Ok(asJson(blockchain))
  }

  def addTransaction = Action { req: Request[AnyContent] =>
    val jsonBodyStr = req.body.asJson.get.toString()
    val transaction = as[Transaction](jsonBodyStr)
    Try(transaction.validate()(blockchain, scalaContext)) match {
      case Success(_) =>
        blockchain = blockchain.addTransaction(transaction)
        Ok(asJson(blockchain))
      case Failure(ex) =>
        println(ex)
        UnprocessableEntity("Invalid transaction provided")
    }
  }

  def receiveBlock = Action { req: Request[AnyContent] =>
    val jsonBodyStr = req.body.asJson.get.toString()
    val block = as[Block](jsonBodyStr)
    Try(block.validate()) match {
      case Success(_) =>
        blockchain = blockchain.addBlock(block)
        Ok(asJson(blockchain))
      case Failure(ex) =>
        println(ex)
        UnprocessableEntity("Invalid block proof provided")
    }
  }
}
