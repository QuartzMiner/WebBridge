package info.ccserver.web_bridge

import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer

object CommandAuth extends CommandBase {
  override def getName = "ccserver:auth"

  override def getUsage(sender: ICommandSender): String = ""

  override def checkPermission(server: MinecraftServer, sender: ICommandSender): Boolean = true

  override def execute(server: MinecraftServer, sender: ICommandSender, args: Array[String]): Unit = {
    sender match {
      case sender: EntityPlayerMP => args.toSeq match {
          case Seq(token) => Client.sendMinecraftAuth(sender, token)
          case _ =>
        }
      case _ =>
    }
  }

  def commandStr(token: String) = s"/$getName $token"
}
