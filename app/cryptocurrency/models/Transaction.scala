package cryptocurrency.models

import cryptocurrency.Blockchain

case class Transaction(inputs: Seq[Transaction.Input],
                       outputs: Seq[Transaction.Output]) {

  val serialise: String = {
    val inputHashStr = inputs.size + inputs.foldLeft("")(_ + _.serialise)
    val outputHashStr = outputs.size + outputs.foldLeft("")(_ + _.serialise)
    inputHashStr + outputHashStr
  }

  val hash: Hash256 = Sha256.digest(serialise)

  def getOutput(index: Int): Transaction.Output =
    outputs
      .lift(index)
      .getOrElse(throw new IllegalArgumentException("invalid output index"))
}

object Transaction {

  type InputOutputs = (Seq[Transaction.Input], Seq[Transaction.Output]) //TODO needed?

  def createCoin(value: BigDecimal, address: String): Transaction =
    Transaction(
      inputs = Seq.empty,
      outputs = Seq(Output(value, address)))

  case class Input(signature: String,
                   outputRef: OutputRef) {

    val serialise: String = signature + outputRef.serialise
  }

  case class OutputRef(hash: String,
                       outputIndex: Int) {

    val serialise: String = hash + outputIndex
  }

  case class Output(value: BigDecimal,
                    address: String) {

    val serialise: String = value + address
  }
}
