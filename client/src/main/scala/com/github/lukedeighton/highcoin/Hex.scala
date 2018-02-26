package com.github.lukedeighton.highcoin

import scala.scalajs.js.typedarray.Uint8Array

object Hex {

  def encode(bytes: Uint8Array): String =
    bytes.foldLeft("") { case (hex, byte) =>
      val byteHex = byte.toHexString
      val leadingZeroHex =
        if (byteHex.length == 1) "0" + byteHex
        else byteHex
      hex + leadingZeroHex
    }

  def decode(hex: String): Uint8Array = ???
}
