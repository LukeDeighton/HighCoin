package com.github.lukedeighton.highcoin.controllers

case class SendRequest(recipientAddress: String, value: BigDecimal)
