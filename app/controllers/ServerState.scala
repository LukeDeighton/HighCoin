package controllers

import cryptocurrency.Blockchain

object ServerState {

  implicit var blockchain: Blockchain = Blockchain.empty
}
