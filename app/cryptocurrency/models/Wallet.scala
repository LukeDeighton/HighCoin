package cryptocurrency.models

import cryptocurrency.Blockchain
import cryptocurrency.models.Transaction.InputOutputs
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



    val transaction = spendTransactionOutput()

    Transaction(
      inputs = Seq(
        Transaction.Input(
          signature,
          outputRef = Transaction.OutputRef(spendableTransaction.hash.hex, 0)
        )),
      outputs = Seq(
        Transaction.Output(value, recipientAddress)
      )
    )
    ???
  }

  def spendTransactionOutput(spendValue: BigDecimal, transaction: Transaction, outputIndex: Int): InputOutputs = {
    val output = transaction.getOutput(outputIndex)
    output.value match {
      case value if value < spendValue  => throw new IllegalArgumentException(s"Transaction output too small to spend value")
      case value if value == spendValue => //spend completely
      case _                            => //spend partially - need to send back money - does this involve signing now?
    }
  }
}
