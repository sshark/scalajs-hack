package tutorial.webapp

import org.querki.jquery._
import org.scalajs.dom
import org.scalajs.dom.document
import tutorial.webapp.ModalHelper._

object TutorialApp {
  def main(args: Array[String]): Unit = {
    $(setupUI _)
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  def addClickedMessage(): Unit = {
    appendPar(document.body, "You clicked the button!")
  }

  def setupUI(): Unit = {
/*
    $("""<div><button type="button">Click me!</button></div>""")
      .click(() => $("body").append("<p>Buttion is clicked</p>")).appendTo($("body"))
*/

    $("""<div><button type="button">Open window</button></div>""")
      .click(() => {
        println("try to open dialog")
        $("#ex1").modal()
      }).appendTo($("body"))

    $("body").append("<p>Hello World!!!</p>")
  }
}
