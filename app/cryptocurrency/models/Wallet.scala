package cryptocurrency.models

import cryptocurrency.Blockchain
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
    val signature = "Signature" //TODO
    val unspentPairs = blockchain.getUnspentTransactionOutputPairs(address)
    val balance = unspentPairs.collect { case (_, output) => output.value }.sum

    if (balance < value) throw new IllegalStateException("Insufficient funds to send transaction")

//    var spentValue = 0
    val (transaction, unspentOutput) = unspentPairs.head //TODO
    val outputIndex = transaction.outputs.indexOf(unspentOutput)
    val outputRef = Transaction.OutputRef(transaction.hash.hex, outputIndex)

    Transaction(
      inputs = Seq(Transaction.Input(signature, outputRef)),
      outputs = Seq(Transaction.Output(value, recipientAddress))
    )
  }
}
