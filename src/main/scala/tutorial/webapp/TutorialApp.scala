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

    $(
      """<div id="ex1" class="modal">
        |  <p>Thanks for clicking. That felt good.</p>
        |  <a href="#" rel="modal:close">Close</a>
        |</div>
        |<p><a href="#ex1" rel="modal:open">Open Modal</a></p>
        |<div><button type="button">Open Modal Too</button></div>"""
        .stripMargin).click(() => $("#ex1").modal()).appendTo($("body"))

    Modal
  }
}
