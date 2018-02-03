package cryptocurrency.models

case class Transaction(inputs: Seq[Transaction.Input],
                       outputs: Seq[Transaction.Output]) {

  lazy val serialise: String = {
    val inputHashStr = inputs.size + inputs.foldLeft("")(_ + _.serialise)
    val outputHashStr = outputs.size + outputs.foldLeft("")(_ + _.serialise)
    inputHashStr + outputHashStr
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

  def createCoin(value: BigDecimal, address: String): Transaction =
    Transaction(
      inputs = Seq.empty,
      outputs = Seq(Output(value, address)))

  case class Input(signature: String,
                   outputRef: OutputRef) {

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
