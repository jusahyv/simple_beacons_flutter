import 'dart:async';

import 'package:flutter/services.dart';

class BeaconsPlugin {
  static const MethodChannel _channel = const MethodChannel('beacons_plugin');
  static const channel = EventChannel('beacons_plugin_stream');
  static const region_events_channel = EventChannel('beacons_region_stream');

  static Future<String> get startMonitoring async {
    final String result = await _channel.invokeMethod('startMonitoring');
    print(result);
    return result;
  }

  static Future<String> get stopMonitoring async {
    final String result = await _channel.invokeMethod('stopMonitoring');
    print(result);
    return result;
  }

  static Future<String> addRegion(String identifier) async {
    final String result = await _channel
        .invokeMethod('addRegion', <String, dynamic>{'identifier': identifier});
    print(result);
    return result;
  }

  static listenToBeacons(StreamController controller) async {
    channel.receiveBroadcastStream().listen((dynamic event) {
      print('Received: $event');
      controller.add(event);
    }, onError: (dynamic error) {
      print('Received error: ${error.message}');
    });
  }

  static listenToRegionEvents(StreamController controller) async {
    region_events_channel.receiveBroadcastStream().listen((dynamic event) {
      print('Received: $event');
      controller.add(event);
    }, onError: (dynamic error) {
      print('Received error: ${error.message}');
    });
  }
}