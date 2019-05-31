package io.lbert.fp

trait Eq[A] {

  def eq(a1: A, a2: A): Boolean

}

object Eq {

  def apply[A: Eq]: Eq[A] = implicitly[Eq[A]]

}

