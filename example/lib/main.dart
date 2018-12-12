import 'package:flutter/material.dart';
import 'package:flutter_virtual_app/flutter_virtual_app.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('VirtualApp'),
        ),
        body: Column(
          children: <Widget>[
            // 扁平化按钮
            new FlatButton(
              // 字体颜色
                textColor: Colors.white,
                // 背景颜色
                color: Colors.blue,
                // 文字
                child: new Text('getInstalledAppsList'),
                // 扁平化按钮
                onPressed: () async {
                  await FlutterVirtualApp.getInstalledAppsList();
                }),
            // 扁平化按钮
            new FlatButton(
              // 字体颜色
                textColor: Colors.white,
                // 背景颜色
                color: Colors.blue,
                // 文字
                child: new Text('installApp'),
                // 扁平化按钮
                onPressed: () async {
                  await FlutterVirtualApp.installApp();
                }),
            // 扁平化按钮
            new FlatButton(
              // 字体颜色
                textColor: Colors.white,
                // 背景颜色
                color: Colors.blue,
                // 文字
                child: new Text('launchApp'),
                // 扁平化按钮
                onPressed: () async {
                  await FlutterVirtualApp.launchApp();
                })
          ],
        ),
      ),
    );
  }

}
