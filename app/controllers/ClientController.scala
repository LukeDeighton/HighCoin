package controllers

import javax.inject.{Inject, Singleton}

import cryptocurrency.{ServerState, ClientState}
import cryptocurrency.ProofOfWork
import cryptocurrency.models.{Transaction, Wallet}
import models.SendRequest
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import utils.Json.{as, asJson}
import utils.JsonWriteables._

@Singleton
class ClientController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  //TODO APIs that should run on client

  import ServerState.blockchain
  import ClientState.wallet

  def generateWallet = Action {
    wallet = Wallet.create()
    Ok(asJson(wallet))
  }

  def getBalance(address: String) = Action {
    val balance = Wallet.create(address).getBalance
    Ok(balance.toString())
  }

  def mine(address: String) = Action {
    val rewardAmount = 25

    val nextBlock =
      blockchain
        .createBlock(nonce = 0)
        .withTransaction(
          Transaction.createCoin(rewardAmount, address))
    val validNextBlock = ProofOfWork.calculate(nextBlock)
    blockchain = blockchain.addBlock(validNextBlock)

    Ok(s"Successfully mined a block. Rewarding address: $address with $rewardAmount highcoins")
  }

  def send = Action { implicit req: Request[AnyContent] =>
    val jsonBodyStr = req.body.asJson.get.toString()
    val request = as[SendRequest](jsonBodyStr)

    if (request.value > wallet.getBalance) {
      UnprocessableEntity("Insufficient funds")
    } else {
      val spendTransaction = wallet.send(request.value, request.recipientAddress)
      blockchain = blockchain.addTransaction(spendTransaction)
      Ok("Transaction will be added to block index: " + blockchain.nextBlockIndex)
    }
  }

}
