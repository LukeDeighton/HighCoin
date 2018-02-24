package com.github.lukedeighton.highcoin

import java.util.Date

import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Transaction, Wallet}
import upickle.default.{ReadWriter => RW}
import upickle.{Js, default}

object ReadWriters {

  implicit def walletRW: RW[Wallet] = RW(default.macroW[Wallet].write, readWallet)

  def readWallet: PartialFunction[Js.Value, Wallet] = {
    case walletObj: Js.Obj =>
      val wallet = walletObj.value.toMap
      val signingKey = wallet.get("signingKey").map(_.str.toString)
      val address = wallet("address").str.toString
      Wallet(signingKey, address)
  }

  implicit def dateRW: RW[Date] = RW(date => Js.Str(date.toString), {
    case jsDate => new Date(jsDate.str.toString)
  })

  implicit def transactionOutputRW: RW[Transaction.Output] =
    RW(default.macroW[Transaction.Output].write, readTransactionOutput)

  def readTransactionOutput: PartialFunction[Js.Value, Transaction.Output] = { // doesn't like BigDecimal
    case outputObj: Js.Obj =>
      val output = outputObj.value.toMap
      val value = BigDecimal(output("value").num.toString)
      val address = output("address").str.toString
      Transaction.Output(value, address)
  }

  implicit def transactionOutputRefRW: RW[Transaction.OutputRef] = default.macroRW

  implicit def transactionInputRW: RW[Transaction.Input] = default.macroRW

  implicit def transactionRW: RW[Transaction] = default.macroRW

  implicit def blockRW: RW[Block] = RW(default.macroW[Block].write, readBlock)

  def readBlock: PartialFunction[Js.Value, Block] = { //All because I couldn't work out Options
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
