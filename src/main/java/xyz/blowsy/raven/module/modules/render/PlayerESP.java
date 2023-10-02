package xyz.blowsy.raven.module.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import xyz.blowsy.raven.clickgui.Theme;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.modules.client.AntiBot;
import xyz.blowsy.raven.module.setting.impl.ComboSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;

import java.util.Iterator;

public class PlayerESP extends Module {
   public static TickSetting redDmg;
   public static ComboSetting<modes> mode;

   public PlayerESP() {
      super("PlayerESP", ModuleCategory.Render, 0);
      this.registerSetting(mode = new ComboSetting<>("Mode", modes.Shaded));
      this.registerSetting(redDmg = new TickSetting("Red on damage", true));
   }

   @Override
   public void onDisable() {
      Utils.HUD.ring_c = false;
   }

   @SubscribeEvent
   public void onRender(RenderWorldEvent e) {
      if (Utils.Player.isPlayerInGame()) {
         Iterator var3;
         var3 = mc.theWorld.playerEntities.iterator();

         while(true) {
            EntityPlayer en;
            do {
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  en = (EntityPlayer) var3.next();
               } while (en == mc.thePlayer);
            } while (en.deathTime != 0);

            if (!AntiBot.bot(en)) {
               this.callRender(en, Theme.getMainColor().getRGB());
            }
         }
      }
   }

   private void callRender(Entity en, int rgb) {
      if (mode.getMode() == modes.Box) {
         Utils.HUD.drawBoxAroundEntity(en, 1, 0.0D, 0.0D, rgb, redDmg.isToggled());
      }

      if (mode.getMode() == modes.Shaded) {
         Utils.HUD.drawBoxAroundEntity(en, 2, 0.0D, 0.0D, rgb, redDmg.isToggled());
      }

      if (mode.getMode() == modes.Both) {
         Utils.HUD.drawBoxAroundEntity(en, 1, 0.0D, 0.0D, rgb, redDmg.isToggled());
         Utils.HUD.drawBoxAroundEntity(en, 2, 0.0D, 0.0D, rgb, redDmg.isToggled());
      }

   }

   public enum modes {
      Box, Shaded, Both
   }
}