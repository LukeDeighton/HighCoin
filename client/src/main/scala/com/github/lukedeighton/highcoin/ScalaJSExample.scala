package com.github.lukedeighton.highcoin

import org.scalajs.dom

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = "test"
  }
}
