import 'dart:async';
import 'package:flutter/services.dart';

class FlutterVirtualApp {
  static const MethodChannel _channel = const MethodChannel("AKit/flutter_virtual_app");

  static void getInstalledAppsList() async {
    await _channel.invokeMethod('getInstalledAppsList');
  }

  static void installApp() async {
    await _channel.invokeMethod('installApp');
  }

  static void launchApp() async {
    await _channel.invokeMethod('launchApp');
  }
}
