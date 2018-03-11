package com.github.lukedeighton.highcoin.shared

import com.github.lukedeighton.highcoin.shared.MiningService.mineNextBlock
import org.scalatest.{FlatSpec, Matchers, OptionValues}
import com.github.lukedeighton.highcoin.ScalaContext.scalaContext

class WalletTest extends FlatSpec with Matchers with OptionValues {

  "Wallet" should "correctly calculate the balance" in {

    implicit var blockchain: Blockchain = Blockchain.empty

    val walletA = Wallet(signingKey = Some("SignKeyA"), address = "A")
    val walletB = Wallet(signingKey = Some("SignKeyB"), address = "B")
    val walletC = Wallet(signingKey = Some("SignKeyC"), address = "C")

    walletA.balance shouldBe 0
    walletB.balance shouldBe 0
    walletC.balance shouldBe 0

    var nextBlock = mineNextBlock(rewardAddress = "A").value
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 25
    walletB.balance shouldBe 0
    walletC.balance shouldBe 0

    var spendTransaction = walletA.send(recipientAddress = "C", 25)
    blockchain = blockchain.addTransaction(spendTransaction)

    nextBlock = mineNextBlock(rewardAddress = "A").value
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 25
    walletB.balance shouldBe 0
    walletC.balance shouldBe 25

    nextBlock = mineNextBlock(rewardAddress = "A").value
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 50
    walletB.balance shouldBe 0
    walletC.balance shouldBe 25

    spendTransaction = walletA.send(recipientAddress = "B", 15)
    blockchain = blockchain.addTransaction(spendTransaction)

    nextBlock = mineNextBlock(rewardAddress = "C").value
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 35
    walletB.balance shouldBe 15
    walletC.balance shouldBe 50

    spendTransaction = walletA.send(recipientAddress = "B", 34)
    blockchain = blockchain.addTransaction(spendTransaction)

    nextBlock = mineNextBlock(rewardAddress = "C").value
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 1
    walletB.balance shouldBe 49
    walletC.balance shouldBe 75

    spendTransaction = walletB.send(recipientAddress = "A", 49)
    blockchain = blockchain.addTransaction(spendTransaction)

    nextBlock = mineNextBlock(rewardAddress = "C").value
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 50
    walletB.balance shouldBe 0
    walletC.balance shouldBe 100
  }

}
