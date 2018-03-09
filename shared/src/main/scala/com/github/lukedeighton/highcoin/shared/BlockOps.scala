package com.github.lukedeighton.highcoin.shared

trait BlockOps { block: Block =>

  def validate()(implicit blockchain: Blockchain, context: ScalaJsContext): Unit = {
    validatePreviousBlock()
    validateProof()
    validateTransactions()
  }

  private def validatePreviousBlock()(implicit blockchain: Blockchain, context: ScalaJsContext): Unit = {
    if (blockchain.chain.size > block.height)
      throw new IllegalStateException("Block height incorrect")
    block.previousHash match {
      case Some(hash) =>
        if (blockchain.chain(block.height - 1).hash.hex != hash)
          throw new IllegalStateException("Previous hash doesn't match previous block hash")
      case None =>
        if (blockchain.chain.nonEmpty)
          throw new IllegalStateException("Excepted previous hash")
    }
  }

  private def validateProof()(implicit context: ScalaJsContext): Unit =
    if (!ProofOfWork.isValidProof(block))
      throw new IllegalStateException("Proof is not valid")

  private def validateTransactions()(implicit blockchain: Blockchain, context: ScalaJsContext): Unit =
    transactions.foreach(_.validate)
}
