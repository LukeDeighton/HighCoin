package cryptocurrency

import cryptocurrency.models.Blockchain

object ServerState {

  implicit var blockchain: Blockchain = Blockchain.empty
}