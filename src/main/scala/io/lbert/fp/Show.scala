package io.lbert.fp

trait Show[-A] {

  def show(a: A): String

}

object Show {

  def apply[A: Show]: Show[A] = implicitly[Show[A]]

}
