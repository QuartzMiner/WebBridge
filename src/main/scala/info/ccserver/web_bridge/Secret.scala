package info.ccserver.web_bridge

import java.io.IOException
import java.security.{InvalidKeyException, NoSuchAlgorithmException}
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import info.ccserver.web_bridge.{WebBridgeConfig => Config}

@throws[IOException]
@throws[NoSuchAlgorithmException]
@throws[InvalidKeyException]
object Secret {
  private def b64encode(src: Array[Byte]) = Base64.getEncoder.encode(src)

  def sign(msg: String, secret: String = Config.appKey, algorithm: String = "HmacSHA1"): Array[Byte] = {
    val mac = Mac.getInstance(algorithm)
    mac.init(new SecretKeySpec(secret.getBytes, algorithm))
    val data = b64encode(msg.getBytes)
    val signBytes = mac.doFinal(data)
    data ++ ".".getBytes ++ b64encode(signBytes)
  }
}
