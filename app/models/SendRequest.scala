package models

case class SendRequest(senderSignature: String, senderAddress: String, recipientAddress: String, value: BigDecimal)
