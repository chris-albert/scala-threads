package io.lbert.fp

trait ApplicativeError[F[_], E] extends Applicative[F] {

}

object ApplicativeError {

  def apply[F[_], E](implicit AE: ApplicativeError[F, E]): ApplicativeError[F, E] = AE
}
