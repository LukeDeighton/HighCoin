package controllers

import javax.inject._

import cryptocurrency.Blockchain
import models.{BlockchainResponse, TransactionRequest, Wallet, Transaction => Tx}
import play.api.mvc._
import utils.Json._
import utils.JsonWriteables._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  var blockchain: Blockchain = Blockchain(
    unboundTransactions = Seq.empty,
    chain = Seq.empty)

  def generateWallet = Action {
    Ok(asJson(Wallet()))
  }

  def newTransaction = Action { implicit request: Request[AnyContent] =>
    val jsonBodyStr = request.body.asJson.get.toString()
    val transactionRequest = as[TransactionRequest](jsonBodyStr)
    val senderAddress = transactionRequest.senderAddress
//
//    val transactions = blockchain.chain
//      .flatMap(_.transactions)
//      .filter {
//        case Tx.CoinCreation(_, address) => address == senderAddress
//        case Tx.CoinTransfer(_, outputs) => outputs.exists(_.address == senderAddress)
//      }
//
//    val transaction = Tx.CoinTransfer(
//      inputs = Seq(
//        Tx.Input(
//          transactionRequest.senderSignature,
//          previousOutput = Tx.OutputRef()
//        )),
//      outputs = Seq.empty
//    )
//    transactionRequest.
//
//    blockchain = blockchain.addTransaction(transaction)
    Ok("Transaction will be added to block index: " + blockchain.nextBlockIndex)
  }

  def mine(address: String) = Action {
    val rewardAmount = 25

    val nextBlock = blockchain
      .createBlock(nonce = 0)
      .withTransaction(Tx.CoinCreation(
        address = address,
        value = rewardAmount))

    val validNextBlock = blockchain.calculateProofOfWork(nextBlock)

    blockchain = blockchain.addBlock(validNextBlock)

    Ok(s"Successfully mined a block. Rewarding address: $address with $rewardAmount highcoins")
  }

  def getBlockchain = Action {
    val response = BlockchainResponse(blockchain)
    Ok(asJson(response))
  }
}
