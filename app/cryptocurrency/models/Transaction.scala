package cryptocurrency.models

import scala.util.Random

case class Transaction(inputs: Seq[Transaction.Input],
                       outputs: Seq[Transaction.Output],
                       nonce: Int) extends TransactionOps {

  lazy val serialise: String = {
    val inputHashStr = inputs.size + inputs.foldLeft("")(_ + _.serialise)
    val outputHashStr = outputs.size + outputs.foldLeft("")(_ + _.serialise)
    inputHashStr + outputHashStr + nonce
  }

  lazy val hash: Hash256 = Sha256.digest(serialise)

  def getOutput(index: Int): Transaction.Output =
    outputs
      .lift(index)
      .getOrElse(throw new IllegalArgumentException("invalid output index"))

  def getOutputs(address: String): Seq[Transaction.Output] =
    outputs.filter(_.address == address)
}

object Transaction {

  private val random = new Random()
  private def randomNonce = random.nextInt(Integer.MAX_VALUE)

  def createCoin(value: BigDecimal, address: String): Transaction =
    Transaction(
      inputs = Seq.empty,
      outputs = Seq(Output(value, address)),
      nonce = randomNonce)

  def apply(inputs: Seq[Transaction.Input],
            outputs: Seq[Transaction.Output]): Transaction = {
    this(inputs, outputs, randomNonce)
  }

  case class Input(signature: String,
                   outputRef: OutputRef) extends TransactionInputOps {

    lazy val serialise: String = signature + outputRef.serialise
  }

  case class OutputRef(hash: String,
                       outputIndex: Int) {

    lazy val serialise: String = hash + outputIndex
  }

  case class Output(value: BigDecimal,
                    address: String) {

    lazy val serialise: String = value + address
  }
}
