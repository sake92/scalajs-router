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
  type Routes = Function[String, Component]
  type Listener = PartialFunction[String, Unit]

  private[Router] case class RoutesData(
      mountId: String,
      routes: Routes
  )

  def apply(): Router =
    new Router()

  def apply(mountId: String, routes: Routes): Router =
    new Router(routesData = Some(RoutesData(mountId, routes)))

  def apply(
      baseUrl: String,
      mountId: String,
      routes: Routes
  ): Router =
    new Router(baseUrl = baseUrl, routesData = Some(RoutesData(mountId, routes)))

}

final class Router private (
    baseUrl: String = "",
    routesData: Option[Router.RoutesData] = None,
    routeListener: Option[Router.Listener] = None
) {

  private val maybeMountElement = routesData.map(rd => document.getElementById(rd.mountId))

  def withBaseUrl(baseUrl: String): Router =
    new Router(baseUrl, routesData, routeListener)

  def withListener(routeListener: Router.Listener): Router =
    new Router(baseUrl, routesData, Some(routeListener))

  def withRoutesData(mountId: String, routes: Router.Routes): Router =
    new Router(
      baseUrl,
      routesData = Some(Router.RoutesData(mountId, routes)),
      routeListener
    )

  def navigateTo(path: String): Unit = {
    val newUrl = baseUrl + path
    window.history.pushState(null, null, newUrl) // change URL
    // trigger whenever URL changes
    window.dispatchEvent(new dom.Event("popstate"))
  }

  /**
    * To avoid attaching click listeners twice (when Router is instantiated),
    * we let user manually specify when that should happen
    */
  def init(): Unit = {
    refresh()
    attachNavListeners()
    window.addEventListener(
      "popstate",
      { (evt: dom.PopStateEvent) => refresh() },
      false
    )
  }

  private def refresh(): Unit = {
    // remove baseUrl from matching..
    val newUrl = window.location.pathname.drop(baseUrl.length) + window.location.search
    routesData.foreach { rd =>
      val component = rd.routes(newUrl)
      val mountElem = maybeMountElement.get
      mountElem.innerHTML = ""
      mountElem.appendChild(component.asElement)
    }
    routeListener.foreach { listener =>
      listener.lift(newUrl)
    }
  }

  // this is for static elements, e.g. links in navbar
  // dynamic ones can use navigateTo(..)
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
