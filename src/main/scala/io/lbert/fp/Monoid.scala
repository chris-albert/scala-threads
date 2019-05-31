package io.lbert.fp

trait Monoid[A] extends Semigroup[A] {

  def empty: A

  def combineAll(a: TraversableOnce[A]): A =
    a.foldLeft(empty)(combine)
}

object Monoid {

  def apply[A: Monoid]: Monoid[A] = implicitly[Monoid[A]]

}

