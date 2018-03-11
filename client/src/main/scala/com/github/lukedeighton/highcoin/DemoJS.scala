package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.JsContext.jsContext
import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Wallet}
import org.scalajs.dom
import org.scalajs.dom.raw.{Element, Event, HTMLInputElement}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object DemoJS {

  implicit var blockchain: Blockchain = _

  var wallet: Wallet = _
  var client: HighCoinClient = _
  var miner: HighCoinMiner = _

  val mineButton: Element = dom.document.getElementById("mine")
  val reloadButton: Element = dom.document.getElementById("reload-blockchain")
  val hashesSpan: Element = dom.document.getElementById("hashes")
  val blockchainDiv: Element = dom.document.getElementById("blockchain")
  val sendAddressInput: HTMLInputElement = dom.document.getElementById("send_address").asInstanceOf[HTMLInputElement]
  val sendAmountInput: HTMLInputElement = dom.document.getElementById("send_amount").asInstanceOf[HTMLInputElement]
  val sendButton: Element = dom.document.getElementById("send")
  val addressSpan: Element = dom.document.getElementById("address")
  val balanceSpan: Element = dom.document.getElementById("balance")

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

  private def addClickListeners(): Unit = {
    reloadButton.addEventListener("click", reloadBlockchain)
    mineButton.addEventListener("click", toggleMining)
    sendButton.addEventListener("click", sendTransaction)
  }

  private def reloadBlockchain(event: Event): Unit =
    client.getBlockchain.foreach(handleBlockchainChange)

  private def handleBlockchainChange(blockchain: Blockchain): Unit = {
    this.blockchain = blockchain
    updateWallet(wallet)
    renderBlockchain()
  }

  private def updateWallet(wallet: Wallet): Unit = {
    this.wallet = wallet
    addressSpan.textContent = wallet.address
    balanceSpan.textContent = wallet.balance.toString()
  }

  private def sendTransaction(event: Event): Unit = {
    val sendAddress = sendAddressInput.value
    val sendAmount: Double = Try(sendAmountInput.value.toDouble).getOrElse(0)
    if (sendAddress.isEmpty || sendAmount <= 0 || sendAmount > wallet.balance) {
      dom.console.log("invalid inputs - cannot send transaction")
      return
    }

    val transaction = wallet.send(sendAddress, sendAmount)
    client.sendTransaction(transaction).foreach(handleBlockchainChange)
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

  private def renderBlockchain(): Unit = {
    removeChildren(blockchainDiv)
    if (blockchain.blocks.isEmpty) return

    val blockPadding = 15
    val transactionPadding = 10
    val blockWidth = 300  //TODO based on number of transactions?
    val transactionRadius = 40
    val canvasHeight = blockWidth
    val canvasWidth = blockchain.blocks.size * (blockWidth + blockPadding)

    val params = Params(canvasWidth, canvasHeight)
    val two = new Two(params).appendTo(blockchainDiv)

    blockchain.blocks.foreach { block =>
      val xOffset = block.height * (blockWidth + blockPadding) + blockPadding
      val blockRect = two.makeRectangle(xOffset + blockWidth / 2, blockWidth / 2, blockWidth, blockWidth)

      blockRect.fill = "rgb(0, 200, 255)"
      blockRect.opacity = 0.75
      blockRect.noStroke()

      block.previousHash.foreach { previousHash =>
        val message = s"Previous Hash: 0x${previousHash.substring(0, 8)}..."
        val styles = Styles(size = 19, fill = "#FFFFFF", weight = 700)
        two.makeText(message, xOffset + blockWidth / 2, 25, styles)
      }

      val transactionCount = block.transactions.size
      block.transactions.foreach { transaction =>
        val minX = xOffset + transactionRadius + transactionPadding
        val maxX = xOffset + blockWidth - transactionRadius - transactionPadding
        val transactionCircle = two.makeCircle(minX, blockWidth / 2, transactionRadius)

        transactionCircle.fill = "#FF8000"
        transactionCircle.stroke = "orangered"
        transactionCircle.linewidth = 5
      }
    }

    two.update()
  }

  private def removeChildren(element: Element): Unit =
    while (element.firstChild != null) {
      element.removeChild(element.firstChild)
    }
}
