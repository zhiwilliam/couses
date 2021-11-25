package course6.tools.zioconfig

import zio.config.getConfig
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.test._
import zio.test.Assertion._

object ConfigTest extends DefaultRunnableSpec {
  object TestBussLogic {

    case class HttpQuery(uri: String, query: String)

    case class ApplicationConfig(demo: HttpQuery)

    val appconf = descriptor[ApplicationConfig]

    def run = for {
      appConfig <- getConfig[ApplicationConfig]
    } yield appConfig.demo.uri + appConfig.demo.query
  }

  import TestBussLogic._

  val xmlConf = readDescription(appconf).provideLayer(readXmlFromResource("test.xml").toLayer)
  val propConf = readDescription(appconf).provideLayer(readPropertiesFromResource("test.properties").toLayer)
  val appConf = readDescription(appconf).provideLayer(readHocconFromResource("application.conf").toLayer)
  val yamlConf = readDescription(appconf).provideLayer(readYamlFromResource("test.yaml").toLayer)

  def spec = suite("Read Config Tests")(
    testM("read xml config from resources") {
      for {
        result <- TestBussLogic.run.provideLayer(xmlConf.toLayer)
      } yield assert(result)(equalTo("http://localhost:8080/config"))
    },
    testM("read properties config from resources") {
      for {
        result <- TestBussLogic.run.provideLayer(propConf.toLayer)
      } yield assert(result)(equalTo("http://localhost:8080/config"))
    },
    testM("read application config from default application.conf file") {
      for {
        result <- TestBussLogic.run.provideLayer(appConf.toLayer)
      } yield assert(result)(equalTo("http://localhost:8080/config"))
    },
    testM("read yaml config from resources yaml file") {
      for {
        result <- TestBussLogic.run.provideLayer(yamlConf.toLayer)
      } yield assert(result)(equalTo("http://localhost:8080/config"))
    },
    /*testM("read config from Map") {
      for {
        result <- TestBussLogic.run.provideLayer(mapConf.toLayer)
      } yield assert(result)(equalTo("http://localhost:8080/config"))
    }*/
  )
}
