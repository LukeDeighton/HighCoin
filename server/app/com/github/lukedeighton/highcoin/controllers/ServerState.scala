package com.github.lukedeighton.highcoin.controllers

import com.github.lukedeighton.highcoin.shared.Blockchain

object ServerState {

  implicit var blockchain: Blockchain = Blockchain.empty
}
