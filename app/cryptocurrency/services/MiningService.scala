package cryptocurrency.services

import cryptocurrency.{Block, Blockchain, Transaction}

object MiningService {

  def mineNextBlock(rewardAddress: String, rewardValue: BigDecimal)
                   (implicit blockchain: Blockchain): Block = {
    val nextBlock =
      blockchain
        .createBlock(nonce = 0)
        .withTransaction(
          Transaction.createCoin(rewardValue, rewardAddress))

    ProofOfWork.calculate(nextBlock)
  }
}
