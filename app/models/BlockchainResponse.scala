package models

import cryptocurrency.models.{Block, Blockchain}

object BlockchainResponse {

  def apply(blockchain: Blockchain): BlockchainResponse =
    BlockchainResponse(blockchain.chain)

  def apply(chain: Seq[Block]): BlockchainResponse =
    BlockchainResponse(chain, chain.size)
}

case class BlockchainResponse(chain: Seq[Block], length: Int)
