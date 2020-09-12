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
  type Listener = PartialFunction[String, Unit]

  private[Router] case class RoutesData(
      mountId: String,
      routes: Routes,
      notFoundComponent: Component
  )

  def apply(): Router =
    new Router(None, None)

  def apply(mountId: String, routes: Routes, notFoundComponent: Component): Router =
    new Router(Some(RoutesData(mountId, routes, notFoundComponent)), None)

}

// routesData is optional because you sometimes just need to listen for a route change
final class Router private (
    routesData: Option[Router.RoutesData],
    routeListener: Option[Router.Listener]
) {

  private val maybeMountElement = routesData.map(rd => document.getElementById(rd.mountId))

  init()

  def withListener(routeListener: Router.Listener): Router =
    new Router(routesData, Some(routeListener))

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
    val newUrl = window.location.pathname + window.location.search
    routesData.foreach { rd =>
      val component = rd.routes.lift(newUrl).getOrElse(rd.notFoundComponent)
      maybeMountElement.get.innerHTML = component.asElement.innerHTML
    }
    routeListener.foreach { listener =>
      listener.lift(newUrl)
    }
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
