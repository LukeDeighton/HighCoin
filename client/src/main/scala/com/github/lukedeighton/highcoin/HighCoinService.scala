package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.{Blockchain, Wallet}
import org.scalajs.dom

import upickle.default._
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.{Promise, Thenable, |}

import ReadWriters._

class HighCoinService(host: String) {

  def createWallet: Future[Wallet] = get[Wallet]("/wallet/new")

  def getBlockchain: Future[Blockchain] = get[Blockchain]("/chain")

  private def get[T : Reader](path: String): Future[T] = makeRequest[T]("GET", path)

  private def makeRequest[T : Reader](method: String, path: String): Future[T] = {
    val url = host + path
    val xhr = new dom.XMLHttpRequest()
    xhr.open(method, url)
    val promise = new Promise[T]({
      (resolve: js.Function1[T | Thenable[T], _], reject: js.Function1[scala.Any, _]) =>
        xhr.onload = { (e: dom.Event) =>
          if (xhr.status >= 200 && xhr.status <= 299) {
            dom.console.log(s"successfuly response to $method $url")
            val responseBodyStr = xhr.responseText
            val responseBody = read[T](responseBodyStr)
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
