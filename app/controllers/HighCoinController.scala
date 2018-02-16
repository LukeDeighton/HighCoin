package controllers

import javax.inject._

import cryptocurrency.Transaction
import play.api.mvc._
import utils.Json._
import utils.JsonWriteables._

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
    Ok(asJson(BlockchainResponse(blockchain)))
  }
}
