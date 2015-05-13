package de.itm.uni_luebeck.de;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;

import java.util.concurrent.Callable;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "Init Controller | LED | Fotowiderstand" );
        final GpioController gpio = GpioFactory.getInstance();

        GpioPinDigitalOutput myLED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29,
                "My LED",
                PinState.LOW);

        final GpioPinDigitalInput myPhoto = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
                PinPullResistance.PULL_DOWN);

        if (myPhoto.getState() == PinState.HIGH) {
            myLED.low();
        }
        else {
            myLED.high();
        }

        System.out.println("Init beendet");

        myPhoto.addTrigger(new GpioSetStateTrigger(PinState.HIGH,myLED,PinState.LOW));
        myPhoto.addTrigger(new GpioSetStateTrigger(PinState.LOW,myLED,PinState.HIGH));

        myPhoto.addTrigger(new GpioCallbackTrigger(new Callable<Void>() {
            public Void call() throws Exception {
                System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ");
                return null;
            }
        }));

        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }
    }

}
