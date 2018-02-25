package com.github.lukedeighton.highcoin.shared

object MiningService {

  val rewardValue: BigDecimal = 25

  def mineNextBlock(rewardAddress: String)
                   (implicit blockchain: Blockchain, context: ScalaJsContext): Block = {
    val nextBlock =
      blockchain
        .createBlock(nonce = 0)
        .addTransaction(
          Transaction.createCoin(rewardValue, rewardAddress))

    ProofOfWork.calculate(nextBlock)
  }
}
