# scalajs-router

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
  case "/home"    => HomeComponent
  case "/users/1" => UserDetailsComponent(1)
}

val router = new Router("main", routes, NotFoundComponent)

// components
object HomeComponent extends Component {
  def asElement: Element = ...
}
case class UserDetailsComponent(userId: Long) extends Component ..
object NotFoundComponent extends Component ..
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