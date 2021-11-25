package course6.tools

import java.time.OffsetDateTime

import zio.Exit.Failure
import zio._
import zio.logging.slf4j.Slf4jLogger
import zio.logging._

package object ziologging {
  object LoggerTool {
    type LoggerTool = Has[LoggerTool.Service]
    trait Service extends Logger[String] {
      val logger: Logger[String]
      def lines: UIO[Vector[(LogContext, () => UIO[Unit])]]
    }

    val logFormat = "%s"
    val correlationId = LogAnnotation[String](
      name = "test",
      initialValue = "",
      combine = (_, newValue) => newValue,
      render = identity
    )
    val env = Slf4jLogger.make((context, message) => logFormat.format(message))

    def make:ZManaged[ZEnv, Nothing, Has[Logger[String]] with Has[Service]] = {
      ZManaged.makeExit(for {
        downStreamLogger <- ZIO.service[Logger[String]]
        data <- Ref.make(Vector.empty[(LogContext, () => UIO[Unit])])
        logger <- FiberRef
          .make(LogContext.empty)
          .map { ref =>
            new Logger[String] with LoggerTool.Service {
              val logger = downStreamLogger

              def locally[R1, E, A](f: LogContext => LogContext)(zio: ZIO[R1, E, A]): ZIO[R1, E, A] = {
                ref.get.flatMap(context => ref.locally(f(context))(zio))
              }

              def log(line: => String): UIO[Unit] = {
                ref.get.flatMap{ context =>
                  val level = context.get(LogAnnotation.Level)
                  val addTime = context.annotate(LogAnnotation.Timestamp, OffsetDateTime.now())
                  if(level == LogLevel.Debug) {
                    val f = () => downStreamLogger.locally(_ => addTime)(downStreamLogger.log(line))
                    ref.get.flatMap(context => data.update(_ :+ (addTime, f)).unit)
                  }
                  else {
                    downStreamLogger.locally(_ => addTime)(downStreamLogger.log(line))
                  }
                }
              }

              def logContext: UIO[LogContext] = ref.get

              def lines: UIO[Vector[(LogContext, () => UIO[Unit])]] = data.get
            }
          }
      } yield Has.allOf[Logger[String], LoggerTool.Service](logger, logger)) {
        (service, exit) => exit match {
          case Failure(e) =>
            doRealLog(service.get[LoggerTool.Service], _ => true) andThen
              service.get[LoggerTool.Service].logger.log(LogLevel.Error)(e.prettyPrint)
          case _ =>
            ZIO.unit
        }
      }
    }.provideCustomLayer(env)

    private def doRealLog(service: LoggerTool.Service, logLevel: LogLevel => Boolean): UIO[Unit] = {
      service.lines.flatMap { logs =>
        logs.foldLeft(ZIO.unit) {
          case (uio, (context, f)) =>
            val tt = context
            uio >>> f().when(logLevel(context.get(LogAnnotation.Level)))
        }
      }
    }

    def lines = ZIO.accessM[LoggerTool](_.get.lines)
  }

  def live = LoggerTool.make.provideCustomLayer(ZEnv.live).toLayerMany
}
