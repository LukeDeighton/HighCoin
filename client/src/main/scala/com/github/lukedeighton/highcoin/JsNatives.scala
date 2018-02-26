package com.github.lukedeighton.highcoin

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.typedarray.Uint8Array

@js.native
@JSGlobal
class TextEncoder() extends js.Object {

  def encode(str: String): Uint8Array = js.native
}

@js.native
@JSGlobal("sha256")
object Sha256 extends js.Object {

  def digest(str: String): Uint8Array = js.native
}