package chen;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

public class MainTest {

    @Test
    public  void getValInInfiniteStream() {
        Mono.just(1)
                .repeat()
                .map(e -> new Random().nextInt(10))
                .doOnNext(e -> System.out.println(e))
                .filter(e -> e.equals(0))
                .blockFirst();
    }

    @Test
    public  void stopInfiniteStream() {
        Mono.just(1)
                .repeat()
                .map(e -> new Random().nextInt(10))
                .doOnNext(e -> System.out.println(e))
                .any(e -> e == 5)
                .subscribe(e -> System.out.println(e));
    }
    @Test
    public  void zip(){
        Flux.create(sink -> {
            char a = 'a';
            while (a <= 'z') {
                sink.next(a++);
            }
            sink.complete();
        }).zipWith(Flux.range(1, Integer.MAX_VALUE), (a, b) -> String.format("%s.%s", b, a))
                .subscribe(e -> System.out.println(e));
    }

    @Test
    public void concat(){
        Mono.just(1)
                .concatWith(Mono.just(2))
                .subscribe(e -> System.out.println(e));
    }

    @Test
    public void backPressure(){
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    int onNextAmount;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        onNextAmount++;
                        if (onNextAmount % 2 == 0) {
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });
    }

    /**
     * 订阅时间流不会阻塞
     */
    @Test
    public void interval(){
        Flux.interval(Duration.ofMillis(500)).subscribe(e -> System.out.println(e));
    }

    @Test
    public void hotStream(){
        ConnectableFlux<Long> publish = Flux.interval(Duration.ofMillis(500))
                .publish();
        publish.subscribe(e -> System.out.println("stream 1 " + e));
        publish.subscribe(e -> System.out.println("stream 2 " + e));
        publish.connect();
        while (true) {

        }
    }

    /**
     * same as sleep ,
     */
    @Test
    public void sample(){
        ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        })
                .sample(Duration.ofMillis(1000))
                .doOnNext(e->{
                    System.out.println(e);
                })
                .publish();

        publish.subscribe();
        publish.connect();

    }

    @Test
    public void abc(){

    }



}
