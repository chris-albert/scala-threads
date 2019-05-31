package io.lbert

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import cats.effect._
import scala.collection.mutable
import scala.util.Try

object Main extends IOApp {

  trait Runnable {
    def run(): Unit
  }

  trait Callable[A] {
    def call(): A
  }

  case class MyThread(runnable: Runnable, name: String) {
    def start(): Unit = runnable.run()
  }

  case class PrintingRunnable(msg: String) extends Runnable {
    override def run(): Unit = println(s"PrintingRunnable: [$msg]")
  }

  case class PausingRunnable(msg: String) extends Runnable {
    override def run(): Unit = {
      println(s"PrintingRunnable: [$msg]")
      Thread.sleep(3000)
    }
  }

  trait Executor {
    def execute(runnable: Runnable): Unit
  }

  class SimpleExecutor extends Executor {
    override def execute(command: Runnable): Unit = {
      println(s"SimpleExecutor.execute")
      command.run()
    }
  }

  case class NormExecutor(threads: Int, name: String = "Norm") extends Executor {

    private val workQueue: ConcurrentLinkedQueue[Runnable] = new ConcurrentLinkedQueue[Runnable]()
    private val threadQueue: mutable.Queue[java.lang.Thread] = new mutable.Queue[Thread]()
    private val active: AtomicBoolean = new AtomicBoolean(true)

    class WorkerThread(name: String) extends Thread(name) {
      override def run(): Unit = {
        Try {
          while(active.get() || !workQueue.isEmpty) {
            println(s"Looking for work [$name]")
            Option(workQueue.poll()).fold(Thread.sleep(1))(_.run())
          }
        }.recover {
          case _ => ()
        }
      }
    }

    private def prefillThreadQueue(): Unit = {
      (0 until threads).foreach(t => threadQueue.enqueue(new WorkerThread(s"NormExecutor-$name-$t")))
      threadQueue.foreach(_.start)
    }

    override def execute(runnable: Runnable): Unit =
      addWork(runnable)

    private def addWork(runnable: Runnable): Unit = {
      println(s"NormExecutor.addWork")
      workQueue.add(runnable)
    }

    def containsWork: Boolean = !workQueue.isEmpty

    prefillThreadQueue()
  }

  def execRunnables(executor: Executor, runnables: List[Runnable]): Unit =
    runnables.foreach(executor.execute)

  def run(args: List[String]): IO[ExitCode] = IO {

    val runnables = List(
      PausingRunnable("Hi, im a runnable 1"),
      PausingRunnable("Hi, im a runnable 2"),
      PausingRunnable("Hi, im a runnable 3")
    )
    println("\nSimpleExecutor\n")
    execRunnables(new SimpleExecutor, runnables)
    println("\nNormExecutor\n")
    val norm = NormExecutor(2)
    execRunnables(norm, runnables)
    while(norm.containsWork) {

    }


    ExitCode.Success
  }
}
