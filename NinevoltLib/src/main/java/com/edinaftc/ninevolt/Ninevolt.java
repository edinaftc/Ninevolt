package com.edinaftc.ninevolt;

import android.app.Activity;
import android.widget.TextView;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Ninevolt {
  private static Config config = new Config();

  public static void setConfig(Config config) {
    Ninevolt.config = config;
  }

  public static Config getConfig() {
    return config;
  }

  public static void addVersionCode(OpMode ctx) {
    int textId = ctx.hardwareMap.appContext.getResources()
        .getIdentifier("textDeviceName", "id",
            ctx.hardwareMap.appContext.getPackageName());
    TextView txt = ((TextView) ((Activity) ctx.hardwareMap.appContext).findViewById(textId));
    txt.setText(String.format("%s (Ninevolt %s)", txt.getText(), BuildConfig.VERSION_NAME));
  }
}
