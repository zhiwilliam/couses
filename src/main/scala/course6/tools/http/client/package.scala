package course6.tools.http

import org.apache.http.HttpEntity
import zio._

package object client {
  import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
  import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
  import org.apache.http.util.EntityUtils
  type HasResponse = Has[CloseableHttpResponse]
  type HTTPClient = Has[CloseableHttpClient]
  type HasHttpEntity = Has[HttpEntity]
  import java.io.InputStream
  type ReadStream = Has[InputStream]

  private val httpClient: ZManaged[Any, Throwable, CloseableHttpClient] =
    Managed.fromAutoCloseable(IO.effect(HttpClients.createDefault()))

  private def httpResponse(uri: String): ZManaged[CloseableHttpClient, Throwable, CloseableHttpResponse] =
    Managed.fromAutoCloseable(
      ZIO.environment[CloseableHttpClient] >>=
      (client => {
        val get = new HttpGet(uri)
        get.addHeader("X-Finnhub-Secret", "bv83fhf48v6vtpa0fjlg")
        IO.effect(client.execute(get))
      })
    )

  private def getEntity(uri: String): ZManaged[CloseableHttpResponse, Throwable, HttpEntity] = {
    val httpEntity = for {
      response <- ZIO.environment[CloseableHttpResponse]
      entity <- IO.effect {
        val httpCode = response.getStatusLine.getStatusCode
        if (httpCode != 200) {
          throw new Exception(s"Failed to get from $uri with http code $httpCode")
        }
        else {
          response.getEntity
        }
      }
    } yield entity
    ZManaged.make {
      httpEntity.map(ent => if(ent == null) throw new Exception("Failed to get entity from http client") else ent)
    }(ent => IO.effect(EntityUtils.consume(ent)).either)
  }

  private def getStream: ZManaged[HttpEntity, Throwable, InputStream] = Managed.fromAutoCloseable(for {
    entity <- ZIO.environment[HttpEntity]
    stream <- IO.effect(entity.getContent)
  } yield stream)

  def live(uri: String): Managed[Throwable, InputStream] =
    httpClient >>> httpResponse(uri) >>> getEntity(uri) >>> getStream

  type HasWeb = Has[ZHttpClient.Service]
  object ZHttpClient {
    trait Service {
      def getUri(uri: String): ZIO[Any, Throwable, InputStream]
    }
    object Service {
      val httpClientImpl = Task.effect {
        new Service {
          override def getUri(uri: String): ZIO[Any, Throwable, InputStream] = (for {
            inputStream <- ZIO.service[InputStream]
          } yield inputStream).provideLayer(live(uri).toLayer)
        }
      }.toLayer
    }
  }

  def http(uri: String) = new {
    def port(portNumber: Int) = new {
      def get(path: String = ""): ZIO[HasWeb, Throwable, InputStream] = {
        ZIO.accessM(_.get.getUri("http://" + uri + ":" + portNumber + "/" + path))
      }
    }
  }
}