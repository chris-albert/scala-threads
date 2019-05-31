package io.lbert.fp.effect

import io.lbert.fp.MonadError

trait Sync[F[_], E] extends MonadError[F, E] {

  def delay[A](thunk: => A): F[A]

  def suspend[A](thunk: => F[A]): F[A]

}

object Sync {

  def apply[F[_], E](implicit S: Sync[F, E]): Sync[F, E] = S

}