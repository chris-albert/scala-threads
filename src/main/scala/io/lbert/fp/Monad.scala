package io.lbert.fp

trait Monad[F[_]] extends Applicative[F] {

  def flatMap[A, B](f: A => F[B]): F[A] => F[B]

  def flatten[A](ff: F[F[A]]): F[A] =
    flatMap[F[A], A](identity)(ff)

  override def ap[A, B](ff: F[A => B]): F[A] => F[B] = fa =>
    flatMap[A => B, B](map(_)(fa))(ff)

  override def map[A, B](f: A => B): F[A] => F[B] =
    flatMap[A, B](a => pure(f(a)))
}

object Monad {

  def apply[F[_]: Monad]: Monad[F] = implicitly[Monad[F]]

}
