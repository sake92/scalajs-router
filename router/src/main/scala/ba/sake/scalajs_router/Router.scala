package ba.sake.scalajs_router

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.window
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.HTMLHtmlElement

trait Component {
  def asElement: Element
}

object Router {
  type Routes = PartialFunction[String, Component]
}

final class Router(mountId: String, routes: Router.Routes, notFoundComponent: Component) {

  private val mountElement = document.getElementById(mountId)

  init()

  def navigateTo(path: String): Unit = {
    window.history.pushState(null, null, path) // change URL
    // trigger whenever URL changes
    window.dispatchEvent(new dom.Event("popstate"))
  }

  private def init(): Unit = {
    refresh()
    attachNavListeners()
    window.addEventListener(
      "popstate",
      { (evt: dom.PopStateEvent) => refresh() },
      false
    )
  }

  private def refresh(): Unit = {
    val component = routes.lift(window.location.href).getOrElse(notFoundComponent)
    mountElement.innerHTML = component.asElement.innerHTML
  }

  private def attachNavListeners(): Unit = {
    val routeLinks = document.body.querySelectorAll("[data-navigate]")
    for (i <- 0 until routeLinks.length) {
      val routeLink = routeLinks.item(i)
      routeLink.addEventListener(
        "click",
        (e: dom.MouseEvent) => {
          val navigateTarget = e.target.asInstanceOf[HTMLHtmlElement].dataset.get("navigate").get
          navigateTo(navigateTarget)
        }
      )
    }
  }
}
