//Let's start with some imports
import cats.{Semigroup, Monoid} //The typeclasses
import cats.syntax.semigroup._ //The syntax
import cats.instances.all._ //The typeclass instances

//Now we can do this:
//With Semigroup:
3 combine 4
"a" combine "b"

//With Monoid:
Monoid[Int].empty //0
Monoid[String].empty //""
Monoid[Map[String, Int]].empty //Map.empty

//Composition
(1, 2) |+| (3, 4)
Map("a" -> 1, "b" -> 2) combine Map("b" -> 1, "c" -> 2)



//Functions
Monoid[Function1[Int, Int]].empty // _ => Monoid[Int].empty
Monoid[Function1[Int, String]].empty // _ => Monoid[String].empty

val increment = (i: Int) => i + 1
val double = (i: Int) => i * 2

val incrementAndDouble = increment |+| double
incrementAndDouble(3) //Increment and double, then combine.




