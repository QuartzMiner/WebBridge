package info.ccserver.web_bridge

import net.minecraftforge.fml.common.Mod
import Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent}
import org.apache.logging.log4j.Logger

import info.ccserver.web_bridge.{ WebBridgeConfig => Config }

import WebBridge._
@Mod(modid = MOD_ID, name = NAME, version = VERSION, serverSideOnly = true, modLanguage = "scala", acceptableRemoteVersions = "*")
object WebBridge {
  final val MOD_ID = "web_bridge"
  final val NAME = "ccserver Web Bridge"
  final val VERSION = "0.1"

  private var _logger: Logger = _
  def logger = _logger

  @EventHandler def preInit(event: FMLPreInitializationEvent): Unit = {
    _logger = event.getModLog
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    Server.serve(Config.modPort)
  }

  @EventHandler
  def serverLoad(event: FMLServerStartingEvent): Unit = {
    event.registerServerCommand(CommandAuth)
  }
}