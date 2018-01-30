package cryptocurrency

import models.{Block, Transaction, Transaction => Tx}

case class Blockchain(unboundTransactions: Seq[Transaction], chain: Seq[Block]) {

  def nextBlockIndex: Int = chain.size

  def lastBlock: Option[Block] = chain.lastOption

  def createBlock(nonce: Int): Block =
    Block(
      index = nextBlockIndex,
      transactions = unboundTransactions,
      nonce = nonce,
      previousHash = lastBlock.map(_.hash.toHex)
    )

  def addBlock(nonce: Int): Blockchain =
    addBlock(createBlock(nonce))

  def addBlock(block: Block): Blockchain =
    Blockchain(Seq.empty, this.chain :+ block)

  def addTransaction(transaction: Transaction): Blockchain =
    this.copy(unboundTransactions :+ transaction)

  val allTransactions: Seq[Transaction] = chain.flatMap(_.transactions)

  val allTxOutputs: Seq[Tx.Output] = allTransactions.flatMap(_.outputs)

  val allTxInputs: Seq[Tx.Input] = allTransactions.flatMap(_.inputs)
}