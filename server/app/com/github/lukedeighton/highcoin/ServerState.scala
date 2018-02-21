package com.github.lukedeighton.highcoin

import com.github.lukedeighton.highcoin.shared.Blockchain

object ServerState {

  implicit var blockchain: Blockchain = Blockchain.empty
}
