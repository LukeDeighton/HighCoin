package com.github.lukedeighton.highcoin.shared

//TODO mutable blockchain structure
case class Blockchain(unboundTransactions: Seq[Transaction], blocks: Seq[Block]) extends BlockchainOps {

  def nextBlockIndex: Int = blocks.size

  def lastBlock: Option[Block] = blocks.lastOption

  def createBlock(nonce: Int)
                 (implicit context: ScalaJsContext): Block =
    Block(
      index = nextBlockIndex,
      transactions = unboundTransactions,
      nonce = nonce,
      previousHash = lastBlock.map(_.hash.hex)
    )

  def addBlock(block: Block)
              (implicit context: ScalaJsContext): Blockchain =
    Blockchain(Seq.empty, this.blocks :+ block)

  def addTransaction(transaction: Transaction)
                    (implicit context: ScalaJsContext): Blockchain =
    this.copy(unboundTransactions :+ transaction)

  val allTransactions: Seq[Transaction] = blocks.flatMap(_.transactions)

  val allTransactionOutputs: Seq[Transaction.Output] = allTransactions.flatMap(_.outputs)

  val allTransactionInputs: Seq[Transaction.Input] = allTransactions.flatMap(_.inputs)
}

object Blockchain {

  val empty =
    Blockchain(
      unboundTransactions = Seq.empty,
      blocks = Seq.empty)
}