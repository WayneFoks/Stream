package fok.stream;

import android.support.test.runner.AndroidJUnit4;

import fok.stream.action.Action;
import fok.stream.action.AsyncAction;
import fok.stream.schedule.SchedulerFactory;
import fok.stream.subscribe.Subsriber;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StreamInstrumentedTest {
    @Test
    public void testCase() {
        Stream
            .from("String 1")
            .to(new Action<String, Integer>() {
                @Override
                public Integer action(String input) {
                    assertEquals(input, "String 1");
                    return 100;
                }
            })
            .on(SchedulerFactory.ui())
            .to(new AsyncAction<Integer, Double>() {
                @Override
                public void action(Integer input, Result<Double> result) {
                    assertEquals(input, Integer.valueOf(100));
                    result.result(1.01);
                }
            })
            .to(new Action<Double, String>() {
                @Override
                public String action(Double input) {
                    assertEquals(input, Double.valueOf(1.01));
                    return "String 2";
                }
            })
            .on(SchedulerFactory.io())
            .subscribe(new Subsriber<String>() {
                @Override
                public void subscribe(String value) {
                    assertEquals(value, "String 2");
                }
            });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
