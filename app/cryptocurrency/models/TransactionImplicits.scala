package cryptocurrency.models

object TransactionImplicits {

  implicit class TransactionInputs(inputs: Seq[Transaction.Input]) {

    def price(implicit blockchain: Blockchain): BigDecimal = {
      inputs.map(_.transactionOutput).flatMap(_.map(_.value)).sum
    }
  }

  implicit class TransactionOutputs(outputs: Seq[Transaction.Output]) {

    def price: BigDecimal = {
      outputs.map(_.value).sum
    }
  }

  implicit class TransactionOutputPairs(outputPairs: Seq[TransactionOutputPair]) {

    def price: BigDecimal = {
      outputPairs.map(_.output).price
    }
  }
}
