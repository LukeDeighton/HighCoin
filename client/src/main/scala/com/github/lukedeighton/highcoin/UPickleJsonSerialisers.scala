package com.github.lukedeighton.highcoin

import java.util.Date

import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Transaction, Wallet}

object UPickleJsonSerialisers extends upickle.AttributeTagged {

  override implicit def OptionWriter[T: Writer]: Writer[Option[T]] =
    implicitly[Writer[T]].comap[Option[T]] {
      case None => null.asInstanceOf[T]
      case Some(x) => x
    }

  override implicit def OptionReader[T: Reader]: Reader[Option[T]] = {
    new Reader.Delegate[Any, Option[T]](implicitly[Reader[T]].map(Some(_))){
      override def visitNull(index: Int) = None
    }
  }

  implicit def dateRW: ReadWriter[Date] = readwriter[ujson.Value]
    .bimap(date => ujson.Str(date.toString), jsDate => new Date(jsDate.str))

  implicit def bigDecimalRW: ReadWriter[BigDecimal] = readwriter[ujson.Value].bimap(value => ujson.Num(value.doubleValue()), {
    case ujson.Str(value) => BigDecimal(value)
    case ujson.Num(value) => BigDecimal(value)
  })

  implicit def walletRW: ReadWriter[Wallet] = macroRW

  implicit def transactionOutputRW: ReadWriter[Transaction.Output] = macroRW

  implicit def transactionOutputRefRW: ReadWriter[Transaction.OutputRef] = macroRW

  implicit def transactionInputRW: ReadWriter[Transaction.Input] = macroRW

  implicit def transactionRW: ReadWriter[Transaction] = macroRW

  implicit def blockRW: ReadWriter[Block] = ReadWriter.join(blockR, macroW[Block])

  implicit def blockR: Reader[Block] = reader[ujson.Value].map { case blockObj: ujson.Obj =>
    val block = blockObj.value.toMap
    val height = block("height").num.asInstanceOf[Int]
    val nonce = block("nonce").num.asInstanceOf[Int]
    val transactions = block("transactions").arr.map(ujson.transform(_, transactionRW))
    val previousHash = block.get("previousHash").map(_.str)
    val timestamp = ujson.transform(block("timestamp"), dateRW)
    Block(height, nonce, transactions, previousHash, timestamp)
  }

  implicit def blockchainRW: ReadWriter[Blockchain] = macroRW
}