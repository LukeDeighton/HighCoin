package com.github.lukedeighton.highcoin.shared

object ProofOfWork {

  def calculate(unproved: Block)
               (implicit context: ScalaJsContext): Block = {
    var block = unproved
    var nonce = 0
    while (!isValidProof(block)) {
      nonce += 1
      block = block.withNonce(nonce)
    }
    block
  }

  def isValidProof(block: Block)
                  (implicit context: ScalaJsContext): Boolean =
    block.hash.hex.startsWith("0000")
}
