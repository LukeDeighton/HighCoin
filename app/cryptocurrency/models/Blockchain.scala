package cryptocurrency.models

//TODO mutable blockchain structure
case class Blockchain(unboundTransactions: Seq[Transaction], chain: Seq[Block]) extends BlockchainOps {

  def nextBlockIndex: Int = chain.size

  def lastBlock: Option[Block] = chain.lastOption

  def createBlock(nonce: Int): Block =
    Block(
      index = nextBlockIndex,
      transactions = unboundTransactions,
      nonce = nonce,
      previousHash = lastBlock.map(_.hash.hex)
    )

  def addBlock(nonce: Int): Blockchain =
    addBlock(createBlock(nonce))

  def addBlock(block: Block): Blockchain =
    Blockchain(Seq.empty, this.chain :+ block)

  def addTransaction(transaction: Transaction): Blockchain =
    this.copy(unboundTransactions :+ transaction)

  val allTransactions: Seq[Transaction] = chain.flatMap(_.transactions)

  val allTransactionOutputs: Seq[Transaction.Output] = allTransactions.flatMap(_.outputs)

  val allTransactionInputs: Seq[Transaction.Input] = allTransactions.flatMap(_.inputs)
}