package com.github.lukedeighton.highcoin.controllers

import javax.inject.{Inject, Singleton}

import com.github.lukedeighton.highcoin.models.SendRequest
import com.github.lukedeighton.highcoin.shared.MiningService.mineNextBlock
import com.github.lukedeighton.highcoin.shared.{MiningService, Wallet}
import com.github.lukedeighton.highcoin.utils.Json.{as, asJson}
import com.github.lukedeighton.highcoin.utils.JsonWriteables._
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import com.github.lukedeighton.highcoin.{ClientState, ServerState}
import com.github.lukedeighton.highcoin.ScalaContext.scalaContext

@Singleton
class ClientController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  import ClientState.wallet
  import ServerState.blockchain

  //TODO not moved to the client until impl ECKey JS side
  def createWallet = Action {
    wallet = Wallet.create()
    Ok(asJson(wallet))
  }

  def getBalance(address: String) = Action {
    val balance = Wallet.create(address).balance
    Ok(balance.toString())
  }

  def mineBlock(address: String) = Action {
    val nextBlock = mineNextBlock(address)

    blockchain = blockchain.addBlock(nextBlock)

    Ok(s"Successfully mined a block. Rewarding address: $address with ${MiningService.rewardValue} highcoins")
  }

  def sendCoins = Action { implicit request: Request[AnyContent] =>
    val sendReq = as[SendRequest](request.body)

    if (sendReq.value > wallet.balance) {
      UnprocessableEntity("Insufficient funds")
    } else {
      val spendTransaction = wallet.send(sendReq.value, sendReq.recipientAddress)
      blockchain = blockchain.addTransaction(spendTransaction)
      Ok("Transaction will be added to block index: " + blockchain.nextBlockIndex)
    }
  }

}
