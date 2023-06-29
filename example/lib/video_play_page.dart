import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class VideoPlayPage extends StatefulWidget {
  const VideoPlayPage({Key? key}) : super(key: key);

  @override
  State<VideoPlayPage> createState() => _VideoPlayPageState();
}

class _VideoPlayPageState extends State<VideoPlayPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('play'),
        actions: [
          ElevatedButton(
              onPressed: () {
                SystemChrome.setPreferredOrientations([
                  DeviceOrientation.landscapeLeft,
                  DeviceOrientation.landscapeRight
                ]);
              },
              child: const Text('Full Screen')),
          const SizedBox(
            width: 16,
          ),
          ElevatedButton(
              onPressed: () {
                SystemChrome.setPreferredOrientations([
                  DeviceOrientation.portraitUp,
                  DeviceOrientation.portraitDown
                ]);
              },
              child: const Text('Exit Full Screen'))
        ],
      ),
      body: Container(
        color: Colors.redAccent,
        child: Center(
          child: SizedBox(
            width: MediaQuery.of(context).size.width,
            height: 250,
            child: const AndroidView(viewType: 'video_player'),
          ),
        ),
      ),
    );
  }
}
