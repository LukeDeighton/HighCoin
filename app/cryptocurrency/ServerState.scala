package cryptocurrency

object ServerState {

  implicit var blockchain: Blockchain = Blockchain(
    unboundTransactions = Seq.empty,
    chain = Seq.empty)
}
