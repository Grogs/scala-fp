package lesson5

import cats.Monad

import scala.language.higherKinds

trait RecursiveTailRecM[T[_]] { self: Monad[T] =>
  //So you don't have to implement tailRecM
  override def tailRecM[A, B](a: A)(f: (A) => T[Either[A, B]]) = {
    def unwrapEither(f: (A) => T[Either[A, B]]) = f.andThen(r =>
      map(r)(_.right.get)
    )
    flatMap(pure(a))(unwrapEither(f))
  }
}
