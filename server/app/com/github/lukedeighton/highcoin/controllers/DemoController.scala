package com.github.lukedeighton.highcoin.controllers

import javax.inject.{Inject, Singleton}
import com.github.lukedeighton.highcoin.shared.Wallet
import com.github.lukedeighton.highcoin.views.html.index
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class DemoController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def interactiveWallet = Action(Ok(index()))

}
