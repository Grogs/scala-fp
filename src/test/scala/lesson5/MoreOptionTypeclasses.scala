package lesson5

import cats.{Applicative, Eval, Foldable, Functor, Monad, MonadFilter, Traverse}
import org.scalatest.{FunSuite, Matchers}
import cats.instances.list._
import cats.instances.int._
import cats.instances.future._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.{None => _, Option => _, Some => _} //Disable stdlib Option

class MoreOptionTypeclasses extends FunSuite with Matchers with ScalaFutures {


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




  val none: Option[Int] = None

  test("Foldable[Option] lets us fold an Option into some summary value") {

    implicit object OptionFoldable extends Foldable[Option] {

      def foldLeft[A, B](fa: Option[A], b: B)(f: (B, A) => B): B = {
        ???
      }

      // Note: Eval is short for Evaluation, and lets us control when `lb` is evaluated.
      // Here, we're using it so that `lb` can be evaluated lazily, which gives us stack safety.
      def foldRight[A, B](fa: Option[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
        ???
      }
    }

    import cats.syntax.foldable._

    //Foldable gives us .foldLeft
    Option(5).foldLeft(4)(_ + _) shouldBe 9

    //Interestingly, folding is really powerful, and lets us derive other useful methods for free:
    Option(8).exists( _ % 2 == 0) shouldBe true
    Option(5).exists( _ % 2 == 0) shouldBe false
    none.exists( _ % 2 == 0) shouldBe false

    Option(3).find(_ > -1) shouldBe scala.Option(3)
    Option(3).filter_(_ > -1) shouldBe List(3)

    //If the Option contains a type which has a Monoid, we can use .combineAll
    //This is equivalent to .sum on collections
    Option(3).combineAll shouldBe 3
    none.combineAll shouldBe 0
  }

  test("Traverse[Option] lets us traverse over a structure with some Applicative effect") {

    implicit object OptionTraverse extends Traverse[Option] {

      def foldLeft[A, B](fa: Option[A], b: B)(f: (B, A) => B): B = {
        ???
      }

      def foldRight[A, B](fa: Option[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
        ???
      }

      def traverse[G[_]: Applicative, In, Out](fa: Option[In])(f: In => G[Out]): G[Option[Out]] = {
        ???
      }
    }

    import cats.syntax.traverse._

    //Traverse also provides sequence, which lets us invert the outer layers
    Option(List(1,2,3)).sequence shouldBe List(Option(1), Option(2), Option(3))

    //It's quite common to write code like this, map over some something, then flip the layer:
    //Here we take our IDs can fetch the names as Future (asynchronous, potentially slow operation)
    val eventualNames: List[Future[String]] =
      List(1, 2, 3).map(fetchName)

    //But then we just want a Future of all the names, so we can sequence it
    val futureNames = eventualNames.sequence
    futureNames.futureValue shouldEqual Future.successful(List("Fred", "Greg", "Alan")).futureValue

    //It turns out that .sequence is a special case of .traverse, and we could skip the immediate result
    //Which is more efficient and concise.
    List(1, 2, 3).traverse(fetchName).futureValue shouldEqual Future.successful(List("Fred", "Greg", "Alan")).futureValue
  }

  def fetchName(id: Int): Future[String] = {
    id match {
      case 1 => Future.successful("Fred")
      case 2 => Future.successful("Greg")
      case 3 => Future.successful("Alan")
      case other => Future.failed(???)
    }
  }

}
