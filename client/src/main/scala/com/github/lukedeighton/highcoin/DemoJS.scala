package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.JsContext.jsContext
import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Wallet}
import org.scalajs.dom
import org.scalajs.dom.raw.{Element, Event}

import scala.concurrent.ExecutionContext.Implicits.global

object DemoJS {

  implicit var blockchain: Blockchain = _

  var wallet: Wallet = _
  var client: HighCoinClient = _
  var miner: HighCoinMiner = _

  val mineButton: Element = dom.document.getElementById("mine")
  val reloadButton: Element = dom.document.getElementById("reload-blockchain")
  val hashesSpan: Element = dom.document.getElementById("hashes")

  def main(args: Array[String]): Unit = {
    client = new HighCoinClient(host = "http://localhost:9000")
    miner = new HighCoinMiner
    for {
      blockchain <- client.getBlockchain
      wallet <- client.createWallet //TODO create clientside rather than call to server
    } yield {
      this.wallet = wallet
      handleBlockchainChange(blockchain)
      addClickListeners()
    }
    dom.window.setInterval(() => renderHashesPerSecond(), 500)
  }

  def addClickListeners(): Unit = {
    reloadButton.addEventListener("click", reloadBlockchain)
    mineButton.addEventListener("click", toggleMining)
  }

  def reloadBlockchain(event: Event): Unit =
    client.getBlockchain.foreach(handleBlockchainChange)

  private def handleBlockchainChange(blockchain: Blockchain): Unit = {
    this.blockchain = blockchain
    updateWallet(wallet)
  }

  def updateWallet(wallet: Wallet): Unit = {
    this.wallet = wallet
    dom.document.getElementById("address").textContent = wallet.address
    dom.document.getElementById("balance").textContent = wallet.balance.toString()
  }

  private def renderHashesPerSecond(): Unit =
    hashesSpan.textContent = s"${miner.hashesPerSecond()} hashes / sec"

  private def toggleMining(event: Event): Unit =
    if (miner.isMining) {
      miner.cancelMining()
    } else {
      mineButton.textContent = "stop"
      miner.beginMining().foreach {
        case Some(nextBlock) => onMiningComplete(nextBlock)
        case None            => onMiningCancelled()
      }
    }

  private def onMiningCancelled(): Unit = {
    mineButton.textContent = "start"
  }

  private def onMiningComplete(block: Block): Unit = {
    mineButton.textContent = "start"
    client.broadcastNextBlock(block).foreach(handleBlockchainChange)
  }
}
