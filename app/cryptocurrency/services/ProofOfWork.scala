package cryptocurrency.services

import cryptocurrency.Block

object ProofOfWork {

  def calculate(unproved: Block): Block = {
    var block = unproved
    var nonce = 0
    while (!isValidProof(block)) {
      nonce += 1
      block = block.withNonce(nonce)
    }
    block
  }

  def isValidProof(block: Block): Boolean =
    block.hash.hex.startsWith("0000")
}
