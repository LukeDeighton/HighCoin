package com.github.lukedeighton.highcoin

import java.util.Date

import com.github.lukedeighton.highcoin.DemoJS.wallet
import com.github.lukedeighton.highcoin.shared.Block
import com.github.lukedeighton.highcoin.shared.MiningService.mineNextBlock
import org.scalajs.dom

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.{Promise, Thenable, |}

class HighCoinMiner {

  import DemoJS.blockchain
  import JsContext.jsContext

  private val nonceIncrementCount = 2000

  private var isMiningNextBlock: Boolean = false
  private var miningNonceStart: Int = 0

  private var lastHashCount: Int = 0
  private var lastHashTime: Long = 0

  def hashesPerSecond(): Long = {
    if (!isMiningNextBlock) return 0

    val currentHashTime = new Date().getTime
    val currentHashCount = miningNonceStart

    val hashCountDiff = currentHashCount - lastHashCount
    val timeDiff = currentHashTime - lastHashTime

    val hashesPerSec = hashCountDiff * 1000 / timeDiff

    lastHashTime = currentHashTime
    lastHashCount = currentHashCount

    hashesPerSec
  }

  def isMining: Boolean = isMiningNextBlock

  def cancelMining(): Unit = isMiningNextBlock = false

  def beginMining(): Future[Option[Block]] =
    new Promise[Option[Block]]({ (resolve: js.Function1[Option[Block] | Thenable[Option[Block]], _], reject: js.Function1[scala.Any, _]) =>
      isMiningNextBlock = true
      miningNonceStart = 0
      lastHashCount = 0
      lastHashTime = new Date().getTime
      beginMining(resolve, reject)
    }).toFuture

  private def beginMining(resolve: js.Function1[Option[Block] | Thenable[Option[Block]], _], reject: js.Function1[scala.Any, _]): Unit = {
    if (!isMiningNextBlock) {
      dom.console.log("Mining cancelled")
      resolve(None)
    } else {
      val nonceEnd = miningNonceStart + nonceIncrementCount
      dom.console.log(s"mining attempt $miningNonceStart - $nonceEnd")

      val nextBlockOpt = mineNextBlock(
        wallet.address,
        nonceStart = miningNonceStart,
        nonceEnd = nonceEnd)
      miningNonceStart = nonceEnd

      nextBlockOpt match {
        case Some(nextBlock) =>
          dom.console.log(s"found block with nonce: ${nextBlock.nonce} and hash: ${nextBlock.hash.hex}")
          isMiningNextBlock = false
          resolve(nextBlockOpt)
        case None =>  //TODO use web worker API?
          dom.window.setTimeout(() => beginMining(resolve, reject), 0)
      }
    }
  }
}
