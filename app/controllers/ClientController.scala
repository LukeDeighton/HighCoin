package controllers

import javax.inject.{Inject, Singleton}

import cryptocurrency.services.MiningService.mineNextBlock
import cryptocurrency.Wallet
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import utils.Json.{as, asJson}
import utils.JsonWriteables._

@Singleton
class ClientController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  //TODO APIs that should run on client

  import ClientState.wallet
  import ServerState.blockchain

  def createWallet = Action {
    wallet = Wallet.create()
    Ok(asJson(wallet))
  }

  def getBalance(address: String) = Action {
    val balance = Wallet.create(address).balance
    Ok(balance.toString())
  }

  def mineBlock(address: String) = Action {
    val rewardValue = 25

    val nextBlock = mineNextBlock(address, rewardValue)

    blockchain = blockchain.addBlock(nextBlock)

    Ok(s"Successfully mined a block. Rewarding address: $address with $rewardValue highcoins")
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
