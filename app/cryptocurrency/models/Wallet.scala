package cryptocurrency.models

import org.bitcoinj.core.{Base58, ECKey}

object Wallet {

  def create(): Wallet = {
    val key = new ECKey()
    val privateKey = Base58.encode(key.getPrivKeyBytes)
    val publicKey = Base58.encode(key.getPubKey)
    this(Some(privateKey), publicKey)
  }

  def create(address: String): Wallet = this(None, address)
}

case class Wallet(signingKey: Option[String], address: String) {

  def getBalance(implicit blockchain: Blockchain): BigDecimal =
    blockchain.getUnspentTransactionOutputs(address).map(_.value).sum

  def send(value: BigDecimal, recipientAddress: String)(implicit blockchain: Blockchain): Transaction = {
    val signature = s"$address Signature" //TODO
    val unspentPairs = blockchain.getUnspentTransactionOutputPairs(address)
    val balance = unspentPairs.collect { case (_, output) => output.value }.sum

    if (balance < value) throw new IllegalStateException("Insufficient funds to send transaction")

    var spentValue: BigDecimal = 0
    var inputs = Seq.empty[Transaction.Input]
    var outputs = Seq.empty[Transaction.Output]
    var pairIndex = 0
    while (spentValue < value) {
      val (transaction, unspentOutput) =
        unspentPairs
          .lift(pairIndex)
          .getOrElse(throw new IllegalStateException("Ran out of spendable outputs"))

      val remainingSpendValue = value - spentValue
      if (unspentOutput.value == remainingSpendValue) {

      }

      //TODO determine whether to completely spend, underspend etc.

      val outputIndex = transaction.outputs.indexOf(unspentOutput)
      val outputRef = Transaction.OutputRef(transaction.hash.hex, outputIndex)

      spentValue = spentValue + unspentOutput.value
      pairIndex = pairIndex + 1

      inputs = inputs :+ Transaction.Input(signature, outputRef)
      outputs = outputs :+ Transaction.Output(value, recipientAddress)
    }
    Transaction(inputs, outputs)
  }
}
