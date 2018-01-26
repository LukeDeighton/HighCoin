package models

sealed trait Transaction {

  def serialise: String

  def hash(): Hash256
}

object Transaction {

  case class CoinCreation(value: BigDecimal, address: String) extends Transaction {

    def serialise: String = value + address

    def hash(): Hash256 = Sha256.digest(serialise)
  }

  case class CoinTransfer(inputs: Seq[Input],
                          outputs: Seq[Output]) extends Transaction {

    def serialise: String = {
      val inputHashStr = inputs.size + inputs.foldLeft("")(_ + _.serialise)
      val outputHashStr = outputs.size + outputs.foldLeft("")(_ + _.serialise)
      inputHashStr + outputHashStr
    }

    def hash(): Hash256 = Sha256.digest(serialise)
  }

  case class Input(signature: String,
                   previousOutput: OutputRef) {

    def serialise: String = signature + previousOutput.serialise
  }

  case class OutputRef(prevTxHash: Hash256,
                       outputIndex: Int) {

    def serialise: String = prevTxHash.toHex + outputIndex
  }

  case class Output(value: BigDecimal,
                    address: String) {

    def serialise: String = value + address
  }
}
