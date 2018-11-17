package tutorial.webapp

import org.querki.jquery.{JQuery, JQueryStatic}

import scala.scalajs.js


trait JQueryModal extends JQuery {
  def close(): JQuery = js.native
  def modal(): JQuery = js.native
}

@js.native
object JQueryModal extends JQueryModal

object ModalHelper {
  implicit class JQueryStaticToModal(private val jQuery: JQueryStatic.type)
    extends AnyVal {

    @inline def modal: JQueryModal.type =
      jQuery.asInstanceOf[js.Dynamic].modal.asInstanceOf[JQueryModal.type]

  }

  implicit def toJQueryModal(jQuery: JQuery) = jQuery.asInstanceOf[JQueryModal]
}
