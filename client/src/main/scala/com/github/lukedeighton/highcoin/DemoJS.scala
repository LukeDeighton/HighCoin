package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.JsContext.jsContext
import com.github.lukedeighton.highcoin.shared.MiningService.mineNextBlock
import com.github.lukedeighton.highcoin.shared.{Blockchain, Wallet}
import org.scalajs.dom
import org.scalajs.dom.crypto.{HashAlgorithm, crypto}
import org.scalajs.dom.raw.Event
import scala.scalajs.js

import scala.concurrent.ExecutionContext.Implicits.global

object DemoJS {

  implicit var blockchain: Blockchain = _

  var wallet: Wallet = _
  var client: HighCoinClient = _
  var isMining: Boolean = false

  def main(args: Array[String]): Unit = {
    client = new HighCoinClient(host = "http://localhost:9000")
    for {
      blockchain <- client.getBlockchain
      wallet <- client.createWallet //TODO create clientside rather than call to server
    } yield {
      this.wallet = wallet
      handleBlockchainChange(blockchain)
      addClickListeners()
    }
  }

  def addClickListeners(): Unit = {
    dom.document.getElementById("reload-blockchain")
      .addEventListener("click", reloadBlockchain)

    dom.document.getElementById("mine")
      .addEventListener("click", toggleMining)
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

  private def toggleMining(event: Event): Unit = {
    isMining = !isMining //TODO add way to cancel mining
    if (isMining)
      mineNextBlock(wallet.address)
  }
}
