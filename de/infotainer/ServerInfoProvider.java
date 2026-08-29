package de.infotainer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

public class ServerInfoProvider {
    public static final String GROEKINGEN_DOMAIN = "mc.grökingen.com";
    public static final String GROEKINGEN_PUNYCODE = "mc.xn--grkingen-beb.com"; // für Punycode Check

    public String getAddress(){
        var s = MinecraftClient.getInstance().getCurrentServerEntry();
        return s != null ? s.address : "Singleplayer";
    }

    public boolean isGroekingen() {
        String addr = getAddress().toLowerCase();
        return addr.contains("grökingen") || addr.contains("grokingen") || 
               addr.contains("xn--grkingen") || addr.contains("groekingen.com");
    }

    public String getMotd(){
        var s = MinecraftClient.getInstance().getCurrentServerEntry();
        if(s == null) return "Local World";
        if(isGroekingen() && s.label != null) {
            return "Grökingen • " + s.label.getString().replaceAll("§.", "");
        }
        return s.label != null ? s.label.getString().replaceAll("§.", "") : "";
    }

    public ServerInfo getInfo(){ 
        return MinecraftClient.getInstance().getCurrentServerEntry(); 
    }
    
    public String getDisplayAddress() {
        if(isGroekingen()) return GROEKINGEN_DOMAIN;
        return getAddress();
    }
}
