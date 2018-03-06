package com.github.lukedeighton.highcoin.shared

object ProofOfWork {

  def calculate(unproved: Block, nonceStart: Int = 0, nonceEnd: Int = Int.MaxValue)
               (implicit context: ScalaJsContext): Option[Block] = {
    var block = unproved
    var nonce = nonceStart
    while (nonce <= nonceEnd) {
      if (isValidProof(block)) return Some(block)

      nonce += 1
      block = block.withNonce(nonce)
    }
    None
  }

  private def isValidProof(block: Block)(implicit context: ScalaJsContext): Boolean =
    block.hash.hex.startsWith("00000")
}
