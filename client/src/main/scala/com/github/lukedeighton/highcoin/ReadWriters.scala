package com.github.lukedeighton.highcoin

import java.util.Date

import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Transaction}
import upickle.default.{ReadWriter => RW}
import upickle.{Js, default}

object ReadWriters {

  implicit def dateRW: RW[Date] = RW(date => Js.Str(date.toString), {
    case jsDate => new Date(jsDate.str.toString)
  })

  implicit def transactionOutputRW: RW[Transaction.Output] =
    RW(default.macroW[Transaction.Output].write, transactionOutputRead)

  def transactionOutputRead: PartialFunction[Js.Value, Transaction.Output] = { // doesn't like BigDecimal
    case outputObj: Js.Obj =>
      val output = outputObj.value.toMap
      val value = BigDecimal(output("value").num.toString)
      val address = output("address").str.toString
      Transaction.Output(value, address)
  }

  implicit def transactionOutputRefRW: RW[Transaction.OutputRef] = default.macroRW

  implicit def transactionInputRW: RW[Transaction.Input] = default.macroRW

  implicit def transactionRW: RW[Transaction] = default.macroRW

  implicit def blockRW: RW[Block] = RW(default.macroW[Block].write, blockRead)

  def blockRead: PartialFunction[Js.Value, Block] = { //All because I couldn't work out Options
    case blockObj: Js.Obj =>
      val block = blockObj.value.toMap
      val height = block("height").num.asInstanceOf[Int]
      val nonce = block("nonce").num.asInstanceOf[Int]
      val transactions = block("transactions").arr.map(transactionRW.read)
      val previousHash = block.get("previousHash").map(_.str.toString)
      val timestamp = dateRW.read(block("timestamp"))
      Block(height, nonce, transactions, previousHash, timestamp)
  }

  implicit def blockchainRW: RW[Blockchain] = default.macroRW

}
