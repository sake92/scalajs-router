# scalajs-router [![Maven Central](https://img.shields.io/maven-central/v/ba.sake/scalajs-router_sjs1_2.13.svg?style=flat-square&label=Scala+2.13)](https://mvnrepository.com/artifact/ba.sake/scalajs-router) [![Build Status](https://img.shields.io/travis/sake92/scalajs-router/master.svg?logo=travis&style=flat-square)](https://travis-ci.com/sake92/scalajs-router) 

ScalaJS frontend router.

In your HTML add `data-navigate` attribute to nav elements:

```html
<nav>
  <button data-navigate="/home">Home</button>
  <button data-navigate="/users/1">User details</button>
</nav>
```

Create element where router will show your dynamic content:
```html
<div id="main"></div>
```

Then specify your routes and bind the router:
```scala
val routes: Router.Routes = {
  case "/home"        => HomeComponent
  case s"/users/$id"  => UserDetailsComponent(id.toLong)
  case _              => NotFoundComponent
}

Router("main", routes).init()

// components
object HomeComponent extends Component {
  def asElement: Element = ...
}
case class UserDetailsComponent(userId: Long) extends Component ..
object NotFoundComponent extends Component ..
```

---
You can attach a listener when a route changes:
```scala
Router().withListener {
  case "/active"    => // do something...
  case "/other"     => 
  case whateverElse => 
}.init()
```

---
With [`@Route` macro](https://github.com/sake92/stone#route) 
you can simplify your routes matching to this:

```scala
@Route class HomeRoute(p1: "home")()
@Route class UserDetailsRoute(p1: "users", val userId: Long)()

val routes: Router.Routes = {
  case HomeRoute()              => HomeComponent
  case UserDetailsRoute(userId) => UserDetailsComponent(userId)
}
```
