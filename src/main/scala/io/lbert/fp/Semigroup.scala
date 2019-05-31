package io.lbert.fp

import scala.annotation.tailrec

trait Semigroup[A] {

  def combine(a1: A, a2: A): A

  @tailrec
  final def combineN(a: A, times: Int): A =
    if(times <= 0) a else combineN(combine(a, a), times - 1)
}

object Semigroup {

  def apply[A: Semigroup]: Semigroup[A] = implicitly[Semigroup[A]]

}
