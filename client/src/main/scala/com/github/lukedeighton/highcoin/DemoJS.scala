package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.{Blockchain, Wallet}
import org.scalajs.dom
import scala.concurrent.ExecutionContext.Implicits.global
import com.github.lukedeighton.highcoin.shared.JsContext.jsContext

object DemoJS {

  implicit var blockchain: Blockchain = _
  var wallet: Wallet = _

  def main(args: Array[String]): Unit = {
    val highcoinService = new HighCoinService(host = "http://localhost:9000")
    for {
      blockchain <- highcoinService.getBlockchain
      wallet <- highcoinService.createWallet
    } yield {
      this.blockchain = blockchain
      this.wallet = wallet
      updateWallet(wallet)
    }
  }

  def updateWallet(wallet: Wallet): Unit = {
    this.wallet = wallet
    dom.document.getElementById("address").textContent = wallet.address
    dom.document.getElementById("balance").textContent = wallet.balance.toString()
  }

//  def downloadWallet(): Unit = { //TODO this should be created client side
//    wallet = Wallet.create()
//  }
//
//  def updateBalance(): Unit = {
//    dom.document.getElementById("balance").textContent = wallet.balance.toString()
//  }
//
//  def downloadBlockchain(): Unit = {
//    val xhr = new dom.XMLHttpRequest()
//    xhr.open("GET", "http://localhost:9000/chain")
//    xhr.onload = { (e: dom.Event) =>
//      if (xhr.status == 200) {
//        val responseBody = xhr.responseText
//        dom.console.log(responseBody)
//        blockchain = read[Blockchain](responseBody)
//        reloadBlockchainInfo()
//      }
//    }
//    xhr.send()
//  }
//
//  def reloadBlockchainInfo(): Unit = {
//    val blockchainContainer = dom.document.getElementById("blockchain")
//    blockchainContainer.textContent = blockchain.toString
//
//  }

}
