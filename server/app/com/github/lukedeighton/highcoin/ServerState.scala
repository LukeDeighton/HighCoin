package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.Blockchain
import com.github.lukedeighton.highcoin.shared.ScalaContext.scalaContext

object ServerState {

  implicit var blockchain: Blockchain = Blockchain.empty
}
