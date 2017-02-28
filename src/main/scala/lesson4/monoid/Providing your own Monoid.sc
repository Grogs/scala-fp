//Let's start with some imports
import cats.{Semigroup, Monoid} //The typeclasses
import cats.syntax.semigroup._ //The syntax

//import cats.instances.int.catsKernelStdGroupForInt
implicit object IntMultiplication extends Monoid[Int] {
  def empty = 1
  def combine(x: Int, y: Int) = x * y
}

3 combine 4



//Motivating example
case class Avg(total: Int, count: Int = 1) {
  def value = total.toDouble / count
  override def toString = value.toString
}

implicit object AvgMonoid extends Monoid[Avg] {
  def empty = Avg(0,0)
  def combine(x: Avg, y: Avg) = Avg(x.total + y.total, x.count + y.count)
}

//It composes too
import cats.instances.map._
val clarityTimesheet1 = Map(
  "Mon" -> Avg(7),
  "Tue" -> Avg(8),
  "Wed" -> Avg(9),
  "Thu" -> Avg(8),
  "Fri" -> Avg(7)
)

val clarityTimesheet2 = Map(
  "Mon" -> Avg(9),
  "Tue" -> Avg(8),
  "Wed" -> Avg(7),
  "Thu" -> Avg(8),
  "Fri" -> Avg(4) //Half day
)

val clarityTimesheet3 = Map(
  "Mon" -> Avg(8),
  "Tue" -> Avg(8),
  "Wed" -> Avg(7),
  "Thu" -> Avg(8),
  "Fri" -> Avg(7) //Half day
)

clarityTimesheet1 |+| clarityTimesheet2


val allTimesheets = Seq(clarityTimesheet1, clarityTimesheet2, clarityTimesheet3)
val avgWeek = Monoid.combineAll(allTimesheets)
val avgDay = Monoid.combineAll(avgWeek.values)

//Question:
//  We could call Monoid.combineAll with an empty list.
//  So what would happen if we only provide Semigroup[Avg]?
