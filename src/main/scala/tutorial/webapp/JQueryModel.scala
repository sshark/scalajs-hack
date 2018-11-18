package tutorial.webapp

import org.querki.jquery.{JQuery, JQueryStatic}
import language.implicitConversions

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Modal extends JQuery {
  def close(): JQuery = js.native
  def modal(): JQuery = js.native
}

@js.native
@JSImport("jquery-modal", JSImport.Default)
object Modal extends js.Object

object ModalHelper {
  implicit class JQueryStaticToModal(private val jQuery: JQueryStatic.type)
    extends AnyVal {

    def modal: Modal.type =
      jQuery.asInstanceOf[js.Dynamic].modal.asInstanceOf[Modal.type]

  }

  implicit def toJQueryModal(jQuery: JQuery): Modal = jQuery.asInstanceOf[Modal]
}
