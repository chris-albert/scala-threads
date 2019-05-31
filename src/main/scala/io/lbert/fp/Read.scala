package io.lbert.fp

trait Read[+A] {

  def read(s: String): Option[A]

}

object Read {

  def apply[A: Read]: Read[A] = implicitly[Read[A]]

}
