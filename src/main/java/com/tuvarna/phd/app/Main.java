package com.tuvarna.phd.app;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {

  public static void main(String... args) {
    System.out.println(
        """
         ___ _            _   _                  _       _                   _
        / __| |_ __ _ _ _| |_(_)_ _  __ _   _ __| |_  __| |  ___ ___ _ ___ _(_)__ ___
        \\__ \\  _/ _` | '_|  _| | ' \\/ _` | | '_ \\ ' \\/ _` | (_-</ -_) '_\\ V / / _/ -_)
        |___/\\__\\__,_|_|  \\__|_|_||_\\__, | | .__/_||_\\__,_| /__/\\___|_|  \\_/|_\\__\\___|
                                |___/  |_|
        """);
    Quarkus.run(args);
  }
}
