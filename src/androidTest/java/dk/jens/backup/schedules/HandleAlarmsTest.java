package dk.jens.backup.schedules;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import dk.jens.backup.AbstractInstrumentationTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HandleAlarmsTest extends AbstractInstrumentationTest {
    @Mock
    private AlarmManager alarmManager = mock(AlarmManager.class);

    @Mock
    private HandleAlarms.DeviceIdleChecker deviceIdleChecker = mock(HandleAlarms.DeviceIdleChecker.class);

    @Rule
    public ActivityTestRule<Scheduler> schedulerActivityTestRule =
        new ActivityTestRule<>(Scheduler.class, false, true);

    @Test
    public void test_setAlarm() {
        final HandleAlarms handleAlarms = new HandleAlarms(
            schedulerActivityTestRule.getActivity());
        handleAlarms.alarmManager = alarmManager;
        handleAlarms.deviceIdleChecker = deviceIdleChecker;

        when(deviceIdleChecker.isIdleModeSupported()).thenReturn(true);
        when(deviceIdleChecker.isIgnoringBatteryOptimizations())
            .thenReturn(true);

        handleAlarms.setAlarm(2, 1020);
        final Intent intent = new Intent(
            schedulerActivityTestRule.getActivity(), AlarmReceiver.class);
        intent.putExtra("id", 2);
        final PendingIntent pendingIntent =
            PendingIntent.getBroadcast(schedulerActivityTestRule.getActivity(),
            2, intent, 0);
        verify(alarmManager).setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
            1020L, pendingIntent);
    }
}
