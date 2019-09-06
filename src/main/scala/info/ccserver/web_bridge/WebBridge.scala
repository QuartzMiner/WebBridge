package info.ccserver.web_bridge

import net.minecraftforge.fml.common.Mod
import Mod.EventHandler
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import org.apache.logging.log4j.Logger

import WebBridge._
@Mod(modid = MOD_ID, name = NAME, version = VERSION, serverSideOnly = true, modLanguage = "scala", acceptableRemoteVersions = "*")
object WebBridge {
  final val MOD_ID = "web_bridge"
  final val NAME = "ccserver Web Bridge"
  final val VERSION = "0.1"

  private var logger: Logger = _

  @EventHandler def preInit(event: FMLPreInitializationEvent): Unit = {
    logger = event.getModLog
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = { // some example code
    logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName)
  }
}