package com.github.lukedeighton.highcoin.controllers

import javax.inject._

import com.github.lukedeighton.highcoin.ServerState
import com.github.lukedeighton.highcoin.shared.{Block, Transaction}
import com.github.lukedeighton.highcoin.utils.Json._
import com.github.lukedeighton.highcoin.utils.JsonWriteables._
import play.api.mvc._
import com.github.lukedeighton.highcoin.ScalaContext.scalaContext

@Singleton
class HighCoinController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  import ServerState.blockchain

  def addTransaction = Action { req: Request[AnyContent] =>
    val jsonBodyStr = req.body.asJson.get.toString()
    val transaction = as[Transaction](jsonBodyStr)
    //TODO validate transaction
    blockchain = blockchain.addTransaction(transaction)
    Ok("Transaction will be added to block index: " + blockchain.nextBlockIndex)
  }

  def getBlockchain = Action {
    Ok(asJson(blockchain))
  }

  def receiveBlock = Action { req: Request[AnyContent] =>
    val jsonBodyStr = req.body.asJson.get.toString()
    val block = as[Block](jsonBodyStr)
    //TODO validate block & hash
    Ok("Received Response")
  }
}
