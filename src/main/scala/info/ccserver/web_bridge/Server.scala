package info.ccserver.web_bridge

import java.io.Closeable
import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

import scala.io.Source.fromInputStream
import scala.util.parsing.json.JSON.parseFull
import net.minecraft.util.text.{TextComponentString, TextFormatting}
import net.minecraft.util.text.event.ClickEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import WebBridge._

object Server {
  def using[A <: Closeable, B](resource: A)(f: A => B): B = try { f(resource) } finally { if (resource != null) { resource.close() } }
  def req2response (exchange: HttpExchange)(f: HttpExchange => (Int, String)) = try {
    val (status, bodyString) = f(exchange)
    val body = bodyString.getBytes
    exchange.sendResponseHeaders(status, body.length)
    using(exchange.getResponseBody) { _.write(body) }
    exchange.close()
  } catch { case e => e.printStackTrace() }

  def serve(port: Int): Unit = {
    logger.info("web-bridge server listening: port {}", port)
    val server = HttpServer.create(new InetSocketAddress(port), 0)
    server.createContext("/request_auth", new HttpHandler { override def handle(e: HttpExchange): Unit = req2response(e) { exchange =>
      exchange.getRequestMethod match {
        case "POST" => parseFull(fromInputStream(exchange.getRequestBody).mkString).map { _.asInstanceOf[Map[String, String]] }
          .flatMap { input =>
            for {
              user <- input.get("user")
              token <- input.get("token")
            } yield {
              logger.info("Auth required: {}", user)
              val player = FMLCommonHandler.instance.getMinecraftServerInstance.getPlayerList.getPlayerByUsername(user)
              if (player != null) {
                val cmd = new TextComponentString("[認証]")
                cmd.getStyle.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, CommandAuth.commandStr(token)))
                cmd.getStyle.setColor(TextFormatting.YELLOW)
                player.sendMessage(new TextComponentString("ccserver.info にログインしようとしています: ").appendSibling(cmd))
              }
              (200, "")
            }
          }.getOrElse((422, ""))
        case _ => (405, "Method Not Allowed")
      }
    } })
    server.start()
  }
}
