package com.github.lukedeighton.highcoin

import java.util.Date

import com.github.lukedeighton.highcoin.shared.{Block, Blockchain, Transaction}
import upickle.default.{ReadWriter => RW}
import upickle.{Js, default}

object ReadWriters {

  implicit def date: RW[Date] = RW(date => Js.Str(date.toString), {
    case jsDate => new Date(jsDate.str.toString)
  })

  implicit def transactionOutputRW: RW[Transaction.Output] = default.macroRW

  implicit def transactionOutputRefRW: RW[Transaction.OutputRef] = default.macroRW

  implicit def transactionInputRW: RW[Transaction.Input] = default.macroRW

  implicit def transactionRW: RW[Transaction] = default.macroRW

  implicit def blockRW: RW[Block] = default.macroRW

  implicit def blockchainRW: RW[Blockchain] = default.macroRW

}
