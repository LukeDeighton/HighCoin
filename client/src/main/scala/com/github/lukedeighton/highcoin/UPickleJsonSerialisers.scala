package com.github.lukedeighton.highcoin

import java.util.Date

import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Transaction, Wallet}
import upickle.default.{ReadWriter => RW, Writer, Reader}
import upickle.{Js, default}

object UPickleJsonSerialisers {

  implicit def optionRW[T : RW]: RW[Option[T]] = RW({
    case Some(value) => implicitly[Writer[T]].write(value)
    case None        => Js.Null
  }, {
    case Js.Null => None
    case jsValue => Some(implicitly[Reader[T]].read(jsValue))
  })

  implicit def dateRW: RW[Date] = RW(date => Js.Str(date.toString), {
    case jsDate => new Date(jsDate.str.toString)
  })

  implicit def bigDecimalRW: RW[BigDecimal] = RW(value => Js.Num(value.doubleValue()), {
    case Js.Str(value) => BigDecimal(value.toString)
    case Js.Num(value) => BigDecimal(value)
  })

  implicit def walletRW: RW[Wallet] = default.macroRW

  implicit def transactionOutputRW: RW[Transaction.Output] = default.macroRW

  implicit def transactionOutputRefRW: RW[Transaction.OutputRef] = default.macroRW

  implicit def transactionInputRW: RW[Transaction.Input] = default.macroRW

  implicit def transactionRW: RW[Transaction] = default.macroRW

  implicit def blockRW: RW[Block] = RW(default.macroW[Block].write, readBlock)

  def readBlock: PartialFunction[Js.Value, Block] = { //Reading Options is broken
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
