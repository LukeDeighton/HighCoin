package controllers

import javax.inject._

import cryptocurrency.{Blockchain, ProofOfWork}
import models.{BlockchainResponse, Transaction, TransactionRequest, Wallet}
import play.api.mvc._
import utils.Json._
import utils.JsonWriteables._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit var blockchain: Blockchain = Blockchain(
    unboundTransactions = Seq.empty,
    chain = Seq.empty)

  def generateWallet = Action {
    Ok(asJson(Wallet.create()))
  }

  def newTransaction: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val jsonBodyStr = request.body.asJson.get.toString()
    val txRequest = as[TransactionRequest](jsonBodyStr)

    val wallet = Wallet.create(txRequest.senderAddress)
    if (txRequest.value > wallet.getBalance) {
      return UnprocessableEntity("Insufficient funds")
    }

    val transactions = wallet.getSpendableTransactions
    transactions.headOption match {
      case Some(transaction) =>
        val transferTx = Transaction(
          inputs = Seq(
            Transaction.Input(
              txRequest.senderSignature,
              outputRef = Transaction.OutputRef(transaction.hash().toHex, 0)
            )),
          outputs = Seq(
            Transaction.Output(txRequest.value, txRequest.recipientAddress)
          )
        )

        blockchain = blockchain.addTransaction(transferTx)

        Ok("Transaction will be added to block index: " + blockchain.nextBlockIndex)
      case None =>
        NotImplemented("Invalid spendable transaction")
    }
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

  def getBlockchain = Action {
    val response = BlockchainResponse(blockchain)
    Ok(asJson(response))
  }
}
