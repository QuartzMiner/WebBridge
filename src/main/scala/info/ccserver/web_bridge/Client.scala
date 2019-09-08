package info.ccserver.web_bridge
import java.net.{HttpURLConnection, URL, URLEncoder}

import scala.io.Source.fromInputStream
import net.minecraft.entity.player.EntityPlayerMP
import info.ccserver.web_bridge.{WebBridgeConfig => Config}
import info.ccserver.web_bridge.WebBridge.logger

object Client {
  def sendMinecraftAuth(player: EntityPlayerMP, token: String): Unit = {
    var con: HttpURLConnection = null
    try {
      val t = token.replaceAll("[^A-Za-z0-9+/=]", "") // base64に限定
      val url = new URL(s"${Config.webAppBase}/authenticate")
      val data = s"""{"user": "${player.getName}", "token": "$t"}"""
      logger.info(s"send auth payload to WebApp : ${Config.webAppBase}/authenticate '{}'", data)
      con = url.openConnection().asInstanceOf[HttpURLConnection]
      con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
      con.setRequestMethod("POST")
      con.setDoOutput(true)
      con.setDoInput(true)
      con.connect()
      val out = con.getOutputStream
      out.write(("data=" + URLEncoder.encode(new String(Secret.sign(data), "UTF-8"), "UTF-8")).getBytes)
      out.close()
      logger.info("send.")
      logger.debug(fromInputStream(con.getInputStream).mkString)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        if (con != null) {
          logger.warn(fromInputStream(con.getErrorStream).mkString)
        }
    } finally { if (con != null) { con.disconnect() } }
  }
}
