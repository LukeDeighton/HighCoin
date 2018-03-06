package com.github.lukedeighton.highcoin.shared

object MiningService {

  val rewardValue: BigDecimal = 25

  def mineNextBlock(rewardAddress: String, nonceStart: Int = 0, nonceEnd: Int = Int.MaxValue)
                   (implicit blockchain: Blockchain, context: ScalaJsContext): Option[Block] = {
    val nextBlock =
      blockchain
        .createBlock(nonce = 0)
        .addTransaction(
          Transaction.createCoin(rewardValue, rewardAddress))

    ProofOfWork.calculate(nextBlock, nonceStart, nonceEnd)
  }
}
