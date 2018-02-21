package com.github.lukedeighton.highcoin.controllers

import javax.inject._

import com.github.lukedeighton.highcoin.ServerState
import com.github.lukedeighton.highcoin.shared.Transaction
import com.github.lukedeighton.highcoin.utils.Json._
import com.github.lukedeighton.highcoin.utils.JsonWriteables._
import play.api.mvc._

@Singleton
class HighCoinController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  import ServerState.blockchain

  def addTransaction = Action { implicit req: Request[AnyContent] =>
    val jsonBodyStr = req.body.asJson.get.toString()
    val transaction = as[Transaction](jsonBodyStr)
    //TODO validate transaction
    blockchain = blockchain.addTransaction(transaction)
    Ok("Transaction will be added to block index: " + blockchain.nextBlockIndex)
  }

  def getBlockchain = Action {
    Ok(asJson(blockchain))
  }
}
