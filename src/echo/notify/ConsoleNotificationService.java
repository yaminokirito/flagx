package echo.notify;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notifyUser(String userId, String message){
        System.out.println("[NOTIFY] To " + userId + ": " + message);
    }
}
