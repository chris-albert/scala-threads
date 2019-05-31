package io.lbert.fp

trait MonadError[F[_], E] extends ApplicativeError[F, E] with Monad[F]

object MonadError {

  def apply[F[_], E](implicit ME: MonadError[F, E]): MonadError[F, E] = ME

}
