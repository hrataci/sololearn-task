package task.sololearn.com.task.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import task.sololearn.com.task.R;
import task.sololearn.com.task.activities.MainActivity;
import task.sololearn.com.task.utils.NotificationID;

public class NotificationHelper {

    public static void notify(Context context) {
        int notificationId = NotificationID.getID();
        Notification notification = getNotification(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    private static Notification getNotification(Context ctx) {
        Resources resources = ctx.getResources();
        Notification n = buildBasicNotification(makeNotificationBuilder(ctx), ctx, resources.getString(R.string.notification_header),
                resources.getString(R.string.notification_body));
        n.sound = Uri.parse("content://settings/system/notification_sound");
        return n;
    }

    private static NotificationCompat.Builder makeNotificationBuilder(Context context) {
        String channelId = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(context);
        }
        return new NotificationCompat.Builder(context, channelId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String createNotificationChannel(Context ctx) {
        String channelId = "Task sololearn";
        String channelName = "Task sololearn channel";
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager service = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    private static Notification buildBasicNotification(NotificationCompat.Builder notificationBuilder,
                                                       Context context, String title, String summaryText ) {
        PendingIntent clickIntent = MainActivity.notificationClickIntent(context);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(summaryText);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(clickIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setWhen(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
            }else{
                notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
            }
            return notificationBuilder.build();
        } else {
            return notificationBuilder.build();
        }
    }
}
