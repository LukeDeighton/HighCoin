package models

case class TransactionRequest(senderSignature: String, senderAddress: String, recipientAddress: String, value: BigDecimal)
