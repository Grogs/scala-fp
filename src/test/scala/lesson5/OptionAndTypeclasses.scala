package lesson5

import cats.{Functor, Monad, MonadFilter}
import org.scalatest.{FunSuite, Matchers}

import scala.{None => _, Option => _, Some => _} //Disable stdlib Option

class OptionAndTypeclasses extends FunSuite with Matchers {



  sealed trait Option[+A] {
    def getOrElse[B>:A](default: => B): B = this match {
      case Some(a) => a
      case None => default
    }
    def orElse[B>:A](ob: => Option[B]): Option[B] = this match {
      case Some(a) => Some(a)
      case None => ob
    }
  }

  object Option {
    def apply[T](obj: T) = if (obj == null) None else Some(obj)
  }

  case class Some[+A](get: A) extends Option[A]
  case object None extends Option[Nothing]



  test("Functor[Option] provides .map") {

    implicit object OptionFunctor extends Functor[Option] {
      def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = {
        ???
      }
    }

    import cats.syntax.functor._

    Option(5).map(_.toString) shouldBe Some("5")
    None.map((_:Any).toString) shouldBe None
  }



  test("Monad[Option] provides .flatMap as well as map") {

    implicit object OptionMonad extends Monad[Option] with RecursiveTailRecM[Option] {

      def flatMap[A, B](fa: Option[A])(f: (A) => Option[B]): Option[B] = {
        ???
      }

      def pure[A](x: A): Option[A] = {
        ???
      }
    }

    import cats.syntax.flatMap._
    import cats.syntax.functor._

    val o1 = for {
      a <- Option(1)
      b <- Option(2)
    } yield a + b

    o1 shouldBe Option(3)


    val o2 = for {
      a <- Option(1)
      b <- None: Option[Int]
    } yield a + b

    o2 shouldBe None
  }




  test("MonadFilter[Option] gives you map, flatMap, and filter") {

    //Interestingly, we don't actually implement filter. Just implementing .empty makes Monad powerful enough to derive .filter
    implicit object OptionMonadFilter extends MonadFilter[Option] with RecursiveTailRecM[Option] {

      def flatMap[A, B](fa: Option[A])(f: (A) => Option[B]): Option[B] = {
        ???
      }

      def pure[A](x: A): Option[A] = {
        ???
      }

      def empty[A]: Option[A] = {
        ???
      }
    }

    import cats.syntax.all._

    val result = for {
      a <- Option(1)
      b <- Option(2)
      c = a + b
      if c % 2 == 1
    } yield c

    result shouldBe Option(3)
  }

}
