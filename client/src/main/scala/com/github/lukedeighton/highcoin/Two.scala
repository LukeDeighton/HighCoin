package com.github.lukedeighton.highcoin

import org.scalajs.dom.Element

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class Two(params: Params) extends js.Object {

  def appendTo(element: Element): Two = js.native

  def makeCircle(x: Double, y: Double, radius: Double): Circle = js.native

  def makeRectangle(x: Double, y: Double, width: Double, height: Double): Rectangle = js.native

  def makeText(message: String, x: Double, y: Double, styles: Styles): Text = js.native

  def update(): Unit = js.native
}

trait Params extends js.Object {
  var width: Int
  var height: Int
}

object Params {

  def apply(width: Int, height: Int): Params =
    js.Dynamic.literal(width = width, height = height).asInstanceOf[Params]
}

@js.native
@JSGlobal
class Circle(x: Double, y: Double, radius: Double) extends js.Object {
  var fill: String = js.native
  var stroke: String = js.native
  var linewidth: Double = js.native
}

@js.native
@JSGlobal
class Rectangle(x: Double, y: Double, width: Double, height: Double) extends js.Object {
  var fill: String = js.native
  var stroke: String = js.native
  var linewidth: Double = js.native
}

@js.native
@JSGlobal
class Text(message: String, x: Double, y: Double, styles: Styles) extends js.Object

trait Styles extends js.Object {
  var size: Double
  var fill: String
  var weight: Double
}

object Styles {

  def apply(size: Double = 13, fill: String = "", weight: Double = 500): Styles =
    js.Dynamic.literal(size = size, fill = fill, weight = weight).asInstanceOf[Styles]
}