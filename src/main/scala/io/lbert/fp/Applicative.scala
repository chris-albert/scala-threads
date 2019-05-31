package io.lbert.fp

trait Applicative[F[_]] extends Functor[F] {

  def pure[A](a: A): F[A]

  def ap[A, B](ff: F[A => B]): F[A] => F[B]

  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    ap(map[A, B => (A, B)](a => b => (a, b))(fa))(fb)

  override def map[A, B](f: A => B): F[A] => F[B] =
    ap(pure(f))
}

object Applicative {

  def apply[F[_]: Applicative]: Applicative[F] = implicitly[Applicative[F]]

}
