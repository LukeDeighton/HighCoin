package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Wallet}
import org.scalajs.dom
import upickle.default._

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.{Promise, Thenable, |}
import UPickleJsonSerialisers._

class HighCoinClient(host: String) {

  def createWallet: Future[Wallet] = get[Wallet]("/wallet/new")

  def getBlockchain: Future[Blockchain] = get[Blockchain]("/chain")

//  def broadcastBlock(block: Block): Future[String] = post("/block/next", block)

  private def get[T : Reader](path: String): Future[T] = makeRequest[Unit, T]("GET", path)

  private def post[Req : Writer, Res : Reader](path: String, body: Req): Future[Res] =
    makeRequest[Req, Res]("POST", path, Some(body))

  private def makeRequest[Req : Writer, Res : Reader](method: String, path: String, body: Option[Req] = None): Future[Res] = {
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
    xhr.send()
    promise.toFuture
  }
}
