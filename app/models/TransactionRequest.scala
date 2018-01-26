package models

case class TransactionRequest(senderSignature: String, senderAddress: String, recipientAddress: String, amount: BigDecimal)
