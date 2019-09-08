package info.ccserver.web_bridge;

import net.minecraftforge.common.config.Config;

@Config(modid = "web_bridge", name = "web_bridge")
public class WebBridgeConfig {
    public static String appKey = "app key";
    public static String webAppBase = "http://localhost:8000";
    public static int modPort = 8092;
}
