package cryptocurrency

import cryptocurrency.models.{Blockchain, Wallet}
import cryptocurrency.services.MiningService.mineNextBlock
import org.scalatest.{FlatSpec, Matchers}

class WalletTest extends FlatSpec with Matchers {

  "Wallet" should "correctly calculate the balance" in {

    implicit var blockchain: Blockchain = Blockchain.empty

    val walletA = Wallet(signingKey = Some("SignKeyA"), address = "A")
    val walletB = Wallet(signingKey = Some("SignKeyB"), address = "B")

    walletA.balance shouldBe 0
    walletB.balance shouldBe 0

    var nextBlock = mineNextBlock(rewardAddress = "A", rewardValue = 25)
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 25
    walletB.balance shouldBe 0

    nextBlock = mineNextBlock(rewardAddress = "A", rewardValue = 25)
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 50
    walletB.balance shouldBe 0

    var spendTransaction = walletA.send(15, recipientAddress = "B")
    blockchain = blockchain.addTransaction(spendTransaction)

    nextBlock = mineNextBlock(rewardAddress = "C", rewardValue = 25)
    blockchain = blockchain.addBlock(nextBlock)

    walletA.balance shouldBe 35
    walletB.balance shouldBe 15
  }

}
