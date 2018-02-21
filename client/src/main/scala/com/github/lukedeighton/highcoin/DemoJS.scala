package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.Blockchain
import org.scalajs.dom
import upickle.default._
import ReadWriters._

object DemoJS {

  var blockchain: Blockchain = _

  def main(args: Array[String]): Unit = {
    downloadBlockchain()
  }

  def downloadBlockchain(): Unit = {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET", "http://localhost:9000/chain")
    xhr.onload = { (e: dom.Event) =>
      if (xhr.status == 200) {
        val responseBody = xhr.responseText
        dom.console.log(responseBody)
        blockchain = read[Blockchain](responseBody)
        reloadBlockchainInfo()
      }
    }
    xhr.send()
  }

  def reloadBlockchainInfo(): Unit = {
    val blockchainContainer = dom.document.getElementById("blockchain")
    blockchainContainer.textContent = blockchain.toString
  }
}
