package com.dmsduf.socketio_test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.dmsduf.socketio_test.chat.ChattingActivity;

public class Notification {
    Context context;
    public Notification(Context context){
        this.context = context;
    }
    public void send_notification(String msg) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //노티피케이션 인텐트-> 노티피케이션 클릭시 팬딩인텐트를 통하여 어느 엑티비티로 이동할지 설정할 수 있음.
        Intent noti_intent =  new Intent(context, ChattingActivity.class);
        //값전달
//        noti_intent.putExtra("room_code", msg.split("::")[2]);
//        noti_intent.putExtra("come", 1);
        noti_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        //PendingIntent.FLAG_UPDATE_CURRENT를 줘야 내가 준 내용이 갱신이 됨
        PendingIntent pending_chattingroom = PendingIntent.getActivity(context, 0,
                noti_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남


            String channel_name = "채팅메세지 채널";
            String description = "채팅목록 또는 채팅방이아니여서 메세지를 보냄";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("default", channel_name, importance);
            channel.setDescription(description);


            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        } else
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        String[] msgs = msg.split("::");

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())

                .setContentTitle("메세지가 왔습니다.")
                .setContentText(msgs[1] + ":" + msgs[3])
                .setSubText("(방제목: " + msgs[4] + ")")

                .setContentInfo("INFO")
                .setContentIntent(pending_chattingroom)
                .setOnlyAlertOnce(false);

        notificationManager.notify(1234, builder.build());
    }




    //이건 서비스 무한으로 돌릴때쓰는거
    public void initializeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();

        style.setBigContentTitle(null);
        style.setSummaryText("chat");
        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);
        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }
        android.app.Notification notification = builder.build();
//        startForeground(1, notification);
    }
}
