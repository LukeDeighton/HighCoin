package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Transaction, Wallet}
import org.scalajs.dom

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.{Promise, Thenable, |}
import UPickleJsonSerialisers._

class HighCoinClient(host: String) {

  def createWallet: Future[Wallet] = get[Wallet]("/wallet/new")

  def getBlockchain: Future[Blockchain] = get[Blockchain]("/chain")

  def broadcastNextBlock(block: Block): Future[Blockchain] = post[Block, Blockchain]("/block/next", block)

  def sendTransaction(transaction: Transaction): Future[Blockchain] = post[Transaction, Blockchain]("/transaction/new", transaction)

  private def get[T : Reader](path: String): Future[T] = makeRequest[Unit, T]("GET", path)

  private def post[Req : Writer, Res : Reader](path: String, body: Req): Future[Res] =
    makeRequest[Req, Res]("POST", path, Some(body))

  private def makeRequest[Req : Writer, Res : Reader](method: String, path: String, bodyOpt: Option[Req] = None): Future[Res] = {
    val url = host + path
    val xhr = new dom.XMLHttpRequest()
    xhr.open(method, url)
    val promise = new Promise[Res]({
      (resolve: js.Function1[Res | Thenable[Res], _], reject: js.Function1[scala.Any, _]) =>
        xhr.onload = { (e: dom.Event) =>
          if (xhr.status >= 200 && xhr.status <= 299) {
            dom.console.log(s"Successful response from $method $url")
            val responseBodyStr = xhr.responseText
            val responseBody = read[Res](responseBodyStr)
            dom.console.log(responseBodyStr)
            resolve(responseBody)
          } else {
            val errorMsg = s"request $method $url failed with status: ${xhr.status}"
            reject(errorMsg)
          }
        }
    })
    bodyOpt match {
      case Some(body) =>
        xhr.setRequestHeader("Content-type", "application/json")
        val bodyJson = write(body)
        dom.console.log("sending json body:")
        dom.console.log(bodyJson)
        xhr.send(bodyJson)
      case None =>
        xhr.send()
    }
    promise.toFuture
  }
}
